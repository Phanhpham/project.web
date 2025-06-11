package com.data.controller;

import com.data.dto.CourseDTO;
import com.data.model.Course;
import com.data.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller

public class CourseController {
    @Autowired
    private CourseService courseService;
    @GetMapping("courseManager")
    public String courseAdmin(Model model){
        List<Course> courses = courseService.getAll();
        model.addAttribute("courses",courses);
        return "courseManage";
    }
}
