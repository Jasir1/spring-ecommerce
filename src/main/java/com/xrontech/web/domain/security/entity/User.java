package com.xrontech.web.domain.security.entity;

import com.xrontech.web.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "\"User\"")
public class User extends BaseEntity {
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

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "primary_address", columnDefinition = "TEXT")
    private String primaryAddress;

    @Column(name = "secondary_address", columnDefinition = "TEXT")
    private String secondaryAddress;

    @Column(name = "image_url")
    private String imageURL;

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole = UserRole.USER;

    @Column(name = "status", nullable = false)
    private Boolean status = true;

    @Column(name = "delete", nullable = false)
    private Boolean delete = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}
