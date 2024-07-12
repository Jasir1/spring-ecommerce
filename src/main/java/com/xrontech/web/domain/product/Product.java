package com.xrontech.web.domain.product;

import com.xrontech.web.domain.category.Category;
import com.xrontech.web.domain.model.BaseEntity;
import com.xrontech.web.domain.order.OrderStatus;
import com.xrontech.web.domain.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "\"product\"")
public class Product extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT",nullable = false)
    private String description;

    @Column(name = "price",nullable = false)
    private Double price;

    @Column(name = "qty",nullable = false)
    private Integer qty;

    @Column(name = "image_path",columnDefinition = "TEXT")
    private String imagePath;

    @Column(name = "status")
    private Boolean status = true;

    @Column(name = "category_id")
    private Long categoryId;

    @ManyToOne
    @JoinColumn(name = "category_id",referencedColumnName = "id",updatable = false,insertable = false)
    private Category category;

    @Column(name = "condition")
    @Enumerated(EnumType.STRING)
    private Condition condition = Condition.USED;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id",insertable = false,updatable = false)
    private User user;

    @Column(name = "delete")
    private Boolean delete = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}
