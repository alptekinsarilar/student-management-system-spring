package com.alptekin.student.service;

import com.alptekin.student.model.Student;

public interface StudentService {
    Student getStudentById(Long id);
    Student getStudentByFullName(String firstName, String lastName);
    Student getStudentByEmail(String email);

}
