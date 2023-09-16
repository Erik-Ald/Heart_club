package com.erik.heart_club.security.config;

import com.erik.heart_club.security.details.CustomOAuth2UserDetails;
import com.erik.heart_club.security.details.CustomOAuth2UserDetailsService;
import com.erik.heart_club.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService customUserDetailsService;
    @Autowired
    private CustomOAuth2UserDetailsService customOAuth2UserDetailsService;

    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   PersistentTokenRepository tokenRepository) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable());

        httpSecurity.formLogin(form -> form
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .passwordParameter("password")
                .failureUrl("/sign_in?error=true")
                .loginPage("/sign_in")
        );

        httpSecurity.oauth2Login(login -> login
                .loginPage("/sign_in")
                .userInfoEndpoint(info -> info
                    .userService(customOAuth2UserDetailsService)
                )
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                        Authentication authentication)
                            throws IOException, ServletException {

                        CustomOAuth2UserDetails oauthUser = (CustomOAuth2UserDetails) authentication.getPrincipal();

                        userService.processOAuthPostLogin(oauthUser.getEmail());

                        response.sendRedirect("/");
                    }
                })
        );

        httpSecurity.authorizeHttpRequests(authorize->authorize
                .requestMatchers("/sign_in","/css/**","/img/**","/login/**","/oauth/**",
                        "/sign_up","/registrationConfirm","/js/**","/api/user/add").permitAll()
                .anyRequest().authenticated()
        );

        httpSecurity.rememberMe(rememberMe -> rememberMe
                .rememberMeParameter("remember-me")
                .tokenRepository(tokenRepository)
                .tokenValiditySeconds(60 * 60)
        );

        return httpSecurity.build();
    }

    @Autowired
    public void bindUserDetailsService(AuthenticationManagerBuilder builder,
                                       PasswordEncoder passwordEncoder) throws Exception {
        builder.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    public PersistentTokenRepository tokenRepository(DataSource dataSource) {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }
}
