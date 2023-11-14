package com.alptekin.student.dto;

import jakarta.validation.constraints.NotNull;


public record StudentDTO(Long id,
                         @NotNull String firstName,
                         @NotNull String lastName,
                         @NotNull String email) {

}
