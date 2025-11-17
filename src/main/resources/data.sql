-- Демо данные
-- Этот файл выполняется автоматически при запуске приложения

MERGE INTO categories (id, name) KEY(id) VALUES (1, 'Программирование');
MERGE INTO categories (id, name) KEY(id) VALUES (2, 'Дизайн');
MERGE INTO categories (id, name) KEY(id) VALUES (3, 'Маркетинг');

MERGE INTO users (id, name, email, role) KEY(id) VALUES (1, 'Иван Иванов', 'ivan.ivanov@example.com', 'TEACHER');
MERGE INTO users (id, name, email, role) KEY(id) VALUES (2, 'Мария Матроскина', 'maria.matr@example.com', 'TEACHER');

MERGE INTO users (id, name, email, role) KEY(id) VALUES (3, 'Алексей Забегалов', 'alex.zabegalov@example.com', 'STUDENT');
MERGE INTO users (id, name, email, role) KEY(id) VALUES (4, 'Елена Муххад-Тандыр', 'elena.muhad@example.com', 'STUDENT');

MERGE INTO tags (id, name) KEY(id) VALUES (1, 'Java');
MERGE INTO tags (id, name) KEY(id) VALUES (2, 'Spring Boot');
MERGE INTO tags (id, name) KEY(id) VALUES (3, 'Hibernate');
MERGE INTO tags (id, name) KEY(id) VALUES (4, 'JPA');
MERGE INTO tags (id, name) KEY(id) VALUES (5, 'Beginner');