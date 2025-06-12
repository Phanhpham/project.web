package com.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Course name must not be blank")
    @Size(max = 100, message = "Course name must be at most 100 characters")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be greater than 0")
    private int duration;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Instructor name must not be blank")
    @Size(max = 100, message = "Instructor name must be at most 100 characters")
    private String instructor;

    @Column(name = "create_at", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createAt= LocalDate.now();

    @Column(length = 500)
    private String image;
}
