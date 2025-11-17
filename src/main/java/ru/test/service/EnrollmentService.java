package ru.test.service;

import ru.test.model.Course;
import ru.test.model.Enrollment;
import ru.test.model.EnrollmentStatus;
import ru.test.model.User;
import ru.test.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с записями на курсы
 */
@Service
@Transactional
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    // Записать студента на курс
    public Enrollment enrollStudent(User student, Course course) {
        // Проверяем, не записан ли уже студент на этот курс
        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            throw new RuntimeException("Студент уже записан на этот курс");
        }

        Enrollment enrollment = new Enrollment(student, course);
        return enrollmentRepository.save(enrollment);
    }

    @Transactional(readOnly = true)
    public Optional<Enrollment> getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollmentsByStudent(User student) {
        return enrollmentRepository.findByStudent(student);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollmentsByCourse(Course course) {
        return enrollmentRepository.findByCourse(course);
    }

    public Enrollment updateEnrollmentStatus(Long enrollmentId, EnrollmentStatus status) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Запись не найдена"));
        enrollment.setStatus(status);
        return enrollmentRepository.save(enrollment);
    }

    public void dropStudent(Long enrollmentId) {
        updateEnrollmentStatus(enrollmentId, EnrollmentStatus.DROPPED);
    }

    public void deleteEnrollment(Long id) {
        enrollmentRepository.deleteById(id);
    }
}

