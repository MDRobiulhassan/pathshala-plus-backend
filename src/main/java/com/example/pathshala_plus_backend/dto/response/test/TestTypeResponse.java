package com.example.pathshala_plus_backend.dto.response.test;

import com.example.pathshala_plus_backend.enums.TestTypeName;

public record TestTypeResponse(
        Long id,
        TestTypeName name
) {
}
