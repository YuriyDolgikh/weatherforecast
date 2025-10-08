package com.weatherforecast.service;

import com.weatherforecast.dto.user.UserRequestDto;
import com.weatherforecast.dto.user.UserResponseDto;
import com.weatherforecast.dto.user.UserUpdateRequestDto;
import com.weatherforecast.entity.ConfirmationCode;
import com.weatherforecast.entity.User;
import com.weatherforecast.exception.AlreadyExistException;
import com.weatherforecast.exception.BadRequestException;
import com.weatherforecast.exception.NotFoundException;
import com.weatherforecast.repository.UserRepository;
import com.weatherforecast.service.util.UserConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final CodeConfirmationService codeConfirmationService;

    @Transactional
    public UserResponseDto registration(UserRequestDto request) {
        // Check duplicate for email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistException("User with email: " + request.getEmail() + " is already exist");
        }

        // When email is unique - create a new user
        LocalDateTime now = LocalDateTime.now();

        User newUser = userConverter.fromDto(request);
        newUser.setRole(User.Role.USER); // by default - role is USER
        newUser.setStatus(User.Status.NOT_CONFIRMED); // by default - status is NOT_CONFIRMED
        newUser.setCreateDate(now);
        newUser.setUpdateDate(now);

        userRepository.save(newUser);
        // After creating a new user, we need to create a new confirmation code for him and send it to him by email
        codeConfirmationService.confirmationCodeManager(newUser);
        return userConverter.toDto(newUser);
    }

    public List<UserResponseDto> getAllUsers() {
        return userConverter.fromUsers(userRepository.findAll());
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id = " + id + " not found"));

        return userConverter.toDto(user);
    }

    public List<User> getAllUsersFullDetails() {
        return userRepository.findAll();
    }

    public UserResponseDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email: " + email + " not found"));

        return userConverter.toDto(user);
    }

    @Transactional
    public String confirmationEmail(String code) {
        User user = codeConfirmationService.changeConfirmationStatusByCode(code);
        user.setStatus(User.Status.CONFIRMED);
        userRepository.save(user);
        return "Email " + user.getEmail() + " successfully confirmed";
    }

    @Transactional
    public UserResponseDto updateUser(UserUpdateRequestDto updateRequest) {

        if (updateRequest.getEmail() == null || updateRequest.getEmail().isBlank()) {
            throw new BadRequestException("Email must be provided to update user");
        }

        String userEmail = updateRequest.getEmail();
        // Find the user by email
        User userByEmail = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User with email: " + userEmail + " not found"));
        // Check that the user for update is the same as the current user
        User currentUser = getCurrentUser();
        if (!currentUser.getEmail().equals(updateRequest.getEmail())) {
            throw new BadRequestException("You can't update another user");
        }

        // Update all presented fields.
        // Is not known in advance which fields the user wants to change,
        // so in the JSON (in the request body) there will be only those fields (that are not empty),
        // which the user wants to change (not obligatory all)
        if (updateRequest.getName() != null && !updateRequest.getName().isBlank()) {
            userByEmail.setName(updateRequest.getName());
        }

        if (updateRequest.getHashPassword() != null && !updateRequest.getHashPassword().isBlank()) {
            userByEmail.setHashPassword(updateRequest.getHashPassword());
        }

        // Save the updated user
        userRepository.save(userByEmail);
        return userConverter.toDto(userByEmail);
    }

    @Transactional
    public boolean deleteUser(Long id) {

        // Check that such id exists
        // If not - return false and do nothing
        if (!userRepository.existsById(id)) {
            return false;
        }

        // If exists - delete by id
        userRepository.deleteConfirmationCodesByUserId(id);
        userRepository.deleteUserById(id);
        return true;
    }

    @Transactional
    public boolean renewCode(String email) {    // TODO - by the what case?

        User user = getUserByEmailOrThrow(email);

        codeConfirmationService.confirmationCodeManager(user);
        return true;
    }

    public List<ConfirmationCode> findCodesByUser(String email) {
        User user = getUserByEmailOrThrow(email);
        return codeConfirmationService.findCodesByUser(user);
    }

    private User getUserByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email: " + email + " not found"));
    }

    public User getCurrentUser() {
        return getUserByEmailOrThrow(getCurrentUserEmail());
    }

    public String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public void setRoleAdminForUser(User user) {
        user.setRole(User.Role.ADMIN);
        userRepository.save(user);
    }
}
