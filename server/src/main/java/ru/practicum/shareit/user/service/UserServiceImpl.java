package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        log.info("Добавлен новый пользователь; {}", user.getName());
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User newUser = UserMapper.toUser(userDto);
        var user = userRepository.findById(newUser.getId()).get();
        if (newUser.getEmail() != null) {
            user.setEmail(newUser.getEmail());
        }
        if (newUser.getName() != null) {
            user.setName(newUser.getName());
        }

        log.info("Данные пользователя обновлены: {}", user.getName());
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Transactional
    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(long id) {
        if (!isUserExists(id)) {
            throw new ObjectNotFoundException("Пользователя не существует!");
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDto getUserById(long id) {
        if (!isUserExists(id)) {
            throw new ObjectNotFoundException("Пользователя не существует!");
        }
        User user = userRepository.findById(id).get();
        return UserMapper.toUserDto(user);
    }


    public boolean isUserExists(long userId) {
        var userOptional = userRepository.findById(userId);
        return !userOptional.isEmpty();

    }

    private boolean emailIsExist(User user) {
        String email = user.getEmail();
        List<User> userList = userRepository.findAll();
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
