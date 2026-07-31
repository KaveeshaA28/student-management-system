package com.studentms.backend.controller;

import com.studentms.backend.model.Student;
import com.studentms.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    // 1. Add Student
    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        student.setRegistrationDate(LocalDate.now());
        Student savedStudent = studentRepository.save(student);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }

    // 2. View All Students
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    // 3. View Single Student by ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable String id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 4. Update Student
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable String id, @RequestBody Student updatedStudent) {
        Optional<Student> existingStudentOpt = studentRepository.findById(id);

        if (existingStudentOpt.isPresent()) {
            Student existingStudent = existingStudentOpt.get();
            existingStudent.setFullName(updatedStudent.getFullName());
            existingStudent.setEmail(updatedStudent.getEmail());
            existingStudent.setPhoneNumber(updatedStudent.getPhoneNumber());
            existingStudent.setCourse(updatedStudent.getCourse());
            existingStudent.setAge(updatedStudent.getAge());
            existingStudent.setGender(updatedStudent.getGender());
            existingStudent.setAddress(updatedStudent.getAddress());

            Student saved = studentRepository.save(existingStudent);
            return new ResponseEntity<>(saved, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 5. Delete Student
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 6. Search Students by Name or Email
    @GetMapping("/search")
    public ResponseEntity<List<Student>> searchStudents(@RequestParam String query) {
        List<Student> results = studentRepository
                .findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}