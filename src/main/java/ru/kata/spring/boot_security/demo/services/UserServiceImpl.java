package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }
    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }
    @Override
    @Transactional
    public boolean addUser(User user) {
        Optional<User> userOld = userRepository.findByUsername(user.getUsername());
        if (userOld.isPresent()) {
            return false;
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return true;
        }
    }
//    @Override
//    @Transactional
//    public void update(User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        userRepository.save(user);
//    }
    @Override
    @Transactional
    public boolean update(User user) {
        Optional<User> existingUserOptional = userRepository.findByUsername(user.getUsername());
        if (existingUserOptional.isPresent() && !existingUserOptional.get().getId().equals(user.getId())) {
            // Если пользователь с таким именем уже существует, и его ID не совпадает с ID обновляемого пользователя, вернуть false
            return false;
        }
        // Продолжаем обновление пользователя, если пользователь с новым именем не существует или это тот же пользователь
        userRepository.save(user);
        return true;
    }

    @Override
    public User showUser(Long id) {
        return userRepository.getById(id);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


}
