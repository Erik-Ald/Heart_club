package com.erik.heart_club.repositories;

import com.erik.heart_club.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User,Long> {

    Optional<User> findUserByEmail(String email);

    User getUserByEmail(String email);
}
