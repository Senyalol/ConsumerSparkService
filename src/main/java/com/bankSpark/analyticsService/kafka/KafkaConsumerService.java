package com.bankSpark.analyticsService.kafka;

import com.bankSpark.analyticsService.DTO.UserDTO;
import com.bankSpark.analyticsService.DTO.anomaly.KafkaAnomalyDTO;
import com.bankSpark.analyticsService.DTO.segmentsRFM.KafkaSegmentUserDTO;
import com.bankSpark.analyticsService.ORM.anomaly.Anomaly;
import com.bankSpark.analyticsService.ORM.segment.SegmentUser;
import com.bankSpark.analyticsService.ORM.User;
import com.bankSpark.analyticsService.exception.KafkaCustomException;
import com.bankSpark.analyticsService.mapper.AnomalyMapper;
import com.bankSpark.analyticsService.mapper.SegmentUMapper;
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

    private final SegmentUMapper segmentUMapper;
    private final AnomalyMapper anomalyMapper;
    private final UserMapper userMapper;

    @Autowired
    public KafkaConsumerService(UserRepository userRepository,
                                SegmentURepository segmentURepository,
                                AnomalyRepository anomalyRepository,
                                SegmentUMapper segmentUMapper,
                                AnomalyMapper anomalyMapper,
                                UserMapper userMapper) {

        this.userRepository = userRepository;
        this.segmentURepository = segmentURepository;
        this.anomalyRepository = anomalyRepository;
        this.segmentUMapper = segmentUMapper;
        this.anomalyMapper = anomalyMapper;
        this.userMapper = userMapper;

    }

    @KafkaListener(topics = "users", containerFactory = "kafkaListenerContainerFactory")
    public void listenUserKafka(UserDTO recievedUser) throws JsonProcessingException {

        System.out.println(recievedUser);

        try{
            User newReceivedUser = userMapper.toEntity(recievedUser);
            userRepository.save(newReceivedUser);
        }
        catch (Exception e){
            throw new KafkaCustomException("users",recievedUser);
            //System.out.println("Error user data in object" + e.getMessage());
        }

    }

    @KafkaListener(topics = "user-segments", containerFactory = "segmentUserKafkaListenerContainerFactory")
    public void listenSegmentsUsersKafka(KafkaSegmentUserDTO recievedSegmentU) throws JsonProcessingException {
        System.out.println(recievedSegmentU);
    
        try {

            //Максимум 10 значений сгементов за период
           if(segmentURepository.countByUserId(recievedSegmentU.getUser_id()) <= 9) {

               User user = userRepository.findById(recievedSegmentU.getUser_id()).get();
               SegmentUser segmentUser = segmentUMapper.fromKafkaDTOtoEntity(recievedSegmentU,user);
               Double cutM = Math.round(recievedSegmentU.getM() * 100.0) / 100.0;
               segmentUser.setM(cutM);
               segmentURepository.save(segmentUser);

           }

           else if(segmentURepository.countByUserId(recievedSegmentU.getUser_id()) > 9) {

               SegmentUser oldestSegmentUser = segmentURepository.findOldestByUserId(recievedSegmentU.getUser_id()).get();

               User user = userRepository.findById(recievedSegmentU.getUser_id()).get();
               updateSegment(oldestSegmentUser,segmentUMapper.fromKafkaDTOtoEntity(recievedSegmentU,user));
               segmentURepository.save(oldestSegmentUser);

           }

        }
        catch (Exception e){
            //System.out.println("Error user data in object" + e.getMessage());
            throw new KafkaCustomException("user-segments",recievedSegmentU);
        }

    }

    @KafkaListener(topics = "alerts", containerFactory = "anomalyKafkaListenerContainerFactory")
    public void listenAnomalyKafka(KafkaAnomalyDTO recievedAnomaly) throws JsonProcessingException {
        System.out.println(recievedAnomaly);

        try{
            User user = userRepository.findById(recievedAnomaly.getUser_id()).get();
            Anomaly anomaly = anomalyMapper.fromKafkaDTOtoEntity(recievedAnomaly,user);
            anomalyRepository.save(anomaly);
        }
        catch (Exception e){
            //System.out.println("Error user data in object" + e.getMessage());
            throw new KafkaCustomException("alerts",recievedAnomaly);
        }

    }


    //Обновление существующего сегмента
    private void updateSegment(SegmentUser oldSegment, SegmentUser newSegmentData) {

        oldSegment.setUser(newSegmentData.getUser());
        oldSegment.setSegment(newSegmentData.getSegment());
        oldSegment.setRMinutes(newSegmentData.getRMinutes());
        oldSegment.setF(newSegmentData.getF());
        Double cutM = Math.round(newSegmentData.getM() * 100.0) / 100.0;
        oldSegment.setM(cutM);
        oldSegment.setUpdatedAt(newSegmentData.getUpdatedAt());

    }

}