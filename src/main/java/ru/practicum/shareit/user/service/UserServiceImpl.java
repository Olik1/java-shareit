package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Override
    public UserDto addUser(UserDto userDto) {
        /*
        добавляем пользователя в приложение:
преобразуем дто в юзера
проверяем почту и юзера (взять из прошлого проекта - имя пароль и тп)
преобразуем в дто обект маппером и добавляем нового пользователя в мапу и возвращаем  дто обьект с инфо по добавленному юзеру
         */
        User user = UserMapper.toUser(userDto);
        validateUser(user);
        if (emailIsExist(user)) {
            throw new ConflictException("Такой email уже существует");
        }
        log.info("Добавлен новый пользователь; {}", user.getName());
        return UserMapper.toUserDto(userDao.addUser(user));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User newUser = UserMapper.toUser(userDto);
        User user = userDao.getUserById(newUser.getId());
        if (user == null) {
            throw new ValidationException("Такой пользователь не существует!");
        }

        if (newUser.getEmail() == null) {

            newUser.setEmail(user.getEmail());
            //            user.setEmail(newUser.getEmail());
        }
        if (newUser.getName() == null) {
            newUser.setName(user.getName());
        }

        validateUser(newUser);
        if (emailIsExist(newUser)) {
            throw new ConflictException("Такой email уже существует");
        }
//        if (!isUserExists(user.getId())) {
//            throw new ValidationException("Такой пользователь не существует!");
//        }
//        if (emailIsExist(user)) {
//            throw new ConflictException("Такой email уже существует");
//        }
        log.info("Данные пользователя обновлены: {}", newUser.getName());
        return UserMapper.toUserDto(userDao.updateUser(newUser));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userDao.getAllUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(long id) {
        isUserExists(id);
        userDao.deleteUser(id);

    }

    @Override

    public UserDto getUserById(long id) {
        isUserExists(id);
        User user = userDao.getUserById(id);
        validateUser(user);
        return UserMapper.toUserDto(user);
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ValidationException("Email не может быть пустым!");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Email должно содержать символ @");
        }
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
    }

    private boolean isUserExists(long userId) {
        boolean isExist = false;
        List<User> userList = userDao.getAllUsers();
        for (User user : userList) {
            if (Objects.equals(user.getId(), userId)) {
                isExist = true;
            }
        }
        return isExist;

    }

    private boolean emailIsExist(User user) {
        String email = user.getEmail();
        List<User> userList = userDao.getAllUsers();
        for (User user1 : userList) {
            if (user1.getEmail().contains(email)) { //проверка совпадений почты
                if (!Objects.equals(user1.getId(), user.getId())) { // проверка тот же этот ли пользователь
                    log.info("Пользователь с такой почтой уже существует!");
                    return true;
                }

            }
        }
        return false;
    }

}
