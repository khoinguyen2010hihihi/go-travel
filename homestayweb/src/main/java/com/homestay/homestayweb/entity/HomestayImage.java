package com.homestay.homestayweb.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "homestay_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomestayImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(name = "homestay_id", nullable = false)
    private Long homestayId;

    @Column(name = "image_url", nullable = false, length = 512)
    private String imageUrl;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;
}
