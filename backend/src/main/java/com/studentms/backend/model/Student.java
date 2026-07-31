package com.studentms.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    private String id;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String course;

    private int age;

    private String gender;

    private String address;

    private LocalDate registrationDate;
}