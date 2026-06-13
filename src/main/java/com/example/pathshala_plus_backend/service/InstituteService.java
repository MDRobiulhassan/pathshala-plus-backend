package com.example.pathshala_plus_backend.service;

import com.example.pathshala_plus_backend.dto.response.institute.InstituteResponse;

import java.util.List;

public interface InstituteService {
    List<InstituteResponse> getAllInstitutes();
}
