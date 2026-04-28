package com.bankSpark.analyticsService.mapper;

import com.bankSpark.analyticsService.DTO.UserDTO;
import com.bankSpark.analyticsService.ORM.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    //В лист DTO
    public List<UserDTO> toListDTO(List<User> users) {
        return users.stream()
                .map(x -> this.toDTO(x))
                .collect(Collectors.toList());
    }

}