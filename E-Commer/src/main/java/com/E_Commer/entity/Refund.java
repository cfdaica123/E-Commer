package com.E_Commer.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.E_Commer.enums.RefundStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "refunds")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(nullable = false)
    private Double amount; // Số tiền hoàn lại

    @Column(nullable = false, length = 255)
    private String reason; // Lý do hoàn tiền

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundStatus status; // PENDING, APPROVED, REJECTED

    @CreationTimestamp
    private LocalDateTime createdAt; // Thời gian yêu cầu hoàn tiền

    @UpdateTimestamp
    private LocalDateTime updatedAt; // Thời gian cập nhật trạng thái
}
