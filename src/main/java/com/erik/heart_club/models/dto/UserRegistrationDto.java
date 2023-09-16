package com.erik.heart_club.models.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Date;

@Data
public class UserRegistrationDto {

    private long id;
    private String email;
    private String password;

    @Size(min=2, max=40)
    private String firstName;

    @Size(min=2, max=40)
    private String lastName;

    private String middleName;
    private Date dateOfBirth;
    private String sex;
}
