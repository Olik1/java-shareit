package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.CommentDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.*;
import java.util.List;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addUser(@NotNull @NotBlank @Valid @RequestBody UserDto user) {
        log.info("Добавлен пользователь: {}", user);
        return userClient.addUser(user);
    }


    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateUser(@RequestBody @Valid UserDto user, @PathVariable Long userId) {
        log.info("Обновление данных пользователя c id: {}", userId);
        user.setId(userId);
        return userClient.updateUser(user);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getUsers() {
        return userClient.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable long id) {
        return userClient.getUserById(id);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> deleteUser(@PathVariable int userId) {
        log.info("Пользователь был удален из списка по id: {}", userId);
        return userClient.deleteUser(userId);
    }
}
