package com.bankSpark.analyticsService.service.users;

import com.bankSpark.analyticsService.ORM.User;
import com.bankSpark.analyticsService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id).get();
    }

    @Override
    public List<User> getUsersByLastName(String lastName) {
        return userRepository.findByLastname(lastName);
    }

    @Override
    public List<User> getUsersByFirstName(String firstName) {
        return userRepository.findByFirstname(firstName);
    }

}