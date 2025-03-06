package com.E_Commer.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import com.E_Commer.enums.DiscountStatus;

@Entity
@Table(name = "discounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Discount {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20) // ⚡ Mã giảm giá không được trùng
    private String code;

    @Column(nullable = false) // ⚡ Phần trăm giảm giá không thể null
    private Double percentage;

    private Double maxDiscount; // Giảm giá tối đa (có thể null)

    @Column(nullable = false) // ⚡ Số lượng mã giảm giá có thể sử dụng
    private Integer quantity;

    @Column(nullable = false) // ⚡ Tránh lỗi không có ngày hết hạn
    private LocalDateTime expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) // ⚡ Tránh lỗi trạng thái không hợp lệ
    private DiscountStatus status; // ACTIVE, EXPIRED, USED

    @CreationTimestamp // ⚡ Theo dõi ngày tạo mã giảm giá
    private LocalDateTime createdAt;
}
