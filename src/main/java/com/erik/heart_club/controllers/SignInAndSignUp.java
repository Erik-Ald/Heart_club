package com.erik.heart_club.controllers;

import com.erik.heart_club.exceptions.UserAlreadyExistException;
import com.erik.heart_club.listeners.events.OnRegistrationCompleteEvent;
import com.erik.heart_club.models.User;
import com.erik.heart_club.models.VerificationToken;
import com.erik.heart_club.models.dto.UserRegistrationDto;
import com.erik.heart_club.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Calendar;
import java.util.Locale;

@Controller
@RequiredArgsConstructor
public class SignInAndSignUp {

    private final UserService userService;
    private final MessageSource messages;

    @GetMapping("/")
    public String getMainPage() {
        return "index";
    }

    @GetMapping("/sign_in")
    public String getSignInPage(@RequestParam(value = "error", defaultValue = "false") Boolean error, Model model) {
        return "sign_in_page";
    }

    @GetMapping("/sign_up")
    public String getSignUpPage(Authentication authentication, Model model) {
        if (authentication != null) {
            return "redirect:/";
        }
        model.addAttribute("user", new User());
        return "sign_up_page";
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {
        Locale locale = request.getLocale();

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            String message = messages.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", message);
            return "redirect:/badUser.html";
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            String messageValue = messages.getMessage("auth.message.expired", null, locale);
            model.addAttribute("message", messageValue);
            return "redirect:/badUser.html";
        }

        user.setState(User.State.CONFIRMED);
        userService.saveRegisteredUser(user);
        return "redirect:/login.html";
    }
}
