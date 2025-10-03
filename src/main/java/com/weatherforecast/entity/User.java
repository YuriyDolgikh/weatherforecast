package com.weatherforecast.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    public enum Role {
        ADMIN,
        USER,
    }
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


    private LocalDate createDate;

    private LocalDate updateDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<City> cities = new HashSet<>();
}
