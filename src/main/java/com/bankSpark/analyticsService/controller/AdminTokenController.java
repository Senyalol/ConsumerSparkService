package com.bankSpark.analyticsService.controller;

import com.bankSpark.analyticsService.DTO.inviteTokenDTO.FullTokenInfoDTO;
import com.bankSpark.analyticsService.DTO.inviteTokenDTO.GenerateTokenRequestDTO;
import com.bankSpark.analyticsService.DTO.inviteTokenDTO.TokenResponseDTO;
import com.bankSpark.analyticsService.facade.inviteToken.InviteTokenFacade;
import com.bankSpark.analyticsService.http.HttpResponseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/tokens")
@PreAuthorize("hasRole('ADMIN')")
public class AdminTokenController {

    private final InviteTokenFacade inviteTokenFacade;

    @Autowired
    public AdminTokenController(InviteTokenFacade inviteTokenFacade) {
        this.inviteTokenFacade = inviteTokenFacade;
    }

    @GetMapping
    public ResponseEntity<List<FullTokenInfoDTO>> getAllTokens() {
        return HttpResponseController.build(inviteTokenFacade.getAllInviteTokens());
    }

    @GetMapping("/used")
    public ResponseEntity<List<FullTokenInfoDTO>> getUsedTokens(@RequestParam Boolean used) {
        return HttpResponseController.build(inviteTokenFacade.getAllInviteTokensByUsed(used));
    }

    @PostMapping("/generate")
    public ResponseEntity<TokenResponseDTO> generateToken(@RequestBody(required = false) GenerateTokenRequestDTO request) {

        String role = (request != null) ? request.getRole() : null;
        Integer hours = (request != null) ? request.getHoursValid() : null;

        TokenResponseDTO response = inviteTokenFacade.generateToken(role, hours);
        return ResponseEntity.ok(response);
    }

    /**
     * Генерация токена с параметрами по умолчанию
     */
    @PostMapping("/generate/default")
    public ResponseEntity<TokenResponseDTO> generateDefaultToken() {
        TokenResponseDTO response = inviteTokenFacade.generateDefaultToken();
        return ResponseEntity.ok(response);
    }

    /**
     * Проверка валидности токена
     */
    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        boolean isValid = inviteTokenFacade.isValidToken(token);
        return ResponseEntity.ok(isValid);
    }

    /**
     * Отмена токена
     */
    @DeleteMapping("/revoke")
    public ResponseEntity<String> revokeToken(@RequestParam String token) {
        inviteTokenFacade.revokeToken(token);
        return ResponseEntity.ok("Token revoked: " + token);
    }

    /**
     * Информация о токене
     */
    @GetMapping("/info")
    public ResponseEntity<?> getTokenInfo(@RequestParam String token) {
        return ResponseEntity.ok(inviteTokenFacade.getTokenInfo(token));
    }

    @DeleteMapping("/expired")
    public ResponseEntity<Integer> cleanupExpiredTokens(){
        return ResponseEntity.ok(inviteTokenFacade.cleanupExpiredTokens());
    }

}