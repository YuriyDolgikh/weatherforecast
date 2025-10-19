package com.weatherforecast.service;

import com.weatherforecast.entity.User;
import com.weatherforecast.service.mail.MailUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailUtilCreateConfirmationEmailTest {

    @Mock
    private Configuration freemakerConfiguration;

    @InjectMocks
    private MailUtil mailUtil;

    @Test
    void testCreateConfirmationEmailSuccess() throws Exception {
        User user = User.builder()
                .name("John")
                .build();
        Template template = mock(Template.class);

        when(freemakerConfiguration.getTemplate("confirm_registration_mail.ftlh"))
                .thenReturn(template);

        String result = mailUtil.createConfirmationEmail(user, "http://test.com:8080/confirmRegistration?code=test-code");

        assertNotNull(result);
        verify(freemakerConfiguration).getTemplate("confirm_registration_mail.ftlh");
    }

    @Test
    void testCreateConfirmationEmailTemplateNotFound() throws IOException {
        User user = User.builder().build();
        when(freemakerConfiguration.getTemplate("confirm_registration_mail.ftlh"))
                .thenThrow(new IOException("Not found"));

        assertThrows(IOException.class, () -> mailUtil.createConfirmationEmail(user, "http://test.com:8080/confirmRegistration?code=test-code"));
    }
}