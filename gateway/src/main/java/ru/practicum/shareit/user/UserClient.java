package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.ValidationException;

@Slf4j
@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addUser(UserDto userDto) {
        validateUser(userDto);
        log.info("Добавлен новый пользователь; {}", userDto.getName());
        return post("", userDto);
    }

    public ResponseEntity<Object> updateUser(UserDto userDto) {
        if (userDto == null) {
            throw new ValidationException("Такой пользователь не существует!");
        }
        log.info("Данные пользователя обновлены: {}", userDto.getName());
        return patch("/" + userDto.getId(), userDto);
    }

    public ResponseEntity<Object> getAllUsers() {
        return get("");
    }

    public ResponseEntity<Object> getUserById(long id) {
        return get("/" + id);
    }

    public ResponseEntity<Object> deleteUser(long id) {
        return delete("/" + id);
    }


    private void validateUser(UserDto userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            throw new ValidationException("Email не может быть пустым!");
        }
        if (!userDto.getEmail().contains("@")) {
            throw new ValidationException("Email должно содержать символ @");
        }

    }

}
