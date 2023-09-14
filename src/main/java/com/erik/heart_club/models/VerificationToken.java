package com.erik.heart_club.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

@Data
@Entity
@Table
@NoArgsConstructor
public class VerificationToken {

    private static final int EXPIRATION = 60 * 3;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String token;

    @OneToOne(targetEntity = User.class,fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column
    @DateTimeFormat(pattern= "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private Timestamp expiryDate;

    private Timestamp calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Timestamp(cal.getTime().getTime());
    }

    public VerificationToken(final User user, final String token) {
        super();

        this.user = user;
        this.token = token;
        this.expiryDate = this.calculateExpiryDate(EXPIRATION);
    }
}
