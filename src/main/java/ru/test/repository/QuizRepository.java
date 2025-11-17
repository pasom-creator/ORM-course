package ru.test.repository;

import ru.test.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с тестами
 */
@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    // Базовые методы CRUD наследуются от JpaRepository
}

