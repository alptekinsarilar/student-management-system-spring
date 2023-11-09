package com.alptekin.student.service;

import com.alptekin.student.exception.StudentAlreadyExistsException;
import com.alptekin.student.exception.StudentNotFoundException;
import com.alptekin.student.model.Student;
import com.alptekin.student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        else if(studentRepository.existsById(student.getId())) {
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
    public Student updateStudent(Student student) {
        if (student == null || !studentRepository.existsById(student.getId())) {
            throw new StudentAlreadyExistsException("Unable to find student to update with id: " + student.getId());
        }
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException("Unable to find student to delete with id: " + id);
        }
        studentRepository.deleteById(id);
    }
}
