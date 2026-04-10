package com.bankSpark.analyticsService.service.users;

import com.bankSpark.analyticsService.ORM.User;
import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(int id);

    List<User> getUsersByLastName(String lastName);

    List<User> getUsersByFirstName(String firstName);

}