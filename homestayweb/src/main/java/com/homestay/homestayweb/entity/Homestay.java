package com.homestay.homestayweb.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Homestay")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Homestay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "homestay_id")
    private Long homestayID;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 255)
    private String street;

    @Column(nullable = false, length = 255)
    private String ward;

    @Column(nullable = false, length = 255)
    private String district;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(precision = 3, scale = 2)
    private BigDecimal surfRating;

    @Column(length = 50)
    private String approveStatus;

    private Long approvedBy;

    @Column(length = 255)
    private String contactInfo;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
