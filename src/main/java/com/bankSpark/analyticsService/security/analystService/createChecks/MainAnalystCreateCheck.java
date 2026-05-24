package com.bankSpark.analyticsService.security.analystService.createChecks;

import com.bankSpark.analyticsService.DTO.analyst.CreateAnalystDTO;
import com.bankSpark.analyticsService.ORM.analyst.Analyst;

import java.util.List;

public class MainAnalystCreateCheck {

    private List<AnalystCreateCheck> analystCreateChecks;

    public MainAnalystCreateCheck(List<AnalystCreateCheck> createChecks) {
        this.analystCreateChecks = createChecks;
    }

    public boolean allCreateChecks(CreateAnalystDTO analyst) {

        boolean result = true;

        for(AnalystCreateCheck check : analystCreateChecks) {

            if(!check.createCheck(analyst)) {
                result = false;
            }

        }

        return result;
    }

}