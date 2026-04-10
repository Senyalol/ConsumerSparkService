package com.bankSpark.analyticsService.mapper;

import com.bankSpark.analyticsService.DTO.AnomalyDTO;
import com.bankSpark.analyticsService.ORM.Anomaly;
import com.bankSpark.analyticsService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnomalyMapper {

    private final UserRepository userRepository;

    @Autowired
    public AnomalyMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Из сущности в DTO
    private AnomalyDTO toDTO(Anomaly anomaly) {

        AnomalyDTO dto = new AnomalyDTO();
        dto.setAnomalyId(anomaly.getId());
        dto.setUserId(anomaly.getUser().getId());
        dto.setEventTime(anomaly.getEventTime());
        dto.setType(anomaly.getType());
        dto.setSum(anomaly.getSum());
        dto.setAvgCheck(anomaly.getAvgCheck());
        dto.setMessage(anomaly.getMessage());

        return dto;
    }

    //Из DTO в сущность
    private Anomaly toEntity(AnomalyDTO anomalyDTO) {

        Anomaly anomaly = new Anomaly();
        anomaly.setId(anomalyDTO.getAnomalyId());
        anomaly.setUser(userRepository.findById(anomalyDTO.getUserId()).get());
        anomaly.setEventTime(anomalyDTO.getEventTime());
        anomaly.setType(anomalyDTO.getType());
        anomaly.setSum(anomalyDTO.getSum());
        anomaly.setAvgCheck(anomalyDTO.getAvgCheck());
        anomaly.setMessage(anomalyDTO.getMessage());

        return anomaly;
    }

}