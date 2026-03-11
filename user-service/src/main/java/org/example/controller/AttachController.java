package org.example.controller;

import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.example.dto.ApiResponse;
import org.example.dto.AttachDTO;
import org.example.entity.Attach;
import org.example.service.AttachService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/attach")
public class AttachController {

    private final AttachService attachService;

    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<AttachDTO> upload(@RequestParam("file") MultipartFile file) {
        AttachDTO dto = attachService.uploadFile(file);
        return ApiResponse.successResponse(dto);
    }

    @PermitAll
    @GetMapping("/open/{id}")
    public ResponseEntity<byte[]> open(@PathVariable String id) {
        byte[] fileData = attachService.open(id);
        Attach attach = attachService.get(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + attach.getOriginalName() + "\"")
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(fileData);
    }

}
