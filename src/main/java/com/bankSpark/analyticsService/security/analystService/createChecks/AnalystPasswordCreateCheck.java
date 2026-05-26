package com.bankSpark.analyticsService.security.analystService.createChecks;

import com.bankSpark.analyticsService.DTO.analyst.CreateAnalystDTO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnalystPasswordCreateCheck implements AnalystCreateCheck{

    private static final Logger LOGGER = LogManager.getLogger(AnalystPasswordCreateCheck.class);

    @Override
    public boolean createCheck(CreateAnalystDTO analyst) {

        boolean result = true;

        if(analyst == null || analyst.getPassword() == null || analyst.getPassword().isEmpty()){
            result = false;
            LOGGER.error("analyst password is null or empty");
        }

        return result;
    }

}