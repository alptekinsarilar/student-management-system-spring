package com.alptekin.student.service;

import com.alptekin.student.exception.StudentAlreadyExistsException;
import com.alptekin.student.exception.StudentNotFoundException;
import com.alptekin.student.model.Student;
import com.alptekin.student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService{
    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));
    }

    @Override
    public List<Student> getStudentsByFullName(String firstName, String lastName) {
        List<Student> students = studentRepository.findByFirstNameAndLastName(firstName, lastName);
        if (students.isEmpty()) {
            throw new StudentNotFoundException("No students found with name: " + firstName + " " + lastName);
        }
        return students;
    }


    @Override
    public Student getStudentByEmail(String email) {
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with email: " + email));
    }

    @Override
    public Student createStudent(Student student) {
        if (student == null) {
            throw new StudentNotFoundException("Unable to create student with provided details");
        }
        else if(studentRepository.existsStudentByEmail(student.getEmail())) {
            throw new StudentAlreadyExistsException("Student already exists with provided details");
        }
        return studentRepository.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        if (students.isEmpty()) {
            throw new StudentNotFoundException("No students found");
        }
        return students;
    }

    @Override
    public Student updateStudent(Long id, String email) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Unable to find student to update with id: " + id));

        if(email != null &&
            !email.isEmpty() &&
                !Objects.equals(existingStudent.getEmail(), email)) {
           Optional<Student> studentOptional = studentRepository.
                   findByEmail(email);
           if (studentOptional.isPresent()) {
               throw new IllegalStateException("Email is taken");
           }
           existingStudent.setEmail(email);
        }
        return studentRepository.save(existingStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException("Unable to find student to delete with id: " + id);
        }
        studentRepository.deleteById(id);
    }
}
