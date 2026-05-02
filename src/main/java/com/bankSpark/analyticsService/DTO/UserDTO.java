package com.bankSpark.analyticsService.DTO;

import lombok.Data;

@Data
public class UserDTO {

    private Integer user_id;

    private String firstname;

    private String lastname;

    public UserDTO() {}

    public UserDTO(Integer user_id, String firstname, String lastname) {
        this.user_id = user_id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

}