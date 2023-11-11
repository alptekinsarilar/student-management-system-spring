package com.alptekin.student.repository;

import com.alptekin.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByFirstNameAndLastName(String firstName, String lastName);

    Optional<Student> findByEmail(String email);

    boolean existsStudentByEmail(String email);
}
