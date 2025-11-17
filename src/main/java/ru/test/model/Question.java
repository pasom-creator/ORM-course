package ru.test.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Вопрос теста
 */
@Entity
@Table(name = "questions")
public class Question {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Связь многие к одному - тест
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private ru.test.model.Quiz quiz;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;  // Текст вопроса
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ru.test.model.QuestionType type;
    
    // Связь один ко многим - варианты ответа
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AnswerOption> options = new ArrayList<>();
    
    // Конструкторы
    public Question() {
    }
    
    public Question(String text, ru.test.model.QuestionType type, Quiz quiz) {
        this.text = text;
        this.type = type;
        this.quiz = quiz;
    }
    
    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Quiz getQuiz() {
        return quiz;
    }
    
    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public QuestionType getType() {
        return type;
    }
    
    public void setType(QuestionType type) {
        this.type = type;
    }
    
    public List<AnswerOption> getOptions() {
        return options;
    }
    
    public void setOptions(List<AnswerOption> options) {
        this.options = options;
    }
}

