package com.bankSpark.analyticsService.security.analystService.updateChecks;

import com.bankSpark.analyticsService.DTO.analyst.UpdateAnalystDTO;
import com.bankSpark.analyticsService.ORM.analyst.Analyst;
import com.bankSpark.analyticsService.repository.InviteTokenRepository;

public class AnalystTokenUpdateCheck implements AnalystUpdateCheck{

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

            oldAnalystData.setToken(tokenRepository.findByToken(newAnalystData.getToken()).get());

        }

    }

}