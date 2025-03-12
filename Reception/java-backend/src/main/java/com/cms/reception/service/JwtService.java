package com.cms.reception.service;

import com.cms.reception.property.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final JwtProperties jwtProperties;

    @Autowired
    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public void printJwtConfig() {
        System.out.println("JWT Secret: " + jwtProperties.getSecret());
        System.out.println("JWT Expiration: " + jwtProperties.getExpiration());
    }
}