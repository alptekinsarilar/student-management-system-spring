package com.alptekin.student.dto;

import java.util.List;

public record StudentResponse(
        List<StudentDTO> content,
        int pageNo,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean last
) {
}
