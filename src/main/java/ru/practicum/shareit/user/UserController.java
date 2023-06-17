package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
//@RequestMapping(path = "/users")
@RequestMapping("/users")
@Slf4j
@AllArgsConstructor
public class UserController {
    UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@Valid @RequestBody UserDto user) {
        log.info("Добавлен пользователь: {}", user);
        return userService.addUser(user);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsers() {
        return userService.getAllUsers();
    }
//    @PutMapping
//    @ResponseStatus(HttpStatus.OK)
//    public UserDto updateUser(UserDto user) {
//        log.info("Обновление данных пользователя: {}", user);
//        return userService.updateUser(user);
//    }
    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser( @RequestBody UserDto user, @PathVariable Long userId) {
        log.info("Обновление данных пользователя c id: {}", userId);
        user.setId(userId);
        return userService.updateUser(user);
    }
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable int userId) {
        log.info("Пользователь был удален из списка по id: {}", userId);
        userService.deleteUser(userId);
    }
}
