package com.example.pathshala_plus_backend.supabase;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StorageBucket {
    AVATARS("avatars"),
    QUESTION_ASSETS("question-assets"),
    INSTITUTE_ASSETS("institute-assets");

    private final String bucketName;
}