package ru.test.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Учебный курс
 */
@Entity
@Table(name = "courses")
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private Integer duration;  // Длительность в часах
    
    private LocalDate startDate;
    
    // Связь многие к одному - категория курса
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    
    // Связь многие к одному - преподаватель
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;
    
    // Связь один ко многим - модули курса
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Module> modules = new ArrayList<>();
    
    // Связь один ко многим - записи студентов
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<ru.test.model.Enrollment> enrollments = new ArrayList<>();
    
    // Связь один ко многим - отзывы
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<ru.test.model.CourseReview> reviews = new ArrayList<>();
    
    // Связь многие ко многим - теги
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "course_tag",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();
    
    // Конструкторы
    public Course() {
    }
    
    public Course(String title, String description, User teacher) {
        this.title = title;
        this.description = description;
        this.teacher = teacher;
    }
    
    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public Integer getDuration() {
        return duration;
    }
    
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
    
    public User getTeacher() {
        return teacher;
    }
    
    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }
    
    public List<Module> getModules() {
        return modules;
    }
    
    public void setModules(List<Module> modules) {
        this.modules = modules;
    }
    
    public List<ru.test.model.Enrollment> getEnrollments() {
        return enrollments;
    }
    
    public void setEnrollments(List<ru.test.model.Enrollment> enrollments) {
        this.enrollments = enrollments;
    }
    
    public List<ru.test.model.CourseReview> getReviews() {
        return reviews;
    }
    
    public void setReviews(List<ru.test.model.CourseReview> reviews) {
        this.reviews = reviews;
    }
    
    public Set<Tag> getTags() {
        return tags;
    }
    
    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
}

