package com.bankSpark.analyticsService.security.analystService.updateChecks;

import com.bankSpark.analyticsService.DTO.analyst.UpdateAnalystDTO;
import com.bankSpark.analyticsService.ORM.analyst.Analyst;

import java.util.List;

public class MainAnalystUpdateCheck {

    private List<AnalystUpdateCheck> analystUpdateChecks;

    public MainAnalystUpdateCheck(List<AnalystUpdateCheck> updateChecks) {
        this.analystUpdateChecks = updateChecks;
    }

    public void updateChecks(Analyst old, UpdateAnalystDTO update) {

        for(AnalystUpdateCheck updateCheck : analystUpdateChecks) {

            updateCheck.updateCheck(old, update);

        }

    }

}