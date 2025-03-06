package com.E_Commer.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.E_Commer.enums.OrderTrackingStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_tracking")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private OrderTrackingStatus status; // Enum để quản lý trạng thái

    @CreationTimestamp
    private LocalDateTime createdAt; // Thời gian tạo trạng thái

    @UpdateTimestamp
    private LocalDateTime updatedAt; // Thời gian cập nhật trạng thái
}
