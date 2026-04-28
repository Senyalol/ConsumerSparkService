package com.bankSpark.analyticsService.controller;

import com.bankSpark.analyticsService.DTO.UserDTO;
import com.bankSpark.analyticsService.facade.users.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserFacade userFacade;

    @Autowired
    public UserController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userFacade.getAllUsers();
    }

    @GetMapping("/id")
    public UserDTO getUserById(@RequestParam int id) {
        return userFacade.getUserById(id);
    }

    @GetMapping("/name")
    public List<UserDTO> getUserByName(@RequestParam String name) {
        return userFacade.getUsersByFirstName(name);
    }

    @GetMapping("/lastname")
    public List<UserDTO> getUserByLastName(@RequestParam String lastName) {
        return userFacade.getUsersByLastName(lastName);
    }

}