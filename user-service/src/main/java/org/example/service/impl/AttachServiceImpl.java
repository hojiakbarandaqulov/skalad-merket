package org.example.service.impl;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.AttachDTO;
import org.example.entity.Attach;
import org.example.enums.AppLanguage;
import org.example.exp.AppBadException;
import org.example.repository.AttachRepository;
import org.example.service.AttachService;
import org.example.service.ResourceBundleService;
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
    private final ResourceBundleService messageService;

    @Value("${aws.bucket-name}")
    private String bucketName;

    @Value("${aws.url}")
    private String url;

    @Override
    public AttachDTO uploadFile(MultipartFile file, AppLanguage language) {
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
            entity.setId(key + "." + extension);
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
            throw new AppBadException(messageService.getMessage("file.upload.failed", language));
        }
    }

    @Override
    public boolean delete(String id, AppLanguage language) {
        Attach attach = get(id, language);

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
            throw new AppBadException(messageService.getMessage("file.delete.failed", language));
        }
    }

    @Override
    public Attach get(String id, AppLanguage language) {
        return attachRepository.findById(id)
                .orElseThrow(() -> new AppBadException(messageService.getMessage("attach.not.found", language)));
    }

    @Override
    public byte[] open(String fileId, AppLanguage language) {
        Attach attach = get(fileId, language);
        try {
            String objectName = attach.getId().split("\\.")[0];
            try (InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            )) {
                return stream.readAllBytes();
            }
        } catch (Exception e) {
            log.error("Error reading file from MinIO: {}", e.getMessage());
            throw new AppBadException(messageService.getMessage("file.open.failed", language));
        }
    }

}
