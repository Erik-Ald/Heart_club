package com.erik.heart_club.listeners.events;

import com.erik.heart_club.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private String appUrl;
    private Locale locale;
    private User user;

    public OnRegistrationCompleteEvent(User user, String appUrl) {
        super(user);

        this.user = user;
        this.appUrl = appUrl;
    }
}
