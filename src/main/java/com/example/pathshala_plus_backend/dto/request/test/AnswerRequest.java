package com.example.pathshala_plus_backend.dto.request.test;

import jakarta.validation.constraints.NotNull;

public record AnswerRequest(
        @NotNull Long questionId,
        Long selectedOptionId
) {
}
