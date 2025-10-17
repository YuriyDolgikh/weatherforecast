package com.weatherforecast.service;

import com.weatherforecast.config.FreemarkerConfig;
import com.weatherforecast.entity.ConfirmationCode;
import com.weatherforecast.entity.User;
import com.weatherforecast.repository.ConfirmationCodeRepository;
import com.weatherforecast.repository.UserRepository;
import com.weatherforecast.service.mail.MailUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
public class ConfirmationCodeServiceSendCodeByEmail {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private Configuration freemarkerConfiguration;

    @Autowired
    private ConfirmationCodeRepository confirmationCodeRepository;

    @Autowired
    private UserService userService;

    @Mock
    private Template template;

    @Autowired
    private ConfirmationCodeService confirmationCodeService;

    @InjectMocks
    private MailUtil mailUtil;

    User testUser;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        testUser = User.builder()
                .name("testUser")
                .email("testUser@company.com")
                .hashPassword("Pass12345")
                .role(User.Role.USER)
                .status(User.Status.NOT_CONFIRMED)
                .createDate(now)
                .updateDate(now)
                .build();

        User savedUser = userRepository.save(testUser);

        ConfirmationCode confirmationCode = ConfirmationCode.builder()
                .code("someConfirmationCode")
                .user(savedUser)
                .expireDataTime(now.plusDays(1))
                .build();

        confirmationCodeRepository.save(confirmationCode);

    }

    @AfterEach
    void dropDatabase() {
        confirmationCodeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
//    @WithMockUser(username = "testUser@company.com", roles = "USER")
    void testSendCodeByEmailSuccess() throws Exception{
        String confirmationLink = "http://localhost:8080/api/public/confirmation?code=someConfirmationCode";
        MimeMessage mimeMessage = mock(MimeMessage.class);

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(freemarkerConfiguration.getTemplate("confirm_registration_mail.ftlh")).thenReturn(template);

        when(FreeMarkerTemplateUtils.processTemplateIntoString(eq(template), Map.class))
        .thenAnswer(invocationOnMock ->
                "Hello, User! Please confirm your registration by clicking on the link: " + confirmationLink);


        mailUtil.send(testUser, confirmationLink);

        verify(FreeMarkerTemplateUtils.processTemplateIntoString(eq(template), Map.class), times(1));

    }

}
