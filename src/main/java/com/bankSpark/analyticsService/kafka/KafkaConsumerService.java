package com.bankSpark.analyticsService.kafka;

import com.bankSpark.analyticsService.ORM.User;
import com.bankSpark.analyticsService.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final UserRepository userRepository;

    @Autowired
    public KafkaConsumerService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

}