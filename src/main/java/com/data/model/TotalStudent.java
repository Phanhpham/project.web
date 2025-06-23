package com.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalStudent {
    private int courseId;
    private String image;
    private String courseName;
    private int duration;
    private long totalStudent;
}
