package com.bankSpark.analyticsService.security.analystService.updateChecks;

import com.bankSpark.analyticsService.DTO.analyst.UpdateAnalystDTO;
import com.bankSpark.analyticsService.ORM.analyst.Analyst;
import com.bankSpark.analyticsService.security.Roles;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnalystRoleUpdateCheck implements AnalystUpdateCheck{

    private static final Logger LOGGER = LogManager.getLogger(AnalystRoleUpdateCheck.class);

    private static final List<Roles> roles = Arrays.stream(Roles.values()).toList();

    @Override
    public void updateCheck(Analyst oldAnalystData, UpdateAnalystDTO newAnalystData) {

        if(newAnalystData != null
                && newAnalystData.getRole() != null
                && !newAnalystData.getRole().isBlank()) {

            if(roles.contains(newAnalystData.getRole())){

                String oldRole = oldAnalystData.getRole();
                oldAnalystData.setRole(newAnalystData.getRole());
                LOGGER.info("Analyst - {} Role was updated from - {} to - {}",oldAnalystData.getLogin(),oldRole,newAnalystData.getRole());
            }

            else{
                throw new RuntimeException("Invalid role update");
            }

        }

    }

}