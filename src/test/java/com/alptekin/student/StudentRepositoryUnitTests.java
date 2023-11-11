package com.alptekin.student;

import com.alptekin.student.model.Student;
import com.alptekin.student.repository.StudentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class StudentRepositoryUnitTests {

    @Autowired
    private StudentRepository underTest;

    @Test
    public void StudentRepository_Save_ReturnSavedStudent() {
        //Arrange
        Student student = new Student(
                "John", 
                "Doe",
                "johndoe@example.com",
                "pass1");
        //Act
        Student savedStudent = underTest.save(student);
        
        //Assert
        Assertions.assertNotNull(savedStudent);
        Assertions.assertNotEquals(savedStudent.getId(), 0);
        Assertions.assertEquals(savedStudent.getEmail(), "johndoe@example.com");
    }

    @Test
    public void StudentRepository_FindAll_ReturnMoreThanOneStudent() {
        //Arrange
        Student s1 = new Student(
                "John",
                "Doe",
                "johndoe@example.com",
                "pass1");

        Student s2= new Student(
                "Halil",
                "Erkan",
                "halilerkan@example.com",
                "pass2");
        underTest.save(s1);
        underTest.save(s2);

        //Act
        List<Student> studentList = underTest.findAll();

        //Assert
        Assertions.assertNotNull(studentList);
        Assertions.assertEquals(studentList.size(), 2);
    }

    @Test
    public void StudentRepository_FindById_ReturnStudent() {
        //Arrange
        Student s1 = new Student(
                "John",
                "Doe",
                "johndoe@example.com",
                "pass1");
        underTest.save(s1);

        //Act
        Student savedStudent = underTest.findById(s1.getId()).get();

        //Assert
        Assertions.assertNotNull(savedStudent);
        Assertions.assertEquals(s1.getEmail(), savedStudent.getEmail());
    }

    @Test
    public void StudentRepository_FindStudentByEmail_ReturnStudent() {
        //Arrange
        Student s1 = new Student(
                "John",
                "Doe",
                "johndoe@example.com",
                "pass1");
        underTest.save(s1);

        //Act
        Student savedStudent = underTest.findByEmail(s1.getEmail()).get();

        //Assert
        Assertions.assertNotNull(savedStudent);
        Assertions.assertEquals(s1.getEmail(), savedStudent.getEmail());
    }
    @Test
    public void StudentRepository_FindByFirstNameAndLastName_ReturnOneStudent() {
        //Arrange
        Student s1 = new Student(
                "John",
                "Doe",
                "johndoe@example.com",
                "pass1");
        underTest.save(s1);

        //Act
        List<Student> studentList = underTest.findByFirstNameAndLastName(
                s1.getFirstName(), s1.getLastName());

        //Assert
        Assertions.assertNotNull(studentList);
        Assertions.assertEquals(studentList.size(), 1);
    }

    @Test
    public void StudentRepository_FindByFirstNameAndLastName_ReturnMoreThanOneStudents() {
        //Arrange
        Student s1 = new Student(
                "John",
                "Doe",
                "johndoe@example.com",
                "pass1");
        Student s2= new Student(
                "John",
                "Doe",
                "johndoe2@example.com",
                "pass2");
        underTest.save(s1);
        underTest.save(s2);

        //Act
        List<Student> studentList = underTest.findByFirstNameAndLastName(
                s1.getFirstName(), s1.getLastName());

        //Assert
        Assertions.assertNotNull(studentList);
        Assertions.assertEquals(studentList.size(), 2);
    }

    @Test
    public void StudentRepository_UpdateStudentEmail_ReturnStudentNotNull() {
        //Arrange
        Student s1 = new Student(
                "John",
                "Doe",
                "johndoe@example.com",
                "pass1");
        Student savedStudent = underTest.save(s1);

        //Act
        String newEmail = "halilerkan@example.com";
        savedStudent.setEmail(newEmail);
        Student updatedStudent = underTest.save(savedStudent);

        //Assert
        Assertions.assertNotNull(updatedStudent);
        Assertions.assertEquals(newEmail, savedStudent.getEmail());
    }

    @Test
    public void StudentRepository_DeleteStudent_ReturnStudentIsEmpty() {
        //Arrange
        Student s1 = new Student(
                "John",
                "Doe",
                "johndoe@example.com",
                "pass1");
        underTest.save(s1);

        //Act
        underTest.deleteById(s1.getId());

        Optional<Student> studentReturned = underTest.findById(s1.getId());
        //Assert
        Assertions.assertTrue(studentReturned.isEmpty());
    }

    @Test
    public void StudentRepository_CheckExistsStudentByEmail_ReturnStudentExists() {
        //Arrange
        Student s1 = new Student(
                "John",
                "Doe",
                "johndoe@example.com",
                "pass1");
        underTest.save(s1);

        //Act
        boolean expected = underTest.existsStudentByEmail(s1.getEmail());

        //Assert
        Assertions.assertTrue(expected);
    }

    @Test
    public void StudentRepository_CheckExistsStudentByEmail_ReturnStudentDoesNotExists() {
        //Arrange
        Student s1 = new Student(
                "John",
                "Doe",
                "johndoe@example.com",
                "pass1");
        underTest.save(s1);

        //Act
        boolean expected = underTest.existsStudentByEmail("another@example.com");

        //Assert
        Assertions.assertFalse(expected);
    }
}
