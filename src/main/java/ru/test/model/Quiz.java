package ru.test.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Тест/викторина для модуля
 */
@Entity
@Table(name = "quizzes")
public class Quiz {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Связь один к одному - модуль
    @OneToOne
    @JoinColumn(name = "module_id", nullable = false, unique = true)
    private Module module;
    
    @Column(nullable = false)
    private String title;
    
    private Integer timeLimit;  // Ограничение времени в минутах
    
    // Связь один ко многим - вопросы теста
    @OneToMany(mappedBy = "quiz", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();
    
    // Связь один ко многим - результаты прохождения
    @OneToMany(mappedBy = "quiz", fetch = FetchType.LAZY)
    private List<ru.test.model.QuizSubmission> quizSubmissions = new ArrayList<>();
    
    // Конструкторы
    public Quiz() {
    }
    
    public Quiz(String title, Module module) {
        this.title = title;
        this.module = module;
    }
    
    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Module getModule() {
        return module;
    }
    
    public void setModule(Module module) {
        this.module = module;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Integer getTimeLimit() {
        return timeLimit;
    }
    
    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }
    
    public List<Question> getQuestions() {
        return questions;
    }
    
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
    
    public List<QuizSubmission> getQuizSubmissions() {
        return quizSubmissions;
    }
    
    public void setQuizSubmissions(List<QuizSubmission> quizSubmissions) {
        this.quizSubmissions = quizSubmissions;
    }
}

