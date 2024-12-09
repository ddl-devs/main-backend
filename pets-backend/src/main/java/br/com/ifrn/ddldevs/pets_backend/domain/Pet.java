package br.com.ifrn.ddldevs.pets_backend.domain;


import br.com.ifrn.ddldevs.pets_backend.domain.Enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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

    private String species;

    private Double weight;

    private Integer height;

    private Date dateOfBirth;

    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

}
