package com.data.controller;

import com.data.model.Course;
import com.data.model.Enrollment;
import com.data.model.Student;
import com.data.service.CourseService;
import com.data.service.EnrollmentService;
import com.data.utils.SessionUtils;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.zip.Deflater;

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
                                HttpServletRequest request,
                                @RequestParam(value = "keyword", required = false) String name,
                                @RequestParam(value = "sort", required = false) String sort,
                                @RequestParam(value = "enrollmentId",required = false) Integer enrollmentId,
                                @RequestParam(defaultValue = "false") boolean isShowModalCancel,
                                Model model, HttpSession session
    ) {
        Student student = SessionUtils.getLoginStudent(request, session);
        if (student == null) {
            return "redirect:/login";
        }
        List<Enrollment> enrollments;

        int totalPages;

        if (name != null && !name.trim().isEmpty()) {
            enrollments = enrollmentService.searchByName(studentId, name, page, size);
            totalPages = (int) Math.ceil((double) enrollmentService.countPageByName(studentId, name) / size);

        } else if (sort != null && !sort.isEmpty() && !sort.equals("all")) {
            enrollments = enrollmentService.sortStatus(studentId, sort, page, size);
            System.out.println(enrollments);
            totalPages = (int) Math.ceil((double) enrollmentService.countTotal(studentId) / size);
        } else {

            enrollments = enrollmentService.getAllEnrollment(studentId, page, size);
            totalPages = (int) Math.ceil((double) enrollmentService.countTotal(studentId) / size);
        }

        model.addAttribute("enrollments", enrollments);
        model.addAttribute("currentPage", page);
        model.addAttribute("student",student);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        model.addAttribute("enrollmentId",enrollmentId);
        model.addAttribute("isShowModalCancel",isShowModalCancel);
        model.addAttribute("id",studentId);
        model.addAttribute("sort", (sort != null) ? sort : "all");
        return "historyCourse";
    }

    @GetMapping("managerEnrollment")
    public String showEnrollment(Model model, HttpServletRequest request, HttpSession session,
                                 @RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "5") int size,
                                 @RequestParam(defaultValue = "false") boolean isShowModalConfirm,
                                 @RequestParam(defaultValue = "false") boolean isShowModalDeny,
                                 @RequestParam(value = "filter", required = false) String filter,
                                 @RequestParam(value = "keyword", required = false) String keyword,
                                 @RequestParam(value = "enrollmentId", required = false) Integer enrollmentId
    ) {
        Student student = SessionUtils.getLoginStudent(request, session);
        if (student == null) {
            return "redirect:/login";
        }
        List<Enrollment> getAllListEnrollment;
        int totalPages;

        if (filter != null && !filter.trim().isEmpty() && !filter.equals("all")) {
            getAllListEnrollment = enrollmentService.filterStatus(filter, page, size);
            totalPages = (int) Math.ceil((double) enrollmentService.countStatus(filter) / size);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            getAllListEnrollment = enrollmentService.searchByNameCourse(keyword, page,size);
            totalPages  = (int) Math.ceil((double) enrollmentService.countSearch(keyword) / size);
        }else{
            getAllListEnrollment = enrollmentService.getAllStudentOfCourse(page, size);
            totalPages = (int) Math.ceil((double) enrollmentService.count() / size);
        }


        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        model.addAttribute("getAllList", getAllListEnrollment);
        model.addAttribute("isShowModalConfirm", isShowModalConfirm);
        model.addAttribute("isShowModalDeny", isShowModalDeny);
        model.addAttribute("enrollmentId", enrollmentId);
        model.addAttribute("filter", filter);
        model.addAttribute("keyword", keyword);
        return "enrollmentManager";
    }

    @PostMapping("confirm")
    public String confirm(Model model,
                          @RequestParam(value = "enrollmentId", required = false) Integer enrollmentId
    ) {
        enrollmentService.confirmEnrollment(enrollmentId);
        model.addAttribute("isShowModalConfirm", false);
        return "redirect:/managerEnrollment";
    }

    @PostMapping("denied")
    public String denied(Model model,
                         @RequestParam(value = "enrollmentId", required = false) Integer enrollmentId
    ) {
        enrollmentService.denyEnrollment(enrollmentId);
        model.addAttribute("idShowModelDeny", false);
        return "redirect:/managerEnrollment";
    }

    @PostMapping("cancel")
    public String cancel(Model model,
                         @RequestParam(value = "enrollmentId",required = false) Integer enrollmentId,
                         HttpSession session,HttpServletRequest request
                         ){
        Student student = SessionUtils.getLoginStudent(request,session);
        enrollmentService.cancelRegisterCourse(enrollmentId);
        model.addAttribute("isShowModalCancel",false);
        return "redirect:/history/"+ student.getId();
}
}
