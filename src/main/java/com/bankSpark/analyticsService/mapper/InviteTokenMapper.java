package com.bankSpark.analyticsService.mapper;

import com.bankSpark.analyticsService.DTO.inviteTokenDTO.FullTokenInfoDTO;
import com.bankSpark.analyticsService.ORM.inviteToken.InviteToken;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class InviteTokenMapper {

    //Из сущности в DTO
    public static FullTokenInfoDTO toFullDTO(InviteToken inviteToken) {

        FullTokenInfoDTO fullDTO = new FullTokenInfoDTO();
        fullDTO.setToken(inviteToken.getToken());
        fullDTO.setRole(inviteToken.getRole());
        fullDTO.setUsed(inviteToken.getUsed());

        LocalDateTime expiresAt = inviteToken.getExpiresAt().atZone(ZoneId.systemDefault()).toLocalDateTime();
        fullDTO.setExpiresAt(expiresAt);

        LocalDateTime createAt = inviteToken.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime();
        fullDTO.setCreatedAt(createAt);

        return fullDTO;
    }

}