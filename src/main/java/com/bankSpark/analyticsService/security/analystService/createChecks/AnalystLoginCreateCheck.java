package com.bankSpark.analyticsService.security.analystService.createChecks;

import com.bankSpark.analyticsService.DTO.analyst.CreateAnalystDTO;
import com.bankSpark.analyticsService.repository.AnalystRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnalystLoginCreateCheck implements AnalystCreateCheck{

    private static final Logger LOGGER = LogManager.getLogger(AnalystLoginCreateCheck.class);
    private AnalystRepository analystRepository;

    public AnalystLoginCreateCheck(AnalystRepository analystRepository) {
        this.analystRepository = analystRepository;
    }

    @Override
    public boolean createCheck(CreateAnalystDTO analyst) {

        boolean result = true;

        if(analyst == null || analyst.getLogin() == null
                || analyst.getLogin().isEmpty()
                || analystRepository.getAnalystByLogin(analyst.getLogin()).isPresent()){

            result = false;
            LOGGER.error("Invalid login for new analyst - {}",analyst.getLogin());
        }

        return result;
    }

}