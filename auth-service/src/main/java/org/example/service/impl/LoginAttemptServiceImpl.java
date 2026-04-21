package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.Users;
import org.example.exp.AppBadException;
import org.example.repository.UserRepository;
import org.example.service.LoginAttemptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoginAttemptServiceImpl implements LoginAttemptService {
    private final UserRepository userRepository;

    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            noRollbackFor = AppBadException.class
    )
    public void handleFailedAttempt(Users user) {
        Users freshUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new AppBadException("user.not.found"));
        int failedCount=0;
        try {
             failedCount = freshUser.getFailedLoginCount() + 1;
        }catch (NullPointerException exception){
          exception.printStackTrace();
        }
        freshUser.setFailedLoginCount(failedCount);

        if (failedCount >= 5) {
            freshUser.setLockedUntil(LocalDateTime.now().plusMinutes(15));
            freshUser.setFailedLoginCount(0);
        }
        userRepository.save(freshUser);
    }
}
