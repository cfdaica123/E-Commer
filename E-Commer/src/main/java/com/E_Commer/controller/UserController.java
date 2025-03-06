package com.E_Commer.controller;

import com.E_Commer.dto.UserCreateDTO;
import com.E_Commer.dto.UserDTO;
import com.E_Commer.entity.User;
import com.E_Commer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    // 1️⃣ Lấy danh sách tất cả User
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            List<UserDTO> userDTOs = users.stream().map(userService::convertToDTO).collect(Collectors.toList());
            return ResponseEntity.ok(userDTOs);
        } catch (Exception ex) {
            logger.error("Lỗi khi lấy danh sách User: ", ex);
            throw new RuntimeException("Không thể lấy danh sách User.");
        }
    }

    // 2️⃣ Lấy User theo ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        try {
            Optional<User> user = userService.getUserById(id);
            return user.map(value -> ResponseEntity.ok(userService.convertToDTO(value)))
                    .orElseGet(() -> {
                        logger.warn("Không tìm thấy User với ID: " + id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception ex) {
            logger.error("Lỗi khi lấy User theo ID: ", ex);
            throw new RuntimeException("Lỗi khi lấy thông tin User.");
        }
    }

    // 3️⃣ Tạo User mới (Đã sửa lỗi)
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        if (userCreateDTO.getPassword() == null || userCreateDTO.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Password không được để trống!");
        }

        // Gọi service để lưu User (service đã mã hóa mật khẩu)
        User savedUser = userService.saveUser(userCreateDTO);
        return ResponseEntity.ok(savedUser);
    }

    // 4️⃣ Cập nhật User
    //cách 1: cập nhật từng trường
    // @PutMapping("/{id}")
    // public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
    //     try {
    //         Optional<User> existingUserOpt = userService.getUserById(id);
    //         if (existingUserOpt.isEmpty()) {
    //             logger.warn("Không tìm thấy User với ID: " + id);
    //             return ResponseEntity.notFound().build();
    //         }

    //         User existingUser = existingUserOpt.get();

    //         // Cập nhật từng trường nếu nó không null
    //         if (userDTO.getAvatarUrl() != null) {
    //             existingUser.setAvatarUrl(userDTO.getAvatarUrl());
    //         }
    //         if (userDTO.getPhoneNumber() != null) {
    //             existingUser.setPhoneNumber(userDTO.getPhoneNumber());
    //         }
    //         if (userDTO.getEmail() != null) {
    //             existingUser.setEmail(userDTO.getEmail());
    //         }
    //         if (userDTO.getUsername() != null) {
    //             existingUser.setUsername(userDTO.getUsername());
    //         }
    //         if (userDTO.getRole() != null) {
    //             existingUser.setRole(userDTO.getRole());
    //         }

    //         // Gọi service để lưu lại User đã cập nhật
    //         User updatedUser = userService.updateUser(id, existingUser);

    //         return ResponseEntity.ok(userService.convertToDTO(updatedUser));
    //     } catch (Exception ex) {
    //         logger.error("Lỗi khi cập nhật User: ", ex);
    //         throw new RuntimeException("Lỗi khi cập nhật User.");
    //     }
    // }
    //cách 2: Dùng Reflection API để cập nhật
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        try {
            Optional<User> existingUserOpt = userService.getUserById(id);
            if (existingUserOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            User existingUser = existingUserOpt.get();

            // Dùng Java Reflection để cập nhật các field không null
            for (Field field : UserDTO.class.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(userDTO);
                if (value != null) {
                    Field userField = User.class.getDeclaredField(field.getName());
                    userField.setAccessible(true);
                    userField.set(existingUser, value);
                }
            }

            User updatedUser = userService.updateUser(id, existingUser);
            return ResponseEntity.ok(userService.convertToDTO(updatedUser));
        } catch (Exception ex) {
            logger.error("Lỗi khi cập nhật User: ", ex);
            throw new RuntimeException("Lỗi khi cập nhật User.");
        }
    }


    // 5️⃣ Xóa User
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            if (userService.deleteUser(id)) {
                return ResponseEntity.noContent().build();
            }
            logger.warn("Không tìm thấy User để xóa, ID: " + id);
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            logger.error("Lỗi khi xóa User: ", ex);
            throw new RuntimeException("Lỗi khi xóa User.");
        }
    }
}
