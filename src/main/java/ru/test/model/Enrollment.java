package ru.test.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Запись студента на курс
 */
@Entity
@Table(name = "enrollments")
public class Enrollment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Связь многие к одному - студент
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;
    
    // Связь многие к одному - курс
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    
    private LocalDateTime enrollDate;
    
    @Enumerated(EnumType.STRING)
    private ru.test.model.EnrollmentStatus status;
    
    // Конструкторы
    public Enrollment() {
    }
    
    public Enrollment(User student, Course course) {
        this.student = student;
        this.course = course;
        this.enrollDate = LocalDateTime.now();
        this.status = EnrollmentStatus.ACTIVE;
    }
    
    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getStudent() {
        return student;
    }
    
    public void setStudent(User student) {
        this.student = student;
    }
    
    public Course getCourse() {
        return course;
    }
    
    public void setCourse(Course course) {
        this.course = course;
    }
    
    public LocalDateTime getEnrollDate() {
        return enrollDate;
    }
    
    public void setEnrollDate(LocalDateTime enrollDate) {
        this.enrollDate = enrollDate;
    }
    
    public EnrollmentStatus getStatus() {
        return status;
    }
    
    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }
}

