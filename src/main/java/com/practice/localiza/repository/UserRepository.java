package com.practice.localiza.repository;

import com.practice.localiza.auth.UserLogin;

import java.util.Optional;

public interface UserRepository {
    public Optional<UserLogin> findByUsername(String login);

}
