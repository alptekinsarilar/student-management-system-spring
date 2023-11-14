package com.alptekin.student.service;

import com.alptekin.student.repository.StudentRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StudentServiceUnitTests {
    @Mock
    StudentRepository studentRepository;

    @InjectMocks
    StudentService underTest;


}
