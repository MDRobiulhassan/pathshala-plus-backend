package com.example.pathshala_plus_backend.service;

import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface EmailService {
    void sendOtpEmail(String toEmail, String otp) throws MessagingException, UnsupportedEncodingException;
}
