package ru.practicum.shareit.validation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ValidationException;

@Slf4j
public class Validation {
    public static void checkIfNoBlank(String str, String parameterName) {
        if (str.isBlank()) {
            log.warn("{} не может быть пустым", parameterName);
            throw new ValidationException(String.format("%s не может быть пустым", parameterName));
        }
    }
}
