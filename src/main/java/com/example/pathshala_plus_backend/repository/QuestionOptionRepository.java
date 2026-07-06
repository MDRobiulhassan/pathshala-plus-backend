package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.QuestionOption;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionOptionRepository extends JpaRepository<@NonNull QuestionOption, @NonNull Long> {
    @Query("""
            SELECT o FROM QuestionOption o
            JOIN FETCH o.question q
            JOIN TestQuestion tq ON tq.question.id = q.id
            WHERE tq.test.id = :testId
            AND o.isCorrect = true
            """)
    List<QuestionOption> findCorrectOptionsByTestId(@Param("testId") Long testId);

    @Query("""
            SELECT o FROM QuestionOption o
            JOIN FETCH o.question
            WHERE o.question.id IN :questionIds
            """)
    List<QuestionOption> findByQuestionIds(@Param("questionIds") List<Long> questionIds);
}
