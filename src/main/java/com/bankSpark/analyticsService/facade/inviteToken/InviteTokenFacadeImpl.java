package com.bankSpark.analyticsService.facade.inviteToken;

import com.bankSpark.analyticsService.DTO.inviteTokenDTO.FullTokenInfoDTO;
import com.bankSpark.analyticsService.DTO.inviteTokenDTO.TokenResponseDTO;
import com.bankSpark.analyticsService.exception.GenerateKeyException;
import com.bankSpark.analyticsService.mapper.InviteTokenMapper;
import com.bankSpark.analyticsService.security.randomKeyAPI.InviteTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InviteTokenFacadeImpl implements InviteTokenFacade {

    private final InviteTokenService inviteTokenService;

    @Autowired
    public InviteTokenFacadeImpl(InviteTokenService inviteTokenService) {
        this.inviteTokenService = inviteTokenService;
    }

    @Override
    public TokenResponseDTO generateToken(String role, Integer hoursValid) {
        return inviteTokenService.generateToken(role, hoursValid);
    }

    @Override
    public TokenResponseDTO generateDefaultToken() {
        return inviteTokenService.generateDefaultToken();
    }

    @Override
    public FullTokenInfoDTO getTokenInfo(String token) {
        if(inviteTokenService.isValidToken(token)) {
            return InviteTokenMapper.toFullDTO(inviteTokenService.getTokenInfo(token));
        }
        throw new GenerateKeyException();
    }

    @Override
    public void revokeToken(String token) {
        inviteTokenService.revokeToken(token);
    }

    @Override
    public boolean isValidToken(String token) {
        return inviteTokenService.isValidToken(token);
    }

    @Override
    public int cleanupExpiredTokens() {
        return inviteTokenService.cleanupExpiredTokens();
    }

    @Override
    public List<FullTokenInfoDTO> getAllInviteTokens() {
        return inviteTokenService.getAllInviteTokens();
    }

    @Override
    public List<FullTokenInfoDTO> getAllInviteTokensByUsed(Boolean used) {
        return inviteTokenService.getAllInviteTokensByUsed(used);
    }

}