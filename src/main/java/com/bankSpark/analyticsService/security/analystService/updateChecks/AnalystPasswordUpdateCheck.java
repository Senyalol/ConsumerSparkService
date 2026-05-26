package com.bankSpark.analyticsService.security.analystService.updateChecks;

import com.bankSpark.analyticsService.DTO.analyst.UpdateAnalystDTO;
import com.bankSpark.analyticsService.ORM.analyst.Analyst;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnalystPasswordUpdateCheck implements AnalystUpdateCheck{

    private static final Logger LOGGER = LogManager.getLogger(AnalystPasswordUpdateCheck.class);

    private PasswordEncoder passwordEncoder;

    public AnalystPasswordUpdateCheck(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void updateCheck(Analyst oldAnalystData, UpdateAnalystDTO newAnalystData) {

        if(newAnalystData != null
                && newAnalystData.getPassword() != null
                && !newAnalystData.getPassword().isBlank()){

            oldAnalystData.setPassword(passwordEncoder.encode(newAnalystData.getPassword()));
            LOGGER.info("Analyst password was updated for user - {} ",newAnalystData.getLogin());
        }

    }

}