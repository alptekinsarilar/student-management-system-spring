package com.alptekin.student.dto;

import jakarta.validation.constraints.NotNull;

public record StudentIdDTO(@NotNull Long id) {
}
