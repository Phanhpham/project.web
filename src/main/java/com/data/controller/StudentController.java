package com.data.controller;

import com.data.dto.StudentDTO;
import com.data.model.Course;
import com.data.model.Student;
import com.data.service.StudentService;
import com.data.utils.SessionUtils;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.metamodel.Bindable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("pageStudent")
    public String studentManager(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            HttpServletRequest request,
            HttpSession session,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "sort", required = false) String sort,
            Model model
    ) {
        Student student = SessionUtils.getLoginStudent(request, session);
        if (student == null) {
            return "redirect:/login";
        }
        List<Student> students;
        String sortType = null;
        String field = null;

        if (sort != null && !sort.trim().isEmpty()) {
            String[] strings = sort.split(" ");
            sortType = strings[0];
            field = strings[1];
        }
        int totalPages;
        if (keyword != null && !keyword.trim().isEmpty()) {
            students = studentService.searchByName(keyword, page, size);
            if (students.isEmpty()) {
                model.addAttribute("message", "khong co ket qua phu hop!");
            }
            totalPages = (int) Math.ceil((double) studentService.countSearch(keyword) / size);
        } else {
            students = studentService.getStudentByPage(page, size);
            if (students.isEmpty()) {
                model.addAttribute("message", "Danh sach trong!");
            }
            totalPages = (int) Math.ceil((double) studentService.countTotalStudent() / size);
        }


        if (sortType != null && field != null) {
            switch (field) {
                case "id":
                    students = studentService.sortById(sortType, page, size);
                    totalPages = (int) Math.ceil((double) studentService.countTotalStudent() / size);
                    break;
                case "name":
                    students = studentService.sortByName(sortType, page, size);
                    totalPages = (int) Math.ceil((double) studentService.countTotalStudent() / size);
                    break;
                default:
                    students = studentService.getStudentByPage(page, size);
            }

        }
        System.out.println(students);

        model.addAttribute("student", students);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        model.addAttribute("sort", sort);
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

    @GetMapping("profileUser")
    public String showProfile(Model model, HttpSession session, HttpServletRequest request,
                              @RequestParam(defaultValue = "false") boolean isShowForm) {
        Student student = SessionUtils.getLoginStudent(request, session);
        if (student == null) {
            return "redirect:/login";

        }
        StudentDTO studentDTO = modelMapper.map(student, StudentDTO.class);
        model.addAttribute("isShowForm", isShowForm);
        model.addAttribute("student", studentDTO);
        return "profile";
    }

    @PostMapping("profileUser")
    public String profile(
            @Valid @ModelAttribute("student") StudentDTO studentDTO,

            BindingResult result,
            HttpSession session,
            Model model

    ) {
        Student existEmail = studentService.findByEmail(studentDTO.getEmail());
        if (existEmail != null && existEmail.getId() != studentDTO.getId()) {
            result.rejectValue("email", "error.email", "email da ton tai");
        }
        Student existPhone = studentService.findByPhone(studentDTO.getPhone());
        if (existPhone != null && existPhone.getId() != studentDTO.getId()) {
            result.rejectValue("phone", "error.phone", "Sdt da ton tai");
        }

        if (result.hasErrors()) {
            model.addAttribute("student", studentDTO);
            return "profile";
        }

        Student student = studentService.findById(studentDTO.getId());
        if (student != null) {
            student.setName(studentDTO.getName());
            student.setEmail(studentDTO.getEmail());
            student.setPhone(studentDTO.getPhone());
            student.setSex(studentDTO.getSex());
            student.setDob(studentDTO.getDob());
            session.setAttribute("student", student);
            studentService.updateProfile(student);
        }
        return "redirect:/list";
    }
    @PostMapping("changePassword")
    public String changePass(Model model,
                             HttpSession session,HttpServletRequest request,
                             @RequestParam(value = "oldPassword",required = false) String oldPassword,
                             @RequestParam(value = "newPassword",required = false) String newPassword,
                             @RequestParam(value = "confirmPassword",required = false) String confirmPassword
    ){
        boolean hasErrors = false;
        Student student = SessionUtils.getLoginStudent(request, session);

        if (oldPassword == null || oldPassword.trim().isEmpty()) {
            model.addAttribute("oldPasswordError", "Vui lòng nhập mật khẩu hiện tại!");
            hasErrors = true;
        }

        if (oldPassword != null && !oldPassword.trim().isEmpty() && !oldPassword.equals(student.getPassword())) {
            model.addAttribute("oldPasswordError", "Mật khẩu không đúng!");
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            model.addAttribute("newPasswordError", "Vui lòng nhập mật khẩu mới!");
            hasErrors = true;
        }

        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            model.addAttribute("confirmPasswordError", "Vui lòng nhập xác nhận mật khẩu!");
            hasErrors = true;
        }

        if (newPassword != null && !newPassword.equals(confirmPassword)) {
            model.addAttribute("confirmPasswordError", "Mật khẩu không khớp!");
            hasErrors = true;
        }
        if (hasErrors){
            model.addAttribute("isShowForm",true);
            model.addAttribute("oldPassword", oldPassword);
            model.addAttribute("newPassword", newPassword);
            model.addAttribute("confirmPassword", confirmPassword);
            model.addAttribute("student",student);
            return "profile";
        }
        studentService.changePassword(student.getId(),newPassword);
        model.addAttribute("isShowForm",false);
        return "redirect:/profileUser";
    }

}
