package com.ktb.community_BE.service;


import com.ktb.community_BE.dto.PolicySection;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

@Service
public class PolicyService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<PolicySection> getTermsSections() throws IOException {
        ClassPathResource resource = new ClassPathResource("/policy/terms.json");
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, new TypeReference<>() {});
        }
    }

    public List<PolicySection> getPrivacySections() throws IOException {
        ClassPathResource resource = new ClassPathResource("/policy/privacy.json");
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, new TypeReference<>() {});
        }
    }
}