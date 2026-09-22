package com.example.pathshala_plus_backend.serviceimpl;

import com.example.pathshala_plus_backend.Mapper.SubjectMapper;
import com.example.pathshala_plus_backend.dto.response.subject.SubjectResponse;
import com.example.pathshala_plus_backend.entity.Subject;
import com.example.pathshala_plus_backend.repository.SubjectRepository;
import com.example.pathshala_plus_backend.service.SubjectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class SubjectServiceImpl implements SubjectService {

    SubjectRepository subjectRepository;
    SubjectMapper subjectMapper;

    @Override
    public List<SubjectResponse> getSubjectsByEducationLevel(Long educationLevelId) {
        List<Subject> subjects = subjectRepository.findByEducationLevelId(educationLevelId);
        return subjects.stream()
                .map(subjectMapper::toSubjectResponse)
                .toList();
    }
}