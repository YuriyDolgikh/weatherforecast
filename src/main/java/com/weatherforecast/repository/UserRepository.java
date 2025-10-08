package com.weatherforecast.repository;

import com.weatherforecast.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    List<User> findByName(String name);

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE accounts", nativeQuery = true)
    void truncateAndResetAutoIncrement();

    @Modifying
    @Query("DELETE FROM ConfirmationCode cc WHERE cc.user.id = :userId")
    void deleteConfirmationCodesByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM User u WHERE u.id = :userId")
    void deleteUserById(@Param("userId") Long userId);
}
