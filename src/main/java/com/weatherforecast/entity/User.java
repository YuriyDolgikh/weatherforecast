package com.weatherforecast.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "account")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 15)
    private String name;

    @Email(regexp = "^[A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z]{2,}$", message = "Invalid email")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    private String hashPassword;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @ManyToMany
    @JoinTable(
            name = "user_cities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "city_id")
    )
    private Set<City> cities = new HashSet<>();

    public enum Role {
        ADMIN,
        USER,
    }

    public enum Status {
        NOT_CONFIRMED,
        CONFIRMED,
        DELETE
    }
}
