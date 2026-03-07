package org.example.service;

import org.example.enums.EmailType;
import org.springframework.stereotype.Service;

@Service
public interface EmailHistoryService {

     void create(String email, String code, EmailType emailType);

     Long getEmailCount(String email);

     void check(String email, String code);
}
