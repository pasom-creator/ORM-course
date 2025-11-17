package ru.test.repository;

import ru.test.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с уроками
 */
@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    // Базовые методы CRUD наследуются от JpaRepository
}

