package com.weatherforecast.service;

import com.weatherforecast.entity.ConfirmationCode;
import com.weatherforecast.entity.User;
import com.weatherforecast.exception.NotFoundException;
import com.weatherforecast.repository.ConfirmationCodeRepository;
import com.weatherforecast.service.mail.MailUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@Service
public class CodeConfirmationService {

    private final ConfirmationCodeRepository repository;
    private final MailUtil mailUtil;

    private final int EXPIRATION_PERIOD = 1; // in days

    private final String LINK_PATH = "http://localhost:8080/api/public/confirmation?code=";

    public void confirmationCodeManager(User user) {
        String code = generateCode();
        saveConfirmationCode(code, user);
        sendCodeByEmail(code, user);
    }

    private void sendCodeByEmail(String code, User user) {
        String linkToSend = LINK_PATH + code;
        mailUtil.send(user, linkToSend);
        System.out.printf("Confirmation code: " + linkToSend);
    }

    private void saveConfirmationCode(String generatedCode, User user) {
        ConfirmationCode newCode = ConfirmationCode.builder()
                .code(generatedCode)
                .user(user)
                .expireDataTime(LocalDateTime.now().plusDays(EXPIRATION_PERIOD))
                .isConfirmed(false)
                .build();
        repository.save(newCode);
    }

    /**
     * Generate a random code for confirmation
     *
     * @return String variable with random code
     * @UUID - universal uniq identifier
     * @format -  128 bit
     * @template -  xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx ('x' - is a character or a number)
     * @example - 3f29c3b2-9fc2-11ed-a8fc-0242ac120002
     */
    private String generateCode() {
        return UUID.randomUUID().toString();
    }

    public User changeConfirmationStatusByCode(String code) {
        ConfirmationCode confirmationCode = repository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Confirmation code: " + code + " not found"));
        User user = confirmationCode.getUser();
        confirmationCode.setConfirmed(true);
        repository.save(confirmationCode);
        return user;
    }

    public List<ConfirmationCode> findCodesByUser(User user) {
        return repository.findByUser(user);
    }


}
