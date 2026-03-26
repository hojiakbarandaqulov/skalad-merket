package org.example.service.impl;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatRateLimitService {

    private static final Duration WINDOW = Duration.ofSeconds(30);
    private static final int LIMIT = 20;

    private final Map<Long, Deque<Instant>> messageTimeline = new ConcurrentHashMap<>();

    public boolean allowMessage(Long userId) {
        Instant now = Instant.now();
        Deque<Instant> deque = messageTimeline.computeIfAbsent(userId, ignored -> new ArrayDeque<>());

        synchronized (deque) {
            while (!deque.isEmpty() && Duration.between(deque.peekFirst(), now).compareTo(WINDOW) > 0) {
                deque.pollFirst();
            }

            if (deque.size() >= LIMIT) {
                return false;
            }

            deque.addLast(now);
            return true;
        }
    }
}
