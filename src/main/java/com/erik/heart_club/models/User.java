package com.erik.heart_club.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_main")
public class User {

    public enum State {
        NOT_CONFIRMED, CONFIRMED, DELETED
    }

    public enum Role {
        ADMIN, USER
    }

    public enum Provider {
        LOCAL,GOOGLE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String middleName;

    @Column
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date dateOfBirth;

    @Column
    private String sex;

    @Enumerated(value = EnumType.STRING)
    Role role;

    @Enumerated(value = EnumType.STRING)
    State state;

    @Enumerated(value = EnumType.STRING)
    private Provider provider;


}
