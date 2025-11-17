package ru.test;

import ru.test.model.Enrollment;
import ru.test.model.EnrollmentStatus;
import ru.test.service.CourseService;
import ru.test.service.EnrollmentService;
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
 * REST контроллер для работы с записями на курсы
 */
@RestController
@RequestMapping("/api/enrollments")
@Tag(name = "Enrollments", description = "API для управления записями студентов на курсы")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    // Записать студента на курс
    @Operation(
            summary = "Записать студента на курс",
            description = "Создает новую запись студента на курс. Проверяет уникальность записи."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Студент успешно записан на курс"),
            @ApiResponse(responseCode = "400", description = "Студент уже записан на этот курс или данные невалидны")
    })
    @PostMapping
    public ResponseEntity<Enrollment> enrollStudent(@RequestBody EnrollRequest request) {
        try {
            Enrollment enrollment = enrollmentService.enrollStudent(
                    userService.getUserById(request.getStudentId())
                            .orElseThrow(() -> new RuntimeException("Студент не найден")),
                    courseService.getCourseById(request.getCourseId())
                            .orElseThrow(() -> new RuntimeException("Курс не найден"))
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(
            summary = "Получить запись по ID",
            description = "Возвращает информацию о записи на курс по идентификатору"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запись найдена"),
            @ApiResponse(responseCode = "404", description = "Запись не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> getEnrollmentById(
            @Parameter(description = "ID записи", required = true) @PathVariable Long id) {
        return enrollmentService.getEnrollmentById(id)
                .map(enrollment -> ResponseEntity.ok(enrollment))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Получить все записи студента",
            description = "Возвращает список всех курсов, на которые записан указанный студент"
    )
    @GetMapping("/student/{studentId}")
    public List<Enrollment> getEnrollmentsByStudent(
            @Parameter(description = "ID студента", required = true) @PathVariable Long studentId) {
        return userService.getUserById(studentId)
                .map(enrollmentService::getEnrollmentsByStudent)
                .orElse(List.of());
    }

    @Operation(
            summary = "Изменить статус записи",
            description = "Обновляет статус записи на курс (ACTIVE, COMPLETED, DROPPED)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Невалидный статус или запись не найдена")
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<Enrollment> updateStatus(
            @Parameter(description = "ID записи", required = true) @PathVariable Long id,
            @Parameter(description = "Новый статус (ACTIVE, COMPLETED, DROPPED)", required = true)
            @RequestParam String status) {
        try {
            EnrollmentStatus enrollmentStatus = EnrollmentStatus.valueOf(status.toUpperCase());
            Enrollment updated = enrollmentService.updateEnrollmentStatus(id, enrollmentStatus);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Внутренний класс для запроса записи на курс
    public static class EnrollRequest {
        private Long studentId;
        private Long courseId;

        public Long getStudentId() {
            return studentId;
        }

        public void setStudentId(Long studentId) {
            this.studentId = studentId;
        }

        public Long getCourseId() {
            return courseId;
        }

        public void setCourseId(Long courseId) {
            this.courseId = courseId;
        }
    }
}
