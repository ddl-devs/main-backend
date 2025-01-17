package br.com.ifrn.ddldevs.pets_backend.domain;


import br.com.ifrn.ddldevs.pets_backend.domain.Enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Integer age;

    private String breed;

    @Column(nullable = false)
    private String species;

    @Positive(message = "Peso deve ser maior que zero")
    private Double weight;

    @Positive(message = "Altura (Cm) deve ser maior que zero")
    private Integer height;

    @PastOrPresent
    private LocalDate dateOfBirth;

    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "pet")
    @Column(insertable=false, updatable=false)
    private List<PetAnalysis> petAnalysis = new ArrayList<>();

    @OneToMany(mappedBy = "pet")
    @Column(insertable=false, updatable=false)
    private List<Recommendation> recommendations = new ArrayList<>();
}
