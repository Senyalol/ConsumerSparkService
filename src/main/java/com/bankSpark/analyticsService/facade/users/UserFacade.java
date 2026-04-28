package com.bankSpark.analyticsService.facade.users;

import com.bankSpark.analyticsService.DTO.UserDTO;

import java.util.List;

public interface UserFacade {

    List<UserDTO> getAllUsers();

    UserDTO getUserById(int id);

    List<UserDTO> getUsersByLastName(String lastName);

    List<UserDTO> getUsersByFirstName(String firstName);

}