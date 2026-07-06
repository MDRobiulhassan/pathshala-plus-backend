package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.entity.Question;
import com.example.pathshala_plus_backend.entity.TestAnswer;
import com.example.pathshala_plus_backend.entity.TestAttempt;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestAnswerMapper {
    public static TestAnswer toTestAnswer(TestAttempt attempt, Question question) {
        TestAnswer testAnswer = new TestAnswer();
        testAnswer.setTestAttempt(attempt);
        testAnswer.setQuestion(question);
        return testAnswer;
    }
}