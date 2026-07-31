package com.studentms.backend.config;

import com.studentms.backend.model.Admin;
import com.studentms.backend.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String adminEmail = "[email protected]";
        String adminPassword = "admin123";

        if (adminRepository.findByEmail(adminEmail).isEmpty()) {
            Admin admin = new Admin();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            adminRepository.save(admin);
            System.out.println("✅ Default admin created: " + adminEmail + " / " + adminPassword);
        } else {
            System.out.println("ℹ️ Admin already exists: " + adminEmail);
        }
    }
}