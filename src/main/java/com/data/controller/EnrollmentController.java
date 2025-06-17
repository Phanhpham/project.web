package com.data.controller;

import com.data.model.Course;
import com.data.model.Enrollment;
import com.data.model.Student;
import com.data.service.CourseService;
import com.data.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;
    @Autowired
    private CourseService courseService;

    @PostMapping("enrollment")
    public String enrollment(@RequestParam("courseId") int courseId,
                             Model model,
                             HttpSession session) {
        Student student = (Student) session.getAttribute("student");
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        Course course = courseService.findById(courseId);
        enrollment.setCourse(course);
        enrollmentService.registerCourse(enrollment);

        model.addAttribute("openModal", false);
        return "redirect:/list";
    }

    @GetMapping("history/{id}")
    public String historyCourse(@PathVariable("id") int studentId,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "5") int size,
                                @RequestParam(value = "keyword", required = false) String name,
                                @RequestParam(value = "sort", required = false) String sort,
                                Model model, HttpSession session
    ) {

        List<Enrollment> enrollments;
        int totalPages;

        if (name != null && !name.trim().isEmpty()) {
            enrollments = enrollmentService.searchByName(studentId, name, page, size);
            totalPages = (int) Math.ceil((double) enrollmentService.countPageByName(studentId, name)/size);

        } else if (sort != null && !sort.isEmpty() && !sort.equals("all")) {
            enrollments = enrollmentService.sortStatus(studentId,sort,page,size);
            System.out.println(enrollments);
            totalPages = (int) Math.ceil((double) enrollmentService.countTotal(studentId)/size);
        } else {

            enrollments = enrollmentService.getAllEnrollment(studentId, page, size);
            totalPages = (int) Math.ceil((double) enrollmentService.countTotal(studentId)/size);
        }

        model.addAttribute("enrollments",enrollments);
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages",totalPages);
        model.addAttribute("size",size);
        model.addAttribute("sort", (sort != null) ? sort : "all");
        return"historyCourse";
    }
}
