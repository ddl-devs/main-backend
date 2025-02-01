package br.com.ifrn.ddldevs.pets_backend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recommendations")
public class Recommendation extends BaseEntity {
    @Lob
    @Column(nullable = false)
    private String recommendation;

    @Column(nullable = false, length = 64, name = "category_recommendation")
    private String categoryRecommendation;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;
}