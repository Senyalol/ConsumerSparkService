package com.bankSpark.analyticsService.facade.inviteToken;

import com.bankSpark.analyticsService.DTO.inviteTokenDTO.FullTokenInfoDTO;
import com.bankSpark.analyticsService.DTO.inviteTokenDTO.TokenResponseDTO;

import java.util.List;

public interface InviteTokenFacade {

    TokenResponseDTO generateToken(String role, Integer hoursValid);

    TokenResponseDTO generateDefaultToken();

    FullTokenInfoDTO getTokenInfo(String token);

    void revokeToken(String token);

    boolean isValidToken(String token);

    int cleanupExpiredTokens();

    List<FullTokenInfoDTO> getAllInviteTokens();

    List<FullTokenInfoDTO> getAllInviteTokensByUsed(Boolean used);

}