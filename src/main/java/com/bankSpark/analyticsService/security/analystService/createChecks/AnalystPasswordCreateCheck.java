package com.bankSpark.analyticsService.security.analystService.createChecks;

import com.bankSpark.analyticsService.DTO.analyst.CreateAnalystDTO;

public class AnalystPasswordCreateCheck implements AnalystCreateCheck{

    @Override
    public boolean createCheck(CreateAnalystDTO analyst) {

        boolean result = true;

        if(analyst == null || analyst.getPassword() == null || analyst.getPassword().isEmpty()){
            result = false;
        }

        return result;
    }

}