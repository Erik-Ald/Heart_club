package com.erik.heart_club.controllers.API;

import com.erik.heart_club.exceptions.UserAlreadyExistException;
import com.erik.heart_club.listeners.events.OnRegistrationCompleteEvent;
import com.erik.heart_club.models.User;
import com.erik.heart_club.models.dto.UserRegistrationDto;
import com.erik.heart_club.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class userRestController {

    private final UserService userService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUser(@RequestBody @Valid UserRegistrationDto user, BindingResult bindingResult, HttpServletRequest request, Model model) {

        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getAllErrors().toString();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        try {
            User createdUser = userService.createNewUser(user);
            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(createdUser, appUrl));
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (UserAlreadyExistException uaeEx) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("user_already_exist");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
        }
    }
}
