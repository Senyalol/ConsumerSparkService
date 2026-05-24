package com.bankSpark.analyticsService.mapper;

import com.bankSpark.analyticsService.DTO.analyst.AnalystInfoDTO;
import com.bankSpark.analyticsService.ORM.analyst.Analyst;
import org.springframework.stereotype.Component;

@Component
public class AnalystMapper {

    //Из сущности в DTO
    public AnalystInfoDTO toFullInfoDTO(Analyst analyst) {

        AnalystInfoDTO analystInfoDTO = new AnalystInfoDTO();
        analystInfoDTO.setToken(analyst.getToken().getToken());
        analystInfoDTO.setLogin(analyst.getLogin());
        analystInfoDTO.setPassword(analyst.getPassword());
        analystInfoDTO.setRole(analyst.getRole());
        analystInfoDTO.setCreatedAt(analyst.getCreatedAt());

        return analystInfoDTO;
    }

}