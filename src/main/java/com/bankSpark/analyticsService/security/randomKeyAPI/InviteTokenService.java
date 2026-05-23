package com.bankSpark.analyticsService.security.randomKeyAPI;

import com.bankSpark.analyticsService.DTO.inviteTokenDTO.FullTokenInfoDTO;
import com.bankSpark.analyticsService.DTO.inviteTokenDTO.TokenResponseDTO;
import com.bankSpark.analyticsService.ORM.inviteToken.InviteToken;

import java.util.List;

public interface InviteTokenService {

    TokenResponseDTO generateToken(String role, Integer hoursValid);

    TokenResponseDTO generateDefaultToken();

    InviteToken getTokenInfo(String token);

    void revokeToken(String token);

    boolean isValidToken(String token);

    int cleanupExpiredTokens();

    List<FullTokenInfoDTO> getAllInviteTokens();

    List<FullTokenInfoDTO> getAllInviteTokensByUsed(Boolean used);

}