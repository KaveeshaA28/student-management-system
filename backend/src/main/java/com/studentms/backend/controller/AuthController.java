package com.studentms.backend.controller;

import com.studentms.backend.config.JwtUtil;
import com.studentms.backend.model.Admin;
import com.studentms.backend.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<Admin> adminOpt = adminRepository.findByEmail(loginRequest.getEmail());

        if (adminOpt.isEmpty()) {
            return new ResponseEntity<>(errorResponse("Invalid email or password"), HttpStatus.UNAUTHORIZED);
        }

        Admin admin = adminOpt.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword())) {
            return new ResponseEntity<>(errorResponse("Invalid email or password"), HttpStatus.UNAUTHORIZED);
        }

        String token = jwtUtil.generateToken(admin.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("email", admin.getEmail());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private Map<String, String> errorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }

    // Inner class for request body
    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}