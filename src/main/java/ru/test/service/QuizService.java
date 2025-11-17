package ru.test.service;

import ru.test.model.Quiz;
import ru.test.model.QuizSubmission;
import ru.test.model.User;
import ru.test.repository.QuizRepository;
import ru.test.repository.QuizSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с тестами
 */
@Service
@Transactional
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizSubmissionRepository quizSubmissionRepository;

    // Создать тест
    public Quiz createQuiz(String title, Integer timeLimit) {
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setTimeLimit(timeLimit);
        return quizRepository.save(quiz);
    }

    @Transactional(readOnly = true)
    public Optional<Quiz> getQuizById(Long id) {
        return quizRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public QuizSubmission submitQuiz(Long quizId, User student, Integer score) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Тест не найден"));

        QuizSubmission submission = new QuizSubmission(quiz, student, score);
        return quizSubmissionRepository.save(submission);
    }

    @Transactional(readOnly = true)
    public List<QuizSubmission> getQuizSubmissions(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Тест не найден"));
        return quizSubmissionRepository.findByQuiz(quiz);
    }

    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }
}

