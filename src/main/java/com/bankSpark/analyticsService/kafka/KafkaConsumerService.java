package com.bankSpark.analyticsService.kafka;

import com.bankSpark.analyticsService.ORM.Anomaly;
import com.bankSpark.analyticsService.ORM.SegmentUser;
import com.bankSpark.analyticsService.ORM.User;
import com.bankSpark.analyticsService.mapper.UserMapper;
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

    @Autowired
    public KafkaConsumerService(UserRepository userRepository,
                                SegmentURepository segmentURepository,
                                AnomalyRepository anomalyRepository) {

        this.userRepository = userRepository;
        this.segmentURepository = segmentURepository;
        this.anomalyRepository = anomalyRepository;

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
    public void listenSasKafka(SegmentUser recievedSegmentU) throws JsonProcessingException {
        System.out.println(recievedSegmentU);

        try {

           // if(segmentURepository.findByUserId(recievedSegmentU.getUser().getId()).isEmpty()){
                segmentURepository.save(recievedSegmentU);
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
    public void listenAnomalyKafka(Anomaly recievedAnomaly) throws JsonProcessingException {
        System.out.println(recievedAnomaly);

        try{
            anomalyRepository.save(recievedAnomaly);
        }
        catch (Exception e){
            System.out.println("Error user data in object" + e.getMessage());
        }

    }

}