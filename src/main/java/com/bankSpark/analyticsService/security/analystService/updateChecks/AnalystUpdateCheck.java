package com.bankSpark.analyticsService.security.analystService.updateChecks;

import com.bankSpark.analyticsService.DTO.analyst.UpdateAnalystDTO;
import com.bankSpark.analyticsService.ORM.analyst.Analyst;

public interface AnalystUpdateCheck {

    void updateCheck(Analyst oldAnalystData, UpdateAnalystDTO newAnalystData);

}