package com.example.pathshala_plus_backend.service;

import com.example.pathshala_plus_backend.dto.response.test.TestTypeResponse;

import java.util.List;

public interface TestService {
    List<TestTypeResponse> getAllTestTypes();
    List<TestTypeResponse> getQuestionBankTestTypes();
}
