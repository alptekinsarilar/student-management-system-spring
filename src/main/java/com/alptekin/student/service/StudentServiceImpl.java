package com.alptekin.student.service;

import com.alptekin.student.dto.*;
import com.alptekin.student.exception.StudentAlreadyExistsException;
import com.alptekin.student.exception.StudentNotFoundException;
import com.alptekin.student.model.Student;
import com.alptekin.student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService{
    private final StudentRepository studentRepository;
    private final StudentDTOMapper studentDTOMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public StudentServiceImpl(
            StudentRepository studentRepository,
            StudentDTOMapper studentDTOMapper,
            PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.studentDTOMapper = studentDTOMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public StudentDTO getStudentById(Long id) {
        return studentRepository.findById(id)
                .map(studentDTOMapper::toStudentDTO)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));
    }

    @Override
    public List<StudentDTO> getStudentsByFullName(String firstName, String lastName) {
        List<Student> students = studentRepository.findByFirstNameAndLastName(firstName, lastName);
        if (students.isEmpty()) {
            throw new StudentNotFoundException("No students found with name: " + firstName + " " + lastName);
        }
        return students
                .stream()
                .map(studentDTOMapper::toStudentDTO)
                .collect(Collectors.toList());
    }


    @Override
    public StudentDTO getStudentByEmail(String email) {
        return studentRepository.findByEmail(email)
                .map(studentDTOMapper::toStudentDTO)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with email: " + email));
    }

    @Override
    public StudentIdDTO createStudent(StudentRegistrationRequest request) {
        if (request == null) {
            throw new StudentNotFoundException("Unable to create student with provided details");
        }
        else if(studentRepository.existsStudentByEmail(request.email())) {
            throw new StudentAlreadyExistsException("Student already exists with provided details");
        }
        Student student = studentDTOMapper.toStudent(request);
        student.setPassword(
                passwordEncoder.encode(student.getPassword())
        );

        Student created = studentRepository.save(student);

        return new StudentIdDTO(created.getId());
    }

    @Override
    public StudentResponse getAllStudents(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Student> students = studentRepository.findAll(pageable);
        List<Student> listOfStudents = students.getContent();

        /*
         Throwing an exception when no students are found may not be the best approach.
         It's not really an exceptional circumstance for a query to return no results.
         Especially with pagination where you might request a page beyond the end of the result set.
         */

        List<StudentDTO> content = listOfStudents
                .stream()
                .map(studentDTOMapper::toStudentDTO)
                .collect(Collectors.toList());

        StudentResponse response = new StudentResponse(
                content,
                pageNo,
                pageSize,
                students.getTotalElements(),
                students.getTotalPages(),
                students.isLast()
        );

        return response;
    }

    @Override
    public StudentDTO updateStudent(Long id, String email) {
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
        return studentDTOMapper
                .toStudentDTO(studentRepository.save(existingStudent));
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException("Unable to find student to delete with id: " + id);
        }
        studentRepository.deleteById(id);
    }
}
