package com.alptekin.student.dto;

import com.alptekin.student.model.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentDTOMapper {
    public StudentDTO toStudentDTO(Student student) {
        return new StudentDTO(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail()
        );
    }

    public Student toStudent(StudentRegistrationRequest request) {
        return new Student(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.password()
        );
    }
}
