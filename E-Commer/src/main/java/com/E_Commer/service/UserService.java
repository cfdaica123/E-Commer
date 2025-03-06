package com.E_Commer.service;

import com.E_Commer.dto.UserDTO;
import com.E_Commer.dto.UserCreateDTO;
import com.E_Commer.entity.User;
import com.E_Commer.enums.RoleEnum;
import com.E_Commer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // Inject BCrypt

    public UserDTO convertToDTO(User user) {
        return new UserDTO(
            user.getId(),
            user.getUsername(),
            user.getFullName(),
            user.getAvatarUrl(),
            user.getEmail(),
            user.getRole(),
            user.getPhoneNumber()
        );
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(UserCreateDTO userCreateDTO) {
        User user = new User();
        user.setUsername(userCreateDTO.getUsername());
        user.setFullName(userCreateDTO.getFullName());
        user.setAvatarUrl(userCreateDTO.getAvatarUrl());
        user.setEmail(userCreateDTO.getEmail());
        user.setRole(userCreateDTO.getRole() != null ? userCreateDTO.getRole() : RoleEnum.CUSTOMER);
        user.setPhoneNumber(userCreateDTO.getPhoneNumber());

        // ⚠️ Mã hóa password trước khi lưu
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));

        return userRepository.save(user);
    }

    // cách 1: cập nhật toàn bộ thông tin
    // public User updateUser(Long id, User updatedUser) {
    //     return userRepository.findById(id).map(existingUser -> {
    //         // Chỉ cập nhật nếu trường không null
    //         if (updatedUser.getPhoneNumber() != null) {
    //             existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
    //         }
    //         if (updatedUser.getAvatarUrl()!= null) {
    //             existingUser.setAvatarUrl(updatedUser.getAvatarUrl());
    //         }
    //         if (updatedUser.getEmail() != null) {
    //             existingUser.setEmail(updatedUser.getEmail());
    //         }
    //         if (updatedUser.getUsername() != null) {
    //             existingUser.setUsername(updatedUser.getUsername());
    //         }
    //         if (updatedUser.getRole() != null) {
    //             existingUser.setRole(updatedUser.getRole());
    //         }

    //         return userRepository.save(existingUser);
    //     }).orElseThrow(() -> new RuntimeException("User not found"));
    // }
    // cách 2: Dùng Java Reflection API để cập nhật
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(existingUser -> {
            try {
                for (Field field : User.class.getDeclaredFields()) {
                    field.setAccessible(true);
                    Object newValue = field.get(updatedUser);
                    if (newValue != null) {
                        field.set(existingUser, newValue);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Lỗi khi cập nhật User", e);
            }

            return userRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }


    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
