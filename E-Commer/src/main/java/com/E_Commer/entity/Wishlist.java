package com.E_Commer.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "wishlist", 
       uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "product_id"}) }) // ⚡ Tránh trùng lặp
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Wishlist {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // ⚡ Tối ưu hiệu suất
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // ⚡ Tối ưu hiệu suất
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @CreationTimestamp
    private LocalDateTime createdAt; // ⏳ Theo dõi thời điểm thêm vào wishlist
}
