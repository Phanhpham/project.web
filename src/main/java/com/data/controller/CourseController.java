package com.data.controller;

import com.data.dto.CourseDTO;
import com.data.dto.StudentDTO;
import com.data.model.Course;
import com.data.model.Student;
import com.data.repository.CourseRepo;
import com.data.service.CloudinaryService;
import com.data.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Controller

public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private CloudinaryService cloudinaryService;
    @GetMapping("pageCourse")
    public String pageCourse(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String keyword,
            Model model) {


        List<Course> courses;
        long totalCourses;

        if (keyword != null && !keyword.isEmpty()) {
            courses = courseService.searchByName(keyword, page, size);
            totalCourses = courseService.countByName(keyword);
        } else {
            courses = courseService.getCourseByPage(page, size);
            totalCourses = courseService.countTotalCourse();
        }

        int totalPages = (int) Math.ceil((double) totalCourses / size);

        model.addAttribute("course", courses);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("keyword", keyword);

        return "courseManage";
    }

    // them khoa hoc
    @GetMapping("add")
    public String showAddForm(Model model) {
        model.addAttribute("courseForm", new CourseDTO());
        return "addCourse";
    }

    @PostMapping("addCourse")
    public String addCourse(@Valid @ModelAttribute("courseForm") CourseDTO courseDTO,
                            BindingResult result,
                            Model model) {

        // Check trùng tên
        if (courseRepo.existsByCourseNameIgnoreCase(courseDTO.getName())) {
            result.rejectValue("name", "error.name", "Tên khoa hoc đã tồn tại.");
        }
        if(result.hasErrors()){
            return "addCourse";
        }
        // Kiểm tra file hợp lệ
        String originalFilename = courseDTO.getFile().getOriginalFilename();
        if (originalFilename.isEmpty() || !originalFilename.matches(".*\\.(jpg|jpeg|png)$")) {
            result.rejectValue("file", "Vui lòng chọn ảnh định dạng jpg, jpeg hoặc png.");
        }

        // Nếu có lỗi thì quay lại form
        if (result.hasErrors() || model.containsAttribute("imageError")) {
            return "addCourse";
        }

        // Upload ảnh lên Cloudinary
        String imageUrl = cloudinaryService.upload(courseDTO.getFile());

        Course course = new Course();
        course.setName(courseDTO.getName());
        course.setDuration(courseDTO.getDuration());
        course.setInstructor(courseDTO.getInstructor());
        course.setImage(imageUrl);

        courseRepo.save(course);

        return "redirect:/pageCourse";
    }
    //update khoa hoc
    @GetMapping("updateCourse/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Course course = courseService.findById(id);
        if (course == null) {
            return "redirect:/pageCourse";
        }
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(course.getId());
        courseDTO.setName(course.getName());
        courseDTO.setDuration(course.getDuration());
        courseDTO.setInstructor(course.getInstructor());
        courseDTO.setImage(course.getImage());
        courseDTO.setCreateAt(course.getCreateAt());
        model.addAttribute("course", courseDTO);

        return "editCourse";
    }

    @PostMapping("/courses/update/{id}")
    public String updateCourse(@PathVariable("id") int id,
                             @Valid CourseDTO courseDto,
                             BindingResult result,
                             Model model) {

        Course existingSeed = courseService.findById(id);
        if (existingSeed == null) {
            return "redirect:/pageCourse";
        }

        // Validate tên
        if (courseDto.getName() == null || courseDto.getName().trim().isEmpty()) {
            result.rejectValue("name", "name.empty", "Tên khoa hoc không được để trống.");
        } else if (courseDto.getName().length() > 100) {
            result.rejectValue("name", "name.tooLong", "Tên khoa hoc tối đa 100 ký tự.");
        } else if (!courseDto.getName().equalsIgnoreCase(existingSeed.getName())
                && courseService.existsByCourseNameIgnoreCase(courseDto.getName())) {
            result.rejectValue("name", "name.duplicate", "Tên khoa hoc đã tồn tại.");
        }

        // Nếu có lỗi
        if (result.hasErrors()) {
            model.addAttribute("course", courseDto);
            return "editCourse";
        }
        Course course = new Course();
        // Xử lý ảnh mới nếu có
        if (!courseDto.getFile().isEmpty()) {
            String imageUrl = cloudinaryService.upload(courseDto.getFile());
            course.setImage(imageUrl);
        } else {
            course.setImage(existingSeed.getImage());
        }


        course.setId(courseDto.getId());
        course.setName(courseDto.getName());
        course.setDuration(courseDto.getDuration());
        course.setInstructor(courseDto.getInstructor());

        courseRepo.save(course);

        return "redirect:/pageCourse";
    }

    //xoa khoa hoc
    @GetMapping("/course/delete/{id}")
    public String deleteCourse(@PathVariable("id") int id) {
        courseService.deleteById(id);
        return "redirect:/pageCourse";
    }


}
