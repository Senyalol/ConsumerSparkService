package com.bankSpark.analyticsService.repository;

import com.bankSpark.analyticsService.ORM.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findById(int id);
    List<User> findByLastname(String lastName);
    List<User> findByFirstname(String firstName);

}