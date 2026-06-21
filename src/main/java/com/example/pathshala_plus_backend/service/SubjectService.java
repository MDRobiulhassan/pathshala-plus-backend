package com.example.pathshala_plus_backend.service;

import com.example.pathshala_plus_backend.dto.response.subject.SubjectResponse;

import java.util.List;

public interface SubjectService {
    List<SubjectResponse> getSubjectsByEducationLevel(Long educationLevelId);
}
