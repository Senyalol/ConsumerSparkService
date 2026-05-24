package com.bankSpark.analyticsService.security.analystService.createChecks;

import com.bankSpark.analyticsService.DTO.analyst.CreateAnalystDTO;
import com.bankSpark.analyticsService.repository.InviteTokenRepository;

public class AnalystTokenCreateCheck implements AnalystCreateCheck{

    private InviteTokenRepository tokenRepository;

    public AnalystTokenCreateCheck(InviteTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public boolean createCheck(CreateAnalystDTO analyst) {

        boolean result = true;

        if(analyst == null || analyst.getToken() == null ||
                tokenRepository.findByToken(analyst.getToken()).isEmpty()){

            result = false;

        }

        return result;
    }

}