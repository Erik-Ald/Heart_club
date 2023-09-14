package com.erik.heart_club.listeners;

import com.erik.heart_club.listeners.events.OnRegistrationCompleteEvent;
import com.erik.heart_club.models.User;
import com.erik.heart_club.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {


    private final UserService userService;

    private final MessageSource messages;

    private final JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl
                = event.getAppUrl() + "/registrationConfirm?token=" + token;
        String message = messages.getMessage("message.regSucc", null, Locale.ENGLISH);

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + "http://localhost" + confirmationUrl);
        mailSender.send(email);
    }

}
