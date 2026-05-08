package com.bankSpark.analyticsService.kafka;

import com.bankSpark.analyticsService.DTO.anomaly.AnomalyDTO;
import com.bankSpark.analyticsService.DTO.anomaly.KafkaAnomalyDTO;
import com.bankSpark.analyticsService.DTO.segmentsRFM.KafkaSegmentUserDTO;
import com.bankSpark.analyticsService.DTO.segmentsRFM.SegmentUserDTO;
import com.bankSpark.analyticsService.ORM.Anomaly;
import com.bankSpark.analyticsService.ORM.SegmentUser;
import com.bankSpark.analyticsService.ORM.User;
import com.bankSpark.analyticsService.mapper.AnomalyMapper;
import com.bankSpark.analyticsService.mapper.SegmentUMapper;
import com.bankSpark.analyticsService.repository.AnomalyRepository;
import com.bankSpark.analyticsService.repository.SegmentURepository;
import com.bankSpark.analyticsService.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final UserRepository userRepository;
    private final SegmentURepository segmentURepository;
    private final AnomalyRepository anomalyRepository;

    private final SegmentUMapper segmentUMapper;
    private final AnomalyMapper anomalyMapper;

    @Autowired
    public KafkaConsumerService(UserRepository userRepository,
                                SegmentURepository segmentURepository,
                                AnomalyRepository anomalyRepository,
                                SegmentUMapper segmentUMapper,
                                AnomalyMapper anomalyMapper) {

        this.userRepository = userRepository;
        this.segmentURepository = segmentURepository;
        this.anomalyRepository = anomalyRepository;
        this.segmentUMapper = segmentUMapper;
        this.anomalyMapper = anomalyMapper;

    }

    @KafkaListener(topics = "users", containerFactory = "kafkaListenerContainerFactory")
    public void listenUserKafka(User recievedUser) throws JsonProcessingException {

        System.out.println(recievedUser);

        try{
            userRepository.save(recievedUser);
        }
        catch (Exception e){
            System.out.println("Error user data in object" + e.getMessage());
        }

    }

    @KafkaListener(topics = "user-segments", containerFactory = "segmentUserKafkaListenerContainerFactory")
    public void listenSegmentsUsersKafka(KafkaSegmentUserDTO recievedSegmentU) throws JsonProcessingException {
        System.out.println(recievedSegmentU);

        try {

           // if(segmentURepository.findByUserId(recievedSegmentU.getUser().getId()).isEmpty()){
                SegmentUser segmentUser = segmentUMapper.fromKafkaDTOtoEntity(recievedSegmentU);
                segmentURepository.save(segmentUser);
//            }
//
//            else{
//                segmentURepository.
//            }

        }
        catch (Exception e){
            System.out.println("Error user data in object" + e.getMessage());
        }

    }

    @KafkaListener(topics = "alerts", containerFactory = "anomalyKafkaListenerContainerFactory")
    public void listenAnomalyKafka(KafkaAnomalyDTO recievedAnomaly) throws JsonProcessingException {
        System.out.println(recievedAnomaly);

        try{
            Anomaly anomaly = anomalyMapper.fromKafkaDTOtoEntity(recievedAnomaly);
            anomalyRepository.save(anomaly);
        }
        catch (Exception e){
            System.out.println("Error user data in object" + e.getMessage());
        }

    }

}