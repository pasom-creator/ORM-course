package ru.test.service;

import ru.test.model.Assignment;
import ru.test.model.Lesson;
import ru.test.model.Submission;
import ru.test.model.User;
import ru.test.repository.AssignmentRepository;
import ru.test.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с заданиями
 */
@Service
@Transactional
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    public Assignment createAssignment(String title, String description, Lesson lesson,
                                       LocalDateTime dueDate, Integer maxScore) {
        Assignment assignment = new Assignment(title, description, lesson);
        assignment.setDueDate(dueDate);
        assignment.setMaxScore(maxScore);
        return assignmentRepository.save(assignment);
    }

    @Transactional(readOnly = true)
    public Optional<Assignment> getAssignmentById(Long id) {
        return assignmentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Assignment> getAssignmentsByLesson(Lesson lesson) {
        return assignmentRepository.findByLesson(lesson);
    }

    public Submission submitAssignment(Long assignmentId, User student, String content) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Задание не найдено"));

        Optional<Submission> existingSubmission = submissionRepository
                .findByStudentAndAssignment(student, assignment);
        if (existingSubmission.isPresent()) {
            throw new RuntimeException("Студент уже сдал это задание");
        }

        Submission submission = new Submission(assignment, student, content);
        return submissionRepository.save(submission);
    }

    public Submission gradeSubmission(Long submissionId, Integer score, String feedback) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Решение не найдено"));

        submission.setScore(score);
        submission.setFeedback(feedback);
        return submissionRepository.save(submission);
    }

    @Transactional(readOnly = true)
    public List<Submission> getSubmissionsByAssignment(Assignment assignment) {
        return submissionRepository.findByAssignment(assignment);
    }

    public void deleteAssignment(Long id) {
        assignmentRepository.deleteById(id);
    }
}

