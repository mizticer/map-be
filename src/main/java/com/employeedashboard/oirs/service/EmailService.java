package com.employeedashboard.oirs.service;

import com.employeedashboard.oirs.dto.email.EmailRequest;
import com.employeedashboard.oirs.exception.EmailErrorSendException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;

    public void sendMessage(EmailRequest emailRequest) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {
            mimeMessageHelper.setTo(emailRequest.getTo().toArray(new String[0]));
            mimeMessageHelper.setSubject(emailRequest.getSubject());
            mimeMessageHelper.setText(emailRequest.getText());
            emailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new EmailErrorSendException("EMAIL_SEND_ERROR");
        }
    }

}
