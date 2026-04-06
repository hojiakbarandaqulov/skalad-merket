package org.example.controller;

import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.example.dto.AttachDTO;
import org.example.entity.Attach;
import org.example.enums.AppLanguage;
import org.example.service.AttachService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/attach")
public class AttachController {

    private final AttachService attachService;

    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AttachDTO> upload(@RequestParam("file") MultipartFile file,
                                            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        AttachDTO dto = attachService.uploadFile(file, language);
        return ResponseEntity.ok(dto);
    }

    @PermitAll
    @GetMapping("/open/{id}")
    public ResponseEntity<byte[]> open(@PathVariable String id,
                                       @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        byte[] fileData = attachService.open(id, language);
        Attach attach = attachService.get(id, language);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + attach.getOriginalName() + "\"")
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(fileData);
    }

}
