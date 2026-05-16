package com.example.pathshala_plus_backend.listener;

import com.example.pathshala_plus_backend.event.PasswordResetOtpEvent;
import com.example.pathshala_plus_backend.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
public class PasswordResetOtpEmailListener {

    private final EmailService emailService;

    @EventListener
    public void handlePasswordResetOtpEvent(PasswordResetOtpEvent event) throws MessagingException, UnsupportedEncodingException {
        emailService.sendOtpEmail(event.email(), event.otp());
    }
}
