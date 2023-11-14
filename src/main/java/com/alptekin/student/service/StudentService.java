package com.alptekin.student.service;

import com.alptekin.student.dto.StudentDTO;
import com.alptekin.student.dto.StudentIdDTO;
import com.alptekin.student.dto.StudentRegistrationRequest;

import java.util.List;

public interface StudentService {
    StudentDTO getStudentById(Long id);
    List<StudentDTO> getStudentsByFullName(String firstName, String lastName);
    StudentDTO getStudentByEmail(String email);

    // Additional methods for full CRUD operations
    StudentIdDTO createStudent(StudentRegistrationRequest request); // Create a new student
    List<StudentDTO> getAllStudents(); // Retrieve all students
    StudentDTO updateStudent(Long id, String email); // Update an existing student
    void deleteStudent(Long id); // Delete a student by their id
}
