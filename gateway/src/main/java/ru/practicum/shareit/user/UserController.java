package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validation.ValidationGroups;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(ValidationGroups.Create.class)
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserDto user) {
        log.info("Добавлен пользователь: {}", user);
        return userClient.addUser(user);
    }
//    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.CREATED)
//    @Validated(Create.class)
//    public ResponseEntity<Object> add(@Valid @RequestBody UserDto userDto) {
//        return userClient.addUser(userDto);
//    }

    @PatchMapping("/{userId}")
    @Validated(ValidationGroups.Update.class)
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
