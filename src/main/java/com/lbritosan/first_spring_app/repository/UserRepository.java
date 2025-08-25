package com.lbritosan.first_spring_app.repository;

import com.lbritosan.first_spring_app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}