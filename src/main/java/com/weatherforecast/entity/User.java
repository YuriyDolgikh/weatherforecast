package com.weatherforecast.entity;

import java.time.LocalDate;
import java.util.List;

public class User {

    public enum Role {
        ADMIN,
        USER,
    }


    Long id;
    String name;
    String email;
    String hashPassword;
    Role role;
    LocalDate createDate;
    LocalDate updateDate;
    List<City> favoriteCities;


}
