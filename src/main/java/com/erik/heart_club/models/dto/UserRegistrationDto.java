package com.erik.heart_club.models.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class UserRegistrationDto {

    private long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String middleName;
    private Date dateOfBirth;
    private String sex;
}
