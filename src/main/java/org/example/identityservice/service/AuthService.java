package org.example.identityservice.service;

import org.example.identityservice.entity.User;
import org.example.identityservice.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String username, String password) {

        // Kiểm tra thông tin đăng ký
        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Username và password không được để trống"
            );
        }

        // Kiểm tra username trùng
        if (userRepository.existsByUsername(username)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Username đã tồn tại"
            );
        }

        // Tạo User
        User user = new User();

        user.setId(UUID.randomUUID().toString());
        user.setUsername(username);

        // Mã hóa mật khẩu trước khi lưu
        user.setPassword(passwordEncoder.encode(password));

        // Vai trò mặc định
        user.setRole("USER");

        // Lưu PostgreSQL
        return userRepository.save(user);
    }
}
