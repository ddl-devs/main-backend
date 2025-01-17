package br.com.ifrn.ddldevs.pets_backend.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PetAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @Column(nullable = false)
    private String picture;

    @Column(nullable = false)
    private String result;

    @Column(nullable = false)
    private String analysisType;
}