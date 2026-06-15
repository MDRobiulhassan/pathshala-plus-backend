package com.example.pathshala_plus_backend.service;

import com.example.pathshala_plus_backend.dto.response.question.examinfoquestion.ExamQuestionsResponse;
import com.example.pathshala_plus_backend.dto.response.question.subjectquestion.SubjectBrowseResponse;


public interface QuestionService {
    ExamQuestionsResponse getQuestionsByExamInfo(Long examInfoId);

    SubjectBrowseResponse getQuestionsBySubject(Long subjectId, int page, int size);
}
