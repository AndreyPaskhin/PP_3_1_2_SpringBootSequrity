package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.entities.User;

import java.util.List;

public interface UserService {
    List<User> listUsers();
    User findByUsername(String username);
    boolean addUser(User user);
    User showUser(Long id);
    void deleteUser(Long id);
    void update(User user);

}
