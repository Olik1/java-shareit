package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDao {

    User addUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    void deleteUsers();

    void deleteUser(long id);

    User getUserById(long id);

}
