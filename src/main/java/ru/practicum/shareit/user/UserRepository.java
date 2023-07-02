package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;
//@Repository
public interface UserRepository extends JpaRepository<User,Long> {

}
