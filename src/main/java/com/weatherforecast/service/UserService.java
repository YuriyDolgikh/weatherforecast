package com.weatherforecast.service;

import com.weatherforecast.dto.user.UserRequestDto;
import com.weatherforecast.dto.user.UserResponseDto;
import com.weatherforecast.dto.user.UserUpdateRequestDto;
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
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final ConfirmationCodeService codeConfirmationService;

    /**
     * Register a new user in the system.
     * wChecks whether the email already exists, creates a new user entity
     * with default role and status, saves it to the repository, and sends
     * a confirmation code via email.
     * @param request from user registration data
     * @return {@link UserResponseDto} containing information about the registered user
     * @throws AlreadyExistException if a user with the given email already exists
     */
    @Transactional
    public UserResponseDto registration(UserRequestDto request) {

        // Check the password is not null or blank
        if (request.getHashPassword() == null || request.getHashPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }

        if (request.getHashPassword().length() < 6 || request.getHashPassword().length() > 20) {
            throw new IllegalArgumentException("Password length must be between 6 and 20");
        }

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


    /**
     * Retrieves all users from the system
     * Converts all user entities from the repository into {@link UserResponseDto}
     * @return a list of {@link UserResponseDto} representing all users in the system
     */
    public List<UserResponseDto> getAllUsers() {
        return userConverter.fromUsers(userRepository.findAll());
    }


    /**
     * Retrieves a user by their unique identifier.
     * Searches for the user in the repository by the given ID.
     * If no user is found, a {@link NotFoundException} is thrown.
     * @param id the unique identifier of the user
     * @return  {@link UserResponseDto} representing the found user
     * @throws NotFoundException if no user with the given ID exists
     */
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id = " + id + " not found"));

        return userConverter.toDto(user);
    }


    /**
     * Retrieves a user by their unique identifier for administrative purposes.
     Searches for the user in the repository by the given ID.
     * If no user is found, a {@link NotFoundException} is thrown.
     * @param id the unique identifier of the user
     * @return the {@link User} entity representing the found user
     * @throws NotFoundException if no user with the given ID exists
     */
    public User getUserByIdForAdmin(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new NotFoundException("User with id = " + id + " not found"));
    }


    /**
     * Retrieves users from the system
     * @returnRetrieves all users from the system with full details.
     * Returns the complete list of {@link User} entities as stored in the repository,
     * including all fields without conversion to DTO.
     * @return a list of {@link User} entities containing full user information
     */
    public List<User> getAllUsersFullDetails() {
        return userRepository.findAll();
    }


    /**
     * Retrieves a user from the system by their email address.
     *  * Searches for the user in the repository using the provided email.
     * If no user is found, a {@link NotFoundException} is thrown.
     * @param email the email address of the user
     * @return a {@link UserResponseDto} representing the found user
     * @throws NotFoundException if no user with the given email exists
     */
    public UserResponseDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email: " + email + " not found"));

        return userConverter.toDto(user);
    }


    /**
     *
     * @param code
     * @return
     */
    @Transactional
    public String confirmationEmail(String code) {
        User user = codeConfirmationService.changeConfirmationStatusByCode(code);
        user.setStatus(User.Status.CONFIRMED);
        userRepository.save(user);
        return "Email " + user.getEmail() + " is successfully confirmed";
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
        userByEmail.setUpdateDate(LocalDateTime.now());
        userRepository.save(userByEmail);
        return userConverter.toDto(userByEmail);
    }

    @Transactional
    public boolean deleteUser(Long id) {

        // Check that such id exists
        // If not - return false and do nothing
        if (!userRepository.existsById(id)) {
            User user = userRepository.findById(id).get();
            if (user.getRole().equals(User.Role.ADMIN)) {
                throw new BadRequestException("You can't delete an admin");
            }
            return false;
        }

        // If exists - delete by id
        userRepository.deleteConfirmationCodesByUserId(id);
        userRepository.deleteUserById(id);
        return true;
    }

//    @Transactional
//    public boolean renewCode(String email) {    // TODO - by the what case?
//
//        User user = getUserByEmailOrThrow(email);
//
//        codeConfirmationService.confirmationCodeManager(user);
//        return true;
//    }

//    public List<ConfirmationCode> findCodesByUser(String email) {
//        User user = getUserByEmailOrThrow(email);
//        return codeConfirmationService.findCodesByUser(user);
//    }

    public User getUserByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email: " + email + " not found"));
    }

    public User getCurrentUser() {
        return getUserByEmailOrThrow(getCurrentUserEmail());
    }

    public String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void setConfirmedAndAdmin(User user) {
        user.setStatus(User.Status.CONFIRMED);
        user.setRole(User.Role.ADMIN);
        userRepository.save(user);
    }
}
