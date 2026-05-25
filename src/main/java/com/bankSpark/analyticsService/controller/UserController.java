package com.bankSpark.analyticsService.controller;

import com.bankSpark.analyticsService.DTO.UserDTO;
import com.bankSpark.analyticsService.http.HttpResponseController;
import com.bankSpark.analyticsService.facade.users.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000","http://localhost:5174"})
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserFacade userFacade;

    @Autowired
    public UserController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('ANALYST')")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return HttpResponseController.build(userFacade.getAllUsers());
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('ANALYST')")
    @GetMapping("/id")
    public ResponseEntity<UserDTO> getUserById(@RequestParam int id) {
        return HttpResponseController.buildWithId(userFacade.getUserById(id),id);
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('ANALYST')")
    @GetMapping("/name")
    public ResponseEntity<List<UserDTO>> getUserByName(@RequestParam String name) {
        return HttpResponseController.buildWithStringValue(userFacade.getUsersByFirstName(name),name);
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('ANALYST')")
    @GetMapping("/lastname")
    public ResponseEntity<List<UserDTO>> getUserByLastName(@RequestParam String lastName) {
        return HttpResponseController.buildWithStringValue(userFacade.getUsersByLastName(lastName),lastName);
    }

}