package com.bankSpark.analyticsService.mapper;

import com.bankSpark.analyticsService.DTO.anomaly.AnomalyDTO;
import com.bankSpark.analyticsService.DTO.anomaly.KafkaAnomalyDTO;
import com.bankSpark.analyticsService.ORM.Anomaly;
import com.bankSpark.analyticsService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AnomalyMapper {

    private final UserRepository userRepository;

    @Autowired
    public AnomalyMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Из сущности в DTO
    public AnomalyDTO toDTO(Anomaly anomaly) {

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
    public Anomaly toEntity(AnomalyDTO anomalyDTO) {

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

    //Из листа сущностей в лист DTO
    public List<AnomalyDTO> toListDTO(List<Anomaly> anomalyList) {
        return anomalyList.stream()
                .map(x -> this.toDTO(x)).collect(Collectors.toList());
    }

    //Маппинг DTO в сущность из Kafka
    public Anomaly fromKafkaDTOtoEntity(KafkaAnomalyDTO anomalyDTO) {

        Anomaly anomaly = new Anomaly();
        anomaly.setUser(userRepository.findById(anomalyDTO.getUser_id()).get());
        anomaly.setEventTime(anomalyDTO.getEvent_time());
        anomaly.setType(anomalyDTO.getType());
        anomaly.setSum(anomalyDTO.getSum());
        anomaly.setAvgCheck(anomalyDTO.getAvg_check_5min());
        anomaly.setMessage(anomalyDTO.getMessage());

        return anomaly;

    }

}