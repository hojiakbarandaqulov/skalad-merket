package org.example.service.impl;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.AttachDTO;
import org.example.entity.Attach;
import org.example.exp.AppBadException;
import org.example.repository.AttachRepository;
import org.example.service.AttachService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachServiceImpl implements AttachService {
    private final AttachRepository attachRepository;
    private final MinioClient minioClient;

    @Value("${aws.bucket-name}")
    private String bucketName;

    @Value("${aws.url}")
    private String url;

    @Override
    public AttachDTO uploadFile(MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();
            assert originalName != null;
            String extension = originalName.substring(originalName.lastIndexOf('.') + 1);
            String key = UUID.randomUUID().toString();

            try (InputStream inputStream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(key)
                                .stream(inputStream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
            }
            Attach entity = new Attach();
            entity.setId(key+"."+extension);
            entity.setOriginalName(originalName);
            entity.setExtension(extension);
            entity.setSize(file.getSize());
            entity.setPath(bucketName + "/" + key);
            entity.setCreatedDate(LocalDateTime.now());
            attachRepository.save(entity);

            AttachDTO dto = new AttachDTO();
            dto.setId(entity.getId());
            dto.setUrl(url + "/" + bucketName + "/" + key);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("File upload error: " + e.getMessage(), e);
        }
    }
    @Override
    public boolean delete(String id) {
        Attach attach = attachRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found: " + id));

        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(id)
                            .build()
            );

            attachRepository.delete(attach);
            return true;
        } catch (Exception e) {
            log.error("Error deleting file from MinIO: {}", e.getMessage());
            throw new RuntimeException("File delete error: " + e.getMessage());
        }
    }

    public Attach get(String id) {
        return attachRepository.findById(id).orElseThrow(()->new AppBadException("Attach not found"));
    }

    @Override
    public byte[] open(String fileId) {
        try {
            Attach attach = attachRepository.findById(fileId)
                    .orElseThrow(() -> new AppBadException("Attach not found"));
            String objectName = attach.getId().split("\\.")[0]; // id = uuid.extension
            try (InputStream stream = minioClient.getObject(
                    io.minio.GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            )) {
                return stream.readAllBytes();
            }
        } catch (Exception e) {
            log.error("Error reading file from MinIO: {}", e.getMessage());
            throw new RuntimeException("File open error: " + e.getMessage(), e);
        }
    }

}
