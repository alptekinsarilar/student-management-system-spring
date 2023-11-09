package com.alptekin.student.service;

import com.alptekin.student.model.Student;

import java.util.List;

public interface StudentService {
    Student getStudentById(Long id);
    List<Student> getStudentsByFullName(String firstName, String lastName);
    Student getStudentByEmail(String email);

}
