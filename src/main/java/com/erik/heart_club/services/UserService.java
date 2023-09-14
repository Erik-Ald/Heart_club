package com.erik.heart_club.services;

import com.erik.heart_club.exceptions.UserAlreadyExistException;
import com.erik.heart_club.models.User;
import com.erik.heart_club.models.VerificationToken;
import com.erik.heart_club.models.dto.UserRegistrationDto;
import com.erik.heart_club.repositories.VerificationTokenRepository;
import com.erik.heart_club.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;


    public void processOAuthPostLogin(String email) {

        User existUser = usersRepository.getUserByEmail(email);

        if (existUser == null) {
            User newUser = User.builder()
                    .role(User.Role.USER)
                    .email(email)
                    .state(User.State.CONFIRMED)
                    .provider(User.Provider.GOOGLE)
                    .build();

            usersRepository.save(newUser);
        }
    }

    public User createNewUser(UserRegistrationDto user) throws UserAlreadyExistException {

        User existedUser = usersRepository.getUserByEmail(user.getEmail());
        if(existedUser != null && existedUser.getState().equals(User.State.CONFIRMED)) {
            throw new UserAlreadyExistException("An account for that email already exists.");
        }

        User newUser = User.builder()
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .state(User.State.NOT_CONFIRMED)
                .role(User.Role.USER)
                .provider(User.Provider.LOCAL)
                .build();

        usersRepository.save(newUser);
        return newUser;
    }

    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(user,token);

        verificationTokenRepository.save(myToken);
    }

    public void saveRegisteredUser(User user) {
        usersRepository.save(user);
    }

    public VerificationToken getVerificationToken(String token) {
        return verificationTokenRepository.getVerificationTokenByToken(token);
    }
}
