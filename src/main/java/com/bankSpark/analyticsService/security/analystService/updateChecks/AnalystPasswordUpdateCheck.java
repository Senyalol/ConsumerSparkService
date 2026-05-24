package com.bankSpark.analyticsService.security.analystService.updateChecks;

import com.bankSpark.analyticsService.DTO.analyst.UpdateAnalystDTO;
import com.bankSpark.analyticsService.ORM.analyst.Analyst;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AnalystPasswordUpdateCheck implements AnalystUpdateCheck{

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

        }

    }

}