package org.example.service;

import org.example.enums.AppLanguage;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

public interface ResourceBundleService {


     String getMessage(String code, AppLanguage lang);

     String getMessage(String code, String lang);
}
