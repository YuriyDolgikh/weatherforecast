package com.weatherforecast.service;

import com.weatherforecast.entity.ConfirmationCode;
import com.weatherforecast.entity.User;
import com.weatherforecast.exception.NotFoundException;
import com.weatherforecast.repository.ConfirmationCodeRepository;
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

    private final int EXPIRATION_PERIOD = 1;

    private final String LINK_PATH = "localhost:8080/api/users/code/confirmation?code=";

    public void confirmationCodeManager(User user){
        String code = generateCode();
        saveConfirmationCode(code, user);
        sendCodeByEmail(code, user);
    }

    private void sendCodeByEmail(String code, User user) {

        String linkToSend = LINK_PATH + code;

        // TODO тут будет отправка пользователю письма с кодом

        System.out.printf("Код подтверждения: " + linkToSend);


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

    private String generateCode() {

        // universal uniq identifier
        // формат 128 bit
        // xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
        // где каждый символ 'x' - это либо цифра либо символ от a-f
        // 3f29c3b2-9fc2-11ed-a8fc-0242ac120002

        return UUID.randomUUID().toString();

    }

    public User changeConfirmationStatusByCode(String code){
        ConfirmationCode confirmationCode = repository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Confirmation code: " + code + " not found"));

        User user = confirmationCode.getUser();

        confirmationCode.setConfirmed(true);

        repository.save(confirmationCode);

        return user;
    }

    public List<ConfirmationCode> findCodesByUser(User user){
        return repository.findByUser(user);
    }


}
