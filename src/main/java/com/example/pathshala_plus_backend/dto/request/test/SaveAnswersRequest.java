package com.example.pathshala_plus_backend.dto.request.test;

import java.util.List;

public record SaveAnswersRequest(
        List<AnswerRequest> answers
) {
}