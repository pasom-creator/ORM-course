package ru.test.controller;

import ru.test.model.User;
import ru.test.model.UserRole;
import ru.test.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST контроллер для работы с пользователями
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "API для управления пользователями (студенты, преподаватели, администраторы)")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(
            summary = "Создать нового пользователя",
            description = "Создает нового пользователя (студента, преподавателя или администратора)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Невалидные данные или пользователь с таким email уже существует")
    })
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserRequest request) {
        try {
            User user = userService.createUser(request.getName(), request.getEmail(), request.getRole());
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(
            summary = "Получить пользователя по ID",
            description = "Возвращает информацию о пользователе по его идентификатору"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "ID пользователя", required = true) @PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Получить всех пользователей",
            description = "Возвращает список всех пользователей системы"
    )
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(
            summary = "Получить пользователей по роли",
            description = "Возвращает список пользователей с указанной ролью (STUDENT, TEACHER, ADMIN)"
    )
    @GetMapping("/role/{role}")
    public List<User> getUsersByRole(
            @Parameter(description = "Роль пользователя (STUDENT, TEACHER, ADMIN)", required = true)
            @PathVariable String role) {
        UserRole userRole = UserRole.valueOf(role.toUpperCase());
        return userService.getUsersByRole(userRole);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        User updated = userService.updateUser(user);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    public static class CreateUserRequest {
        @jakarta.validation.constraints.NotBlank(message = "Имя не может быть пустым")
        private String name;

        @jakarta.validation.constraints.NotBlank(message = "Email не может быть пустым")
        @jakarta.validation.constraints.Email(message = "Email должен быть валидным")
        private String email;

        @jakarta.validation.constraints.NotNull(message = "Роль обязательна")
        private UserRole role;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public UserRole getRole() {
            return role;
        }

        public void setRole(UserRole role) {
            this.role = role;
        }
    }
}
