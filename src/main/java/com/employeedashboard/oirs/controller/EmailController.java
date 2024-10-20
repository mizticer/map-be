package com.employeedashboard.oirs.controller;

import com.employeedashboard.oirs.dto.email.EmailRequest;
import com.employeedashboard.oirs.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    @PostMapping
    public String sendEmail(@Valid @RequestBody EmailRequest emailRequest) {
        emailService.sendMessage(emailRequest);
        return "Email sent successfully!";
    }
}
