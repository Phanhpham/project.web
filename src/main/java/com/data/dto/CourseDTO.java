package com.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {

    private int id;

    @NotBlank(message = "Course name must not be blank")
    @Size(max = 100, message = "Course name must be at most 100 characters")
    private String name;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be greater than 0")
    private int duration;

    @NotBlank(message = "Instructor name must not be blank")
    @Size(max = 100, message = "Instructor name must be at most 100 characters")
    private String instructor;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createAt;

    private String image;
    private MultipartFile file;
}
