package ru.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.test.model.Assignment;
import ru.test.model.Category;
import ru.test.model.Course;
import ru.test.model.Enrollment;
import ru.test.model.EnrollmentStatus;
import ru.test.model.Lesson;
import ru.test.model.Submission;
import ru.test.model.User;
import ru.test.model.UserRole;
import ru.test.model.Module;
import ru.test.repository.*;
import ru.test.repository.LessonRepository;
import ru.test.service.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * тесты для проверки работы приложения
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class IntTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private LessonRepository lessonRepository;

    @Test
    public void testCreateUser() {
        // Создаем пользователя
        User user = userService.createUser("Петя Ф", "peter@example.com", UserRole.STUDENT);

        assertNotNull(user.getId());
        assertEquals("Петя Ф", user.getName());
        assertEquals("peter@example.com", user.getEmail());
        assertEquals(UserRole.STUDENT, user.getRole());

        // Проверяем, что пользователь сохранен в БД
        assertTrue(userRepository.existsById(user.getId()));
    }

    @Test
    public void testCreateCourse() {
        // Создаем преподавателя
        User teacher = userService.createUser("Петр Петров", "petr@example.com", UserRole.TEACHER);

        // Создаем курс
        Course course = courseService.createCourse("Java для начинающих",
                "Изучение основ Java", teacher);

        assertNotNull(course.getId());
        assertEquals("Java для начинающих", course.getTitle());
        assertEquals(teacher.getId(), course.getTeacher().getId());

        // Проверяем сохранение
        assertTrue(courseRepository.existsById(course.getId()));
    }

    @Test
    public void testEnrollStudent() {
        // Создаем студента и преподавателя
        User student = userService.createUser("Студент", "student@example.com", UserRole.STUDENT);
        User teacher = userService.createUser("Учитель", "teacher@example.com", UserRole.TEACHER);

        // Создаем курс
        Course course = courseService.createCourse("Тестовый курс", "Описание", teacher);

        // Записываем студента на курс
        Enrollment enrollment = enrollmentService.enrollStudent(student, course);

        assertNotNull(enrollment.getId());
        assertEquals(student.getId(), enrollment.getStudent().getId());
        assertEquals(course.getId(), enrollment.getCourse().getId());
        assertEquals(EnrollmentStatus.ACTIVE, enrollment.getStatus());

        // Проверяем сохранение
        assertTrue(enrollmentRepository.existsById(enrollment.getId()));
    }

    @Test
    public void testCascadeOperations() {
        // Создаем преподавателя
        User teacher = userService.createUser("Преподаватель", "teacher2@example.com", UserRole.TEACHER);

        // Создаем категорию
        Category category = new Category("Программирование");
        category = categoryRepository.save(category);

        // Создаем курс
        Course course = courseService.createCourse("Курс с модулями", "Описание", teacher);
        course.setCategory(category);
        course = courseService.updateCourse(course);

        // Создаем модуль
        Module module = new Module("Модуль 1", course);
        module.setOrderIndex(1);
        course.getModules().add(module);
        course = courseService.updateCourse(course);

        // Проверяем каскадное сохранение
        Course savedCourse = courseRepository.findById(course.getId()).orElseThrow();
        assertFalse(savedCourse.getModules().isEmpty());
        assertEquals("Модуль 1", savedCourse.getModules().get(0).getTitle());
    }

    @Test
    public void testAssignmentSubmission() {
        // Создаем студента и преподавателя
        User student = userService.createUser("Студент2", "student2@example.com", UserRole.STUDENT);
        User teacher = userService.createUser("Учитель2", "teacher3@example.com", UserRole.TEACHER);

        // Создаем курс, модуль, урок
        Course course = courseService.createCourse("Курс с заданиями", "Описание", teacher);
        Module module = new Module("Модуль", course);
        module.setOrderIndex(1);
        course.getModules().add(module);
        course = courseService.updateCourse(course);

        // Перезагружаем курс из БД, чтобы получить сохраненный модуль
        Course savedCourse = courseRepository.findById(course.getId()).orElseThrow();
        Module savedModule = savedCourse.getModules().get(0);

        // Создаем урок с сохраненным модулем
        Lesson lesson = new Lesson("Урок 1", savedModule);
        savedModule.getLessons().add(lesson);
        // Сохраняем урок явно
        lesson = lessonRepository.save(lesson);

        // Создаем задание с сохраненным уроком
        Assignment assignment = assignmentService.createAssignment(
                "Домашнее задание 1",
                "Решить задачи",
                lesson,
                LocalDateTime.now().plusDays(7),
                100
        );

        assertNotNull(assignment.getId());

        // Сдаем задание
        Submission submission = assignmentService.submitAssignment(
                assignment.getId(),
                student,
                "Мое решение задачи"
        );

        assertNotNull(submission.getId());
        assertEquals(student.getId(), submission.getStudent().getId());
        assertEquals(assignment.getId(), submission.getAssignment().getId());
    }

    @Test
    public void testLazyLoading() {
        // Создаем преподавателя и курс
        User teacher = userService.createUser("Учитель3", "teacher4@example.com", UserRole.TEACHER);
        Course course = courseService.createCourse("Тест ленивой загрузки", "Описание", teacher);

        // Получаем курс из БД
        Course loadedCourse = courseRepository.findById(course.getId()).orElseThrow();

        // Пытаемся получить модули (ленивая загрузка)
        // Это должно работать внутри транзакции
        List<ru.test.model.Module> modules = loadedCourse.getModules();
        assertNotNull(modules);
        // Коллекция инициализируется, но может быть пустой
    }

    @Test
    public void testCategoryAndCourse() {
        // Создаем категорию
        Category category = categoryRepository.save(new Category("Дизайн"));

        // Создаем преподавателя
        User teacher = userService.createUser("Дизайнер", "designer@example.com", UserRole.TEACHER);

        // Создаем курс в категории
        Course course = courseService.createCourse("Курс дизайна", "Описание", teacher);
        course.setCategory(category);
        course = courseService.updateCourse(course);

        // Проверяем связь
        Course savedCourse = courseRepository.findById(course.getId()).orElseThrow();
        assertNotNull(savedCourse.getCategory());
        assertEquals("Дизайн", savedCourse.getCategory().getName());
    }

    @Test
    public void testDeleteCourse() {
        // Создаем преподавателя и курс
        User teacher = userService.createUser("Учитель4", "teacher5@example.com", UserRole.TEACHER);
        Course course = courseService.createCourse("Курс для удаления", "Описание", teacher);
        Long courseId = course.getId();

        // Удаляем курс
        courseService.deleteCourse(courseId);

        // Проверяем, что курс удален
        assertFalse(courseRepository.existsById(courseId));
    }
}
