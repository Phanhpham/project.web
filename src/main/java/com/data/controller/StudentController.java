package com.data.controller;

import com.data.model.Course;
import com.data.model.Student;
import com.data.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("pageStudent")
    public String studentManager(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model
    ) {
        List<Student> students = studentService.getStudentByPage(page, size);
        long totalStudents = studentService.countTotalStudent();

        int totalPages = (int) Math.ceil((double) totalStudents / size);

        model.addAttribute("student", students);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "student";
    }

    @GetMapping("/students/block/{id}")
    public String blockStudent(@PathVariable("id") int id) {
        studentService.blockUserById(id);
        return "redirect:/pageStudent";
    }

    @GetMapping("/students/unblock/{id}")
    public String unblockStudent(@PathVariable("id") int id) {
        studentService.unblockUserById(id);
        return "redirect:/pageStudent";
    }

}
