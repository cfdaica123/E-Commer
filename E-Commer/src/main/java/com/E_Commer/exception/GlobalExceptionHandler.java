package com.E_Commer.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 1️⃣ Bắt lỗi chung (Exception)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        logger.error("Lỗi hệ thống: ", ex);

        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", "Lỗi hệ thống");
        errorDetails.put("message", ex.getMessage()); // Lấy message của lỗi
        errorDetails.put("cause", (ex.getCause() != null) ? ex.getCause().toString() : "Không rõ nguyên nhân" + ex.getCause());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
    }

    // 2️⃣ Bắt lỗi cụ thể (ví dụ: NullPointerException)
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Map<String, String>> handleNullPointerException(NullPointerException ex) {
        logger.error("NullPointerException: ", ex);

        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", "NullPointerException" + ex.getClass().getSimpleName());
        errorDetails.put("message", ex.getMessage() + ": " + ex.getStackTrace()[0].toString());
        errorDetails.put("cause", "Có thể một giá trị bị null" + ex.getStackTrace()[0].toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }
}
