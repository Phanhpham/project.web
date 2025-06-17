package com.data.controller;

import com.data.dto.LoginDTO;
import com.data.dto.StudentDTO;
import com.data.model.Student;
import com.data.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class AuthController {
    @Autowired
    private StudentService studentService;


    @GetMapping("register")
    public String register(Model model) {
        model.addAttribute("registerUser", new StudentDTO());
        return "register";
    }

    @PostMapping("register")
    public String registerUser(@Valid @ModelAttribute("registerUser") StudentDTO dto, BindingResult bindingResult, Model model) {

        // Kiểm tra username đã tồn tại
        if (studentService.existsByUsername(dto.getUsername())) {
            bindingResult.rejectValue("username", "Tên đăng nhập đã tồn tại!");

        }
        // Kiểm tra email đã tồn tại
        if (studentService.existsByEmail(dto.getEmail())) {
            bindingResult.rejectValue("email", "Email đã tồn tại!");

        }
        if (studentService.existsByPhone(dto.getPhone())) {
            bindingResult.rejectValue("phone", "So dien thoai đã tồn tại!");

        }
        if (bindingResult.hasErrors()) {
            return "register";
        }


        // Tạo đối tượng Student từ DTO
        Student student = new Student();
        student.setUsername(dto.getUsername());
        student.setName(dto.getName());
        student.setDob(dto.getDob());
        student.setEmail(dto.getEmail());
        student.setSex(dto.getSex());
        student.setPassword(dto.getPassword());
        student.setPhone(dto.getPhone());
        student.setRole(true);

        studentService.save(student);


        return "redirect:/login";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text != null && !text.trim().isEmpty()) {
                    setValue(LocalDate.parse(text, formatter));
                }
            }
        });
    }

    @GetMapping("login")
    public String signIn(Model model) {
        model.addAttribute("signIn", new LoginDTO());
        return "login";
    }

    @PostMapping("login")
    public String login(@Valid @ModelAttribute("signIn") LoginDTO loginDTO, BindingResult bindingResult, HttpSession session,Model model) {

        if (bindingResult.hasErrors()) {
            return "login";
        }

        Student student = studentService.findByEmail(loginDTO.getEmail());

        // Nếu tài khoản bị block
        if (!student.getRole()) {
            model.addAttribute("blocked", true);
            return "login";
        }

        if (!student.getPassword().equals(loginDTO.getPassword())) {
            bindingResult.rejectValue("password", "error", "Mật khẩu không đúng!");
            return "login";
        }
        session.setAttribute("student", student);

        if (student.getRole()) {
            return "redirect:/list";
        }
        return "redirect:/home";
    }

    @GetMapping("home")
    public String userPage() {
        return "home";
    }

    @GetMapping("admin")
    public String adminPage() {
        return "admin";
    }


}
