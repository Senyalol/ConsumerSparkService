package com.bankSpark.analyticsService.security.analystService.updateChecks;

import com.bankSpark.analyticsService.DTO.analyst.UpdateAnalystDTO;
import com.bankSpark.analyticsService.ORM.analyst.Analyst;
import com.bankSpark.analyticsService.repository.AnalystRepository;

public class AnalystLoginUpdateCheck implements AnalystUpdateCheck{

    private AnalystRepository analystRepository;

    public AnalystLoginUpdateCheck(AnalystRepository analystRepository) {
        this.analystRepository = analystRepository;
    }

    @Override
    public void updateCheck(Analyst oldAnalystData, UpdateAnalystDTO newAnalystData) {

        if(newAnalystData != null
                && newAnalystData.getLogin() != null
                && !newAnalystData.getLogin().isBlank()
                && analystRepository.getAnalystByLogin(newAnalystData.getLogin()).isEmpty()) {

            oldAnalystData.setLogin(newAnalystData.getLogin());

        }

    }

}