package com.alptekin.student.service;

import com.alptekin.student.dto.*;
import com.alptekin.student.exception.StudentNotFoundException;
import com.alptekin.student.model.Student;
import com.alptekin.student.repository.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class StudentServiceUnitTests {
    @Mock
    private StudentRepository studentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    private StudentServiceImpl underTest;
    private final StudentDTOMapper studentDTOMapper = new StudentDTOMapper();

    @BeforeEach
    void setUp() {
        underTest = new StudentServiceImpl(
                studentRepository,
                studentDTOMapper,
                passwordEncoder)
        ;
    }
    @Test
    public void StudentService_CreateStudent_ReturnStudentDto() {
        Student student = demoStudent();
        StudentRegistrationRequest request =
                demoStudentRegistrationRequest();

        when(studentRepository
                .save(Mockito.any(Student.class)))
                .thenReturn(student);

        StudentIdDTO savedStudent = underTest.createStudent(request);


        Assertions.assertThat(savedStudent).isNotNull();
        //This code is verifying that the save() method of studentRepository is called with any instance of Student class.
        verify(studentRepository).save(Mockito.any(Student.class));
    }

    @Test
    public void StudentService_GetAllStudents_ReturnStudentResponse() {
        // Mock a Page<Student> and
        Page<Student> students = Mockito.mock(Page.class);

        // Specify behaviour for the mocks
        when(students.getTotalElements()).thenReturn(0L);

        // Set up the repository to return the mocked Page when findAll() is called
        when(studentRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(students);


        // Call the method under test
        StudentResponse response = underTest.getAllStudents(0, 10);

        // Verify the results
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.pageNo()).isEqualTo(0);
        Assertions.assertThat(response.pageSize()).isEqualTo(10);
        Assertions.assertThat(response.totalElements()).isEqualTo(0);

        // Verify that the repository was called with the correct Pageable
        verify(studentRepository).findAll(ArgumentMatchers.any(Pageable.class));
    }

    @Test
    public void StudentService_GetStudentById_ReturnStudentDTO() {
        Student student = demoStudent();

        when(studentRepository
                .findById(1L))
                .thenReturn(Optional.of(student));

        StudentDTO savedStudent = underTest.getStudentById(1L);


        Assertions.assertThat(savedStudent).isNotNull();
        verify(studentRepository).findById(Mockito.any(Long.class));
    }

    @Test
    public void StudentService_GetStudentByEmail_ReturnStudentDTO() {
        Student student = demoStudent();

        when(studentRepository
                .findByEmail(student.getEmail()))
                .thenReturn(Optional.of(student));

        StudentDTO savedStudent = underTest.getStudentByEmail(student.getEmail());


        Assertions.assertThat(savedStudent).isNotNull();
        Assertions.assertThat(savedStudent.email()).isEqualTo(student.getEmail());
        verify(studentRepository).findByEmail(Mockito.any(String.class));
    }

    @Test
    public void StudentService_GetStudentByFullName_ReturnStudentDTOList() {
        // Given
        List<Student> list = new ArrayList<>();
        list.add(new Student(
                "John",
                "Doe," ,
                "johndoe@mail.com",
                "password123"
        ));
        list.add(new Student(
                "John",
                "Doe," ,
                "johndoe2@example.com",
                "password123"
        ));
        when(studentRepository
                .findByFirstNameAndLastName("John", "Doe"))
                .thenReturn(list);

        // When
        List<StudentDTO> returnedStudent =
                underTest.getStudentsByFullName("John", "Doe");

        // Then
        Assertions.assertThat(returnedStudent).isNotNull();
        Assertions.assertThat(returnedStudent.getFirst().email()).isEqualTo("johndoe@mail.com");
        Assertions.assertThat(returnedStudent.getLast().email()).isEqualTo("johndoe2@example.com");
        verify(studentRepository).findByFirstNameAndLastName(Mockito.any(String.class), Mockito.any(String.class));
    }

    @Test
    public void StudentService_UpdateStudent_ReturnStudentDto() {
        // Given
        String newMail = "newmail@mail.com";
        Student student = demoStudent();
        when(studentRepository
                .findById(student.getId()))
                .thenReturn(Optional.of(student));
        when(studentRepository
                .findByEmail(newMail))
                .thenReturn(Optional.empty());
        when(studentRepository
                .save(Mockito.any(Student.class)))
                .thenReturn(student);

        // When
        StudentDTO savedStudent = underTest.updateStudent(student.getId(), newMail);

        // Then
        Assertions.assertThat(savedStudent).isNotNull();
        Assertions.assertThat(savedStudent.email()).isEqualTo(newMail);
        verify(studentRepository).save(Mockito.any(Student.class));
    }

    @Test
    public void StudentService_UpdateStudent_ThrowStudentNotFoundException() {
        // Given
        Long id = 1L;
        String newEmail = "newmail@mail.com";

        when(studentRepository.findById(id))
                .thenThrow(new StudentNotFoundException("Unable to find student to update with id: " + id));

        // When
        StudentNotFoundException thrown =
                assertThrows(StudentNotFoundException.class, () -> underTest.updateStudent(id, newEmail));

        // Then
        Assertions.assertThat(thrown.getMessage()).isEqualTo("Unable to find student to update with id: " + id);
    }

    @Test
    public void StudentService_UpdateStudentWithAlreadyExistingMail_ThrowIllegalStateException() {
        // Given
        Long id = 1L;
        Student student = demoStudent();
        Student anotherStudent = new Student(
                "Adam",
                "Smith",
                "adam@mail.com",
                "pass1234"
        );
        String newEmail = "adam@mail.com";

        when(studentRepository.findById(id))
                .thenReturn(Optional.of(student));
        when(studentRepository.findByEmail(newEmail))
                .thenReturn(Optional.of(anotherStudent));
        // When
        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> underTest.updateStudent(id, newEmail));

        // Then
        Assertions.assertThat(thrown.getMessage()).isEqualTo("Email is taken");
    }


    @Test
    public void StudentService_DeleteStudent_VerifyDeletion() {
        // Given
        Long id = 1L;
        when(studentRepository.existsById(id)).thenReturn(true);

        // When
        underTest.deleteStudent(id);

        // Then
        verify(studentRepository).existsById(id);
        verify(studentRepository, times(1)).deleteById(id);
    }


    private Student demoStudent() {
        return new Student(
                "John",
                "Doe," ,
                "johndoe@mail.com",
                "password123"
        );
    }

    private StudentDTO demoStudentDTO1() {
        return new StudentDTO(
                1L,
                "John",
                "Doe",
                "johndoe@mail.com"
        );
    }
    private StudentDTO demoStudentDTO2() {
        return new StudentDTO(
                2L,
                "Adam",
                "Smith",
                "adamsmith@mail.com"
        );
    }

    private List<StudentDTO> demoStudentDTOList() {
        List<StudentDTO> list = new ArrayList<>();
        list.add(demoStudentDTO1());
        list.add(demoStudentDTO2());

        return list;
    }

    private StudentResponse demoStudentResponse() {
        return new StudentResponse(
                demoStudentDTOList(),
                0,
                10,
                2,
                1,
                true
        );
    }

    private StudentRegistrationRequest demoStudentRegistrationRequest() {
        return new StudentRegistrationRequest(
                "John",
                "Doe",
                "johndoe@mail.com",
                "password123");
    }

}
