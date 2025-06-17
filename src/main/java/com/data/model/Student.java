package com.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private Boolean sex;

    @Column(unique = true, length = 20)
    private String phone;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "create_at", nullable = false)
    private LocalDate createAt = LocalDate.now();

    @Column(nullable = false)
    private Boolean role;
}
