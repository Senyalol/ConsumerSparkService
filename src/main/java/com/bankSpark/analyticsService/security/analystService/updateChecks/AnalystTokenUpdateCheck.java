package com.bankSpark.analyticsService.security.analystService.updateChecks;

import com.bankSpark.analyticsService.DTO.analyst.UpdateAnalystDTO;
import com.bankSpark.analyticsService.ORM.analyst.Analyst;
import com.bankSpark.analyticsService.repository.InviteTokenRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnalystTokenUpdateCheck implements AnalystUpdateCheck{

    private static final Logger LOGGER = LogManager.getLogger(AnalystTokenUpdateCheck.class);

    private InviteTokenRepository tokenRepository;

    public AnalystTokenUpdateCheck(InviteTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void updateCheck(Analyst oldAnalystData, UpdateAnalystDTO newAnalystData) {

        if(newAnalystData != null
                && newAnalystData.getToken() != null
                && !newAnalystData.getToken().isBlank()
                && tokenRepository.findByToken(newAnalystData.getToken()).isPresent()
                && !tokenRepository.findByToken(newAnalystData.getToken()).get().getUsed()){

            String oldToken = oldAnalystData.getToken().getToken();
            oldAnalystData.setToken(tokenRepository.findByToken(newAnalystData.getToken()).get());
            LOGGER.info("Analyst -{} , token was updated from - {} to - {}",oldAnalystData.getLogin(),oldToken,newAnalystData.getToken());

        }

    }

}