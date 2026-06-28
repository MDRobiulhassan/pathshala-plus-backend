package com.example.pathshala_plus_backend.serviceimpl;

import com.example.pathshala_plus_backend.dto.response.test.TestTypeResponse;
import com.example.pathshala_plus_backend.entity.TestType;
import com.example.pathshala_plus_backend.enums.TestTypeName;
import com.example.pathshala_plus_backend.repository.TestTypeRepository;
import com.example.pathshala_plus_backend.service.TestService;
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
public class TestServiceImpl implements TestService {

    TestTypeRepository testTypeRepository;

    @Override
    public List<TestTypeResponse> getAllTestTypes() {
        List<TestType> testTypes = testTypeRepository.findAll();
        return testTypes.stream()
                .map(testType -> new TestTypeResponse(
                        testType.getId(),
                        testType.getTestTypeName()
                )).toList();
    }

    @Override
    public List<TestTypeResponse> getQuestionBankTestTypes() {

        List<TestType> testTypes = testTypeRepository.findByTestTypeNameIn(
                List.of(TestTypeName.PRACTICE, TestTypeName.TIMED)
        );

        return testTypes.stream()
                .map(testType -> new TestTypeResponse(
                        testType.getId(),
                        testType.getTestTypeName()
                ))
                .toList();
    }
}
