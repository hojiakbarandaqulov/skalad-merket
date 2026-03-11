package org.example.service;

import org.example.entity.Users;

public interface LoginAttemptService {
    void handleFailedAttempt(Users user);
}
