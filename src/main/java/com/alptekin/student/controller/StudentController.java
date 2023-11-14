package com.alptekin.student.controller;

import com.alptekin.student.dto.StudentDTO;
import com.alptekin.student.dto.StudentIdDTO;
import com.alptekin.student.dto.StudentRegistrationRequest;
import com.alptekin.student.model.Student;
import com.alptekin.student.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}")
    public StudentDTO getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @GetMapping("/email/{email}")
    public StudentDTO getStudentByEmail(@PathVariable String email) {
        return studentService.getStudentByEmail(email);
    }

    @GetMapping("/search")
    public List<StudentDTO> getStudentsByFullName(@RequestParam String firstName, @RequestParam String lastName) {
        return studentService.getStudentsByFullName(firstName, lastName);
    }

    @PostMapping
    public ResponseEntity<?> createStudent(
            @Valid @RequestBody StudentRegistrationRequest request) {
        StudentIdDTO createdStudent = studentService.createStudent(request);
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> students = studentService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id,
                                                 @RequestParam(required = false) String email) {
        StudentDTO updatedStudent = studentService.updateStudent(id, email);
        return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
