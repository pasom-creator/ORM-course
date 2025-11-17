package ru.test.repository;

import ru.test.model.Course;
import ru.test.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с курсами
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByTeacher(User teacher);

    List<Course> findByCategoryId(Long categoryId);

    List<Course> findByTitleContainingIgnoreCase(String title);
}

