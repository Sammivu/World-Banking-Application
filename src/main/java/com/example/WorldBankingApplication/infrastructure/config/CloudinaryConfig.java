package com.example.WorldBankingApplication.infrastructure.config;

import com.cloudinary.Cloudinary;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration

//This is how to upload pictures
public class CloudinaryConfig {

    @Value("${myapp.cloud.name}") // This is to hide sensitive information
    private String cloudName;


    @Value("${myapp.api.key}")
    private String apiKey;


    @Value("${myapp.api.secret}")
    private String apiSecret;


    @Bean
    public Cloudinary cloudinary(){

        Map<String, String> config = new HashMap<>();

        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);

        return new Cloudinary(config);
    }



}
