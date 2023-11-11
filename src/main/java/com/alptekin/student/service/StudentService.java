package com.alptekin.student.service;

import com.alptekin.student.model.Student;

import java.util.List;

public interface StudentService {
    Student getStudentById(Long id);
    List<Student> getStudentsByFullName(String firstName, String lastName);
    Student getStudentByEmail(String email);

    // Additional methods for full CRUD operations
    Student createStudent(Student student); // Create a new student
    List<Student> getAllStudents(); // Retrieve all students
    Student updateStudent(Long id, String email); // Update an existing student
    void deleteStudent(Long id); // Delete a student by their id
}
