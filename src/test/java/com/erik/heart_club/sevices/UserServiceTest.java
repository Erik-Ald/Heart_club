package com.erik.heart_club.sevices;

import com.erik.heart_club.exceptions.UserAlreadyExistException;
import com.erik.heart_club.models.User;
import com.erik.heart_club.repositories.UsersRepository;
import com.erik.heart_club.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    UsersRepository usersRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @Test
    public void givenNewUser_whenRegistered_thenCorrect() throws UserAlreadyExistException {
        final String userEmail = UUID.randomUUID().toString();
        final String userPassword =  UUID.randomUUID().toString();
        final User user = User.builder()
                .email(userEmail)
                .password(userPassword)
                .build();

        final User created = userService.createNewUser(user);

        assertNotNull(created);
        assertNotNull(created.getEmail());
        assertEquals(userEmail,created.getEmail());
        assertEquals(passwordEncoder.encode(userPassword),created.getPassword());
        assertNotNull(created.getId());
    }
}
