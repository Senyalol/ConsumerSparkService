package com.bankSpark.analyticsService.facade.users;

import com.bankSpark.analyticsService.DTO.UserDTO;
import com.bankSpark.analyticsService.mapper.UserMapper;
import com.bankSpark.analyticsService.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserFacadeImpl(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userMapper.toListDTO(userService.getAllUsers());
    }

    @Override
    public UserDTO getUserById(int id) {
        return userMapper.toDTO(userService.getUserById(id));
    }

    @Override
    public List<UserDTO> getUsersByLastName(String lastName) {
        return userMapper.toListDTO(userService.getUsersByLastName(lastName));
    }

    @Override
    public List<UserDTO> getUsersByFirstName(String firstName) {
        return userMapper.toListDTO(userService.getUsersByFirstName(firstName));
    }

}