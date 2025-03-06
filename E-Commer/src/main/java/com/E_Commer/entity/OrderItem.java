package com.E_Commer.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // ⚡ Tránh load dữ liệu không cần thiết
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // ⚡ Tối ưu hiệu suất
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2) // ⚡ Đảm bảo giá tiền chính xác
    private BigDecimal unitPrice;

    @CreationTimestamp
    private LocalDateTime createdAt; // ⚡ Theo dõi thời gian tạo đơn hàng

    @UpdateTimestamp
    private LocalDateTime updatedAt; // ⚡ Theo dõi khi đơn hàng được cập nhật

    @PrePersist
    @PreUpdate
    private void validateQuantityAndPrice() {
        if (quantity == null || quantity < 1) {
            quantity = 1; // Nếu bị null hoặc nhỏ hơn 1 thì mặc định là 1
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) { // ⚡ Sửa lỗi so sánh BigDecimal
            throw new IllegalArgumentException("Unit price must be greater than or equal to 0.");
        }
    }
}
