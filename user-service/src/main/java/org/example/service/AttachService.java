package org.example.service;

import org.example.dto.AttachDTO;
import org.example.entity.Attach;
import org.example.enums.AppLanguage;
import org.springframework.web.multipart.MultipartFile;

public interface AttachService {

    AttachDTO uploadFile(MultipartFile file, AppLanguage language);

    boolean delete(String id, AppLanguage language);

    Attach get(String id, AppLanguage language);

    byte[] open(String fileId, AppLanguage language);
}
