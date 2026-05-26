package com.bankSpark.analyticsService.security.analystService.updateChecks;

import com.bankSpark.analyticsService.DTO.analyst.UpdateAnalystDTO;
import com.bankSpark.analyticsService.ORM.analyst.Analyst;
import com.bankSpark.analyticsService.repository.AnalystRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnalystLoginUpdateCheck implements AnalystUpdateCheck{

    private static final Logger LOGGER = LogManager.getLogger(AnalystLoginUpdateCheck.class);
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

            String oldLogin = oldAnalystData.getLogin();
            oldAnalystData.setLogin(newAnalystData.getLogin());
            LOGGER.info("Analyst login was updated from - {} to - {}",oldLogin,newAnalystData.getLogin());
        }

    }

}