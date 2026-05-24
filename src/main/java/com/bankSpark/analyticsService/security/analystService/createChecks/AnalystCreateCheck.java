package com.bankSpark.analyticsService.security.analystService.createChecks;

import com.bankSpark.analyticsService.DTO.analyst.CreateAnalystDTO;

public interface AnalystCreateCheck {

    boolean createCheck(CreateAnalystDTO analyst);

}