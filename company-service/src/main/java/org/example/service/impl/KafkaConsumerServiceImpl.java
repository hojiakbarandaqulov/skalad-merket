package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Company;
import org.example.repository.CompanyRepository;
import org.example.service.KafkaConsumerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerServiceImpl implements KafkaConsumerService {
    private final CompanyRepository companyRepository;
    @KafkaListener(
            topics = "send.company.name",
            groupId = "company-service-group"
    )
    @Override
    public void sendCompanyName(String companyName) {
        Company company = new Company();
        company.setName(companyName);
        log.info("Sending company name {}", companyName);
        companyRepository.save(company);
    }
}
