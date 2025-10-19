package com.weatherforecast.service;

import com.weatherforecast.entity.User;
import com.weatherforecast.exception.MailSendingException;
import com.weatherforecast.service.mail.MailUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailUtilSendTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private Configuration freemakerConfiguration;

    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private Template template;

    @Spy
    @InjectMocks
    private MailUtil mailUtil;

    @Test
    void testSendAllIsOk() throws TemplateException, IOException {
        User user = User.builder()
                .email("testUser@company.com")
                .build();

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doReturn("email content").when(mailUtil).createConfirmationEmail(user, "http://test.com:8080/confirmRegistration?code=test-code");

        mailUtil.send(user, "http://test.com:8080/confirmRegistration?code=test-code");

        verify(mailSender).send(mimeMessage);
    }

    @Test
    void testSendWhenUserIsNull() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        assertThrows(MailSendingException.class, () -> mailUtil.send(null, "http://test.com:8080/confirmRegistration?code=test-code"));
    }

    @Test
    void testSendWhenEmailIsNull() {
        User user = User.builder().email(null).build();
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        assertThrows(MailSendingException.class, () -> mailUtil.send(user, "http://test.com:8080/confirmRegistration?code=test-code"));
    }

    @Test
    void testSendWhenEmailIsBlank() {
        User user = User.builder().email("   ").build();
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        assertThrows(MailSendingException.class, () -> mailUtil.send(user, "http://test.com:8080/confirmRegistration?code=test-code"));
    }
}