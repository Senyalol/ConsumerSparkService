package com.bankSpark.analyticsService.security.analystService.updateChecks;

import com.bankSpark.analyticsService.DTO.analyst.UpdateAnalystDTO;
import com.bankSpark.analyticsService.ORM.analyst.Analyst;
import com.bankSpark.analyticsService.security.Roles;

import java.util.Arrays;
import java.util.List;

public class AnalystRoleUpdateCheck implements AnalystUpdateCheck{

    private static final List<Roles> roles = Arrays.stream(Roles.values()).toList();

    @Override
    public void updateCheck(Analyst oldAnalystData, UpdateAnalystDTO newAnalystData) {

        if(newAnalystData != null
                && newAnalystData.getRole() != null
                && !newAnalystData.getRole().isBlank()) {

            if(roles.contains(newAnalystData.getRole())){
                oldAnalystData.setRole(newAnalystData.getRole());
            }

            else{
                throw new RuntimeException("Invalid role update");
            }

        }

    }

}