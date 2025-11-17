package ru.test.controller;

import ru.test.model.Course;
import ru.test.service.CourseService;
import ru.test.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST контроллер для работы с курсами
 */
@RestController
@RequestMapping("/api/courses")
@Tag(name = "Courses", description = "API для управления учебными курсами")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Operation(
            summary = "Создать новый курс",
            description = "Создает новый учебный курс с указанным преподавателем"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Курс успешно создан"),
            @ApiResponse(responseCode = "400", description = "Невалидные данные или преподаватель не найден")
    })
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody CreateCourseRequest request) {
        try {
            Course course = courseService.createCourse(
                    request.getTitle(),
                    request.getDescription(),
                    request.getTeacherId() != null ?
                            userService.getUserById(request.getTeacherId())
                                    .orElseThrow(() -> new RuntimeException("Преподаватель не найден")) : null
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(course);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(
            summary = "Получить курс по ID",
            description = "Возвращает информацию о курсе по его идентификатору"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Курс найден"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(
            @Parameter(description = "ID курса", required = true) @PathVariable Long id) {
        return courseService.getCourseById(id)
                .map(course -> ResponseEntity.ok(course))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Получить все курсы",
            description = "Возвращает список всех курсов в системе"
    )
    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @Operation(
            summary = "Поиск курсов по названию",
            description = "Возвращает список курсов, название которых содержит указанный текст"
    )
    @GetMapping("/search")
    public List<Course> searchCourses(
            @Parameter(description = "Текст для поиска в названии курса", required = true)
            @RequestParam String title) {
        return courseService.searchCoursesByTitle(title);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        course.setId(id);
        Course updated = courseService.updateCourse(course);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    public static class CreateCourseRequest {
        private String title;
        private String description;
        private Long teacherId;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Long getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(Long teacherId) {
            this.teacherId = teacherId;
        }
    }
}
