package org.example.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.example.enums.AppLanguage;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages/message");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(new Locale("UZ"));
        return messageSource;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(50, 5, TimeUnit.MINUTES))
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .build();
    }
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(String.class, AppLanguage.class, source -> {
            if (source.isBlank()) {
                return AppLanguage.UZ;
            }

            String normalized = source.trim().split(",")[0].trim().split(";")[0].trim();
            normalized = normalized.split("[-_]")[0].toUpperCase(Locale.ROOT);

            if ("RU".equals(normalized)) {
                return AppLanguage.RU;
            }
            if ("EN".equals(normalized)) {
                return AppLanguage.EN;
            }
            return AppLanguage.UZ;
        });
    }
}
