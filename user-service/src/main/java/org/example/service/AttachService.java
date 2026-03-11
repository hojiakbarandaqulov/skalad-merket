package org.example.service;

import org.example.dto.AttachDTO;
import org.example.entity.Attach;
import org.springframework.web.multipart.MultipartFile;

public interface AttachService {


    AttachDTO uploadFile(MultipartFile file);
    boolean delete(String id);
    Attach get(String id);

    byte[] open(String fileId);
}
