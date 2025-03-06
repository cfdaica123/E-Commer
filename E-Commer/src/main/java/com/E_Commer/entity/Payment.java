package com.E_Commer.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import com.E_Commer.enums.PaymentStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status; // PENDING, SUCCESS, FAILED

    @Column(nullable = false, length = 50)
    private String paymentMethod; // PayPal, Credit Card, VNPay...

    @Column(unique = true)
    private String transactionId; // Mã giao dịch (có thể null nếu chưa thanh toán)

    @CreationTimestamp
    private LocalDateTime createdAt; // Thời điểm thanh toán
}
