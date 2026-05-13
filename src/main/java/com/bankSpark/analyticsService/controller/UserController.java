package com.bankSpark.analyticsService.controller;

import com.bankSpark.analyticsService.DTO.UserDTO;
import com.bankSpark.analyticsService.http.HttpResponseController;
import com.bankSpark.analyticsService.facade.users.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return HttpResponseController.build(userFacade.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
        return HttpResponseController.buildWithId(userFacade.getUserById(id),id);
    }

    @GetMapping("/name")
    public ResponseEntity<List<UserDTO>> getUserByName(@RequestParam String name) {
        return HttpResponseController.buildWithStringValue(userFacade.getUsersByFirstName(name),name);
    }

    @GetMapping("/lastname")
    public ResponseEntity<List<UserDTO>> getUserByLastName(@RequestParam String lastName) {
        return HttpResponseController.buildWithStringValue(userFacade.getUsersByLastName(lastName),lastName);
    }

}