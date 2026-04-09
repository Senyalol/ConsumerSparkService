package com.bankSpark.analyticsService.mapper;

import com.bankSpark.analyticsService.DTO.UserDTO;
import com.bankSpark.analyticsService.ORM.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    //Из сущности в DTO
    public UserDTO toDTO(User user) {

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getId());
        userDTO.setFirstname(user.getFirstname());
        userDTO.setLastname(user.getLastname());

        return userDTO;
    }

    //Из DTO в сущность
    public User toEntity(UserDTO userDTO) {

        User user = new User();
        user.setId(userDTO.getUserId());
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());

        return user;
    }

}