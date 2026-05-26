package com.bankSpark.analyticsService.security.analystService.createChecks;

import com.bankSpark.analyticsService.DTO.analyst.CreateAnalystDTO;
import com.bankSpark.analyticsService.repository.InviteTokenRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnalystTokenCreateCheck implements AnalystCreateCheck{

    private InviteTokenRepository tokenRepository;
    private static final Logger LOGGER = LogManager.getLogger(AnalystTokenCreateCheck.class);


    public AnalystTokenCreateCheck(InviteTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public boolean createCheck(CreateAnalystDTO analyst) {

        boolean result = true;

        if(analyst == null || analyst.getToken() == null ||
                tokenRepository.findByToken(analyst.getToken()).isEmpty()){

            result = false;
            LOGGER.error("analyst token is invalid - {}",analyst.getToken());
        }

        return result;
    }

}