package com.bankSpark.analyticsService.mapper;

import com.bankSpark.analyticsService.DTO.anomaly.AnomalyDTO;
import com.bankSpark.analyticsService.DTO.anomaly.KafkaAnomalyDTO;
import com.bankSpark.analyticsService.ORM.User;
import com.bankSpark.analyticsService.ORM.anomaly.Anomaly;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AnomalyMapper {

//    private final UserRepository userRepository;
//
//    @Autowired
//    public AnomalyMapper(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    //Из сущности в DTO
    public AnomalyDTO toDTO(Anomaly anomaly) {

        AnomalyDTO dto = new AnomalyDTO();
        dto.setAnomalyId(anomaly.getId());
        dto.setUserId(anomaly.getUser().getId());

        //dto.setEventTime(anomaly.getEventTime());
        if (anomaly.getEventTime() != null && anomaly.getEventTime() > 0) {
            dto.setEventTime(
                    LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(anomaly.getEventTime()),
                            ZoneId.systemDefault()
                    ).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            );
        }
        dto.setType(anomaly.getType());
        dto.setSum(anomaly.getSum());
        dto.setAvgCheck(anomaly.getAvgCheck());
        dto.setMessage(anomaly.getMessage());

        return dto;
    }

    //Из DTO в сущность
//    public Anomaly toEntity(AnomalyDTO anomalyDTO) {
//
//        Anomaly anomaly = new Anomaly();
//        anomaly.setId(anomalyDTO.getAnomalyId());
//        anomaly.setUser(userRepository.findById(anomalyDTO.getUserId()).get());
//        anomaly.setEventTime(anomalyDTO.getEventTime());
//        anomaly.setType(anomalyDTO.getType());
//        anomaly.setSum(anomalyDTO.getSum());
//        anomaly.setAvgCheck(anomalyDTO.getAvgCheck());
//        anomaly.setMessage(anomalyDTO.getMessage());
//
//        return anomaly;
//    }

    //Из листа сущностей в лист DTO
    public List<AnomalyDTO> toListDTO(List<Anomaly> anomalyList) {
        return anomalyList.stream()
                .map(x -> this.toDTO(x)).collect(Collectors.toList());
    }

    //Маппинг DTO в сущность из Kafka
    public Anomaly fromKafkaDTOtoEntity(KafkaAnomalyDTO anomalyDTO, User user) {

        Anomaly anomaly = new Anomaly();
        anomaly.setUser(user);
        anomaly.setEventTime(anomalyDTO.getEvent_time());
        anomaly.setType(anomalyDTO.getType());
        anomaly.setSum(anomalyDTO.getSum());
        anomaly.setAvgCheck(anomalyDTO.getAvg_check_5min());
        anomaly.setMessage(anomalyDTO.getMessage());

        return anomaly;

    }

}