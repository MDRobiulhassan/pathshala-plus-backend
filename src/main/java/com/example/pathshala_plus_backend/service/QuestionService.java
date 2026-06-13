package com.example.pathshala_plus_backend.service;

import com.example.pathshala_plus_backend.dto.response.question.ExamQuestionsResponse;


public interface QuestionService {
    ExamQuestionsResponse getQuestionsByExamInfo(Long examInfoId);
}
