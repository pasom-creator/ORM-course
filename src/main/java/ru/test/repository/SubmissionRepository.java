package ru.test.repository;

import ru.test.model.Assignment;
import ru.test.model.Submission;
import ru.test.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с решениями заданий
 */
@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findByStudent(User student);

    List<Submission> findByAssignment(Assignment assignment);

    Optional<Submission> findByStudentAndAssignment(User student, Assignment assignment);
}

