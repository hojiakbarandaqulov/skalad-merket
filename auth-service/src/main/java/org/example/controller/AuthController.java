package org.example.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.dto.ApiResponse;
import org.example.dto.RefreshTokenDTO;
import org.example.dto.RegistrationDTO;
import org.example.dto.TokenResponseDTO;
import org.example.dto.auth.LoginDTO;
import org.example.dto.auth.ProfileDTO;
import org.example.dto.auth.ResetPasswordDTO;
import org.example.dto.auth.UpdatePasswordDTO;
import org.example.enums.AppLanguage;
import org.example.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;


    @PostMapping("/registration")
    public ResponseEntity<ApiResponse<String>> registration(@Valid @RequestBody RegistrationDTO dto,
                                                            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        ApiResponse<String> ok = authService.registration(dto, language);
        return ResponseEntity.ok(ok);
    }

    @GetMapping("/verification/{token}")
    public ResponseEntity<ApiResponse<String>> registrationVerification(@PathVariable("token") String token,
                                                                        @RequestParam(value = "Accept-Language",defaultValue = "UZ") AppLanguage lang) {
        ApiResponse<String> ok = authService.regVerification(token, lang);
        return ResponseEntity.ok(ok);
    }

    @PostMapping("/registration/login")
    public ResponseEntity<ApiResponse<ProfileDTO>> login(@Valid @RequestBody LoginDTO dto,
                                                         @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        ApiResponse<ProfileDTO> ok = authService.login(dto, language);
        return ResponseEntity.ok(ok);
    }
    @PostMapping("/refresh")
    public ApiResponse<TokenResponseDTO> refresh(@RequestBody @Valid RefreshTokenDTO dto) {
        return authService.refresh(dto);
    }

    @PostMapping("/registration/reset")
    public ResponseEntity<ApiResponse<String>> resent(@Valid @RequestBody ResetPasswordDTO dto,
                                                      @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        ApiResponse<String> ok = authService.resetPassword(dto, language);
        return ResponseEntity.ok(ok);
    }

    @PostMapping("/registration/reset-password/confirm")
    public ResponseEntity<ApiResponse<String>> resentPassword(@Valid @RequestBody UpdatePasswordDTO dto,
                                                             @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        ApiResponse<String> ok = authService.resetPasswordConfirm(dto, language);
        return ResponseEntity.ok(ok);
    }

}






