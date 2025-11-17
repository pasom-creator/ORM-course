package ru.test.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Пользователь системы - может быть студентом, преподавателем или администратором
 */
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Column(nullable = false)
    private String name;
    
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email должен быть валидным")
    @Column(nullable = false, unique = true)
    private String email;
    
    @NotNull(message = "Роль пользователя обязательна")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ru.test.model.UserRole role;
    
    // Связь один к одному с профилем (ленивая загрузка)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Profile profile;
    
    // Связь один ко многим - курсы, которые ведет преподаватель
    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    private List<Course> coursesTaught = new ArrayList<>();
    
    // Связь один ко многим - записи студента на курсы
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<Enrollment> enrollments = new ArrayList<>();
    
    // Связь один ко многим - отправленные решения заданий
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<Submission> submissions = new ArrayList<>();
    
    // Связь один ко многим - результаты тестов
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<QuizSubmission> quizSubmissions = new ArrayList<>();
    
    // Связь один ко многим - отзывы на курсы
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<CourseReview> reviews = new ArrayList<>();
    
    // Конструкторы
    public User() {
    }
    
    public User(String name, String email, ru.test.model.UserRole role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }
    
    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public ru.test.model.UserRole getRole() {
        return role;
    }
    
    public void setRole(ru.test.model.UserRole role) {
        this.role = role;
    }
    
    public Profile getProfile() {
        return profile;
    }
    
    public void setProfile(Profile profile) {
        this.profile = profile;
    }
    
    public List<Course> getCoursesTaught() {
        return coursesTaught;
    }
    
    public void setCoursesTaught(List<Course> coursesTaught) {
        this.coursesTaught = coursesTaught;
    }
    
    public List<Enrollment> getEnrollments() {
        return enrollments;
    }
    
    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }
    
    public List<Submission> getSubmissions() {
        return submissions;
    }
    
    public void setSubmissions(List<Submission> submissions) {
        this.submissions = submissions;
    }
    
    public List<QuizSubmission> getQuizSubmissions() {
        return quizSubmissions;
    }
    
    public void setQuizSubmissions(List<QuizSubmission> quizSubmissions) {
        this.quizSubmissions = quizSubmissions;
    }
    
    public List<CourseReview> getReviews() {
        return reviews;
    }
    
    public void setReviews(List<CourseReview> reviews) {
        this.reviews = reviews;
    }
}

