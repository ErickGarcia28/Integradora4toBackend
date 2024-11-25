package com.example.EventosP1.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordHasher {
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "123456789";
        String hashedPassword = passwordEncoder.encode(rawPassword);

        System.out.println("Password encriptado: " + hashedPassword);
    }
}
