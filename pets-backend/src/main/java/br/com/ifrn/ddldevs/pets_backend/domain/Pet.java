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

    private Double weight;

    private String breed;

    private Integer height;

    private Date dateOfBirth;

    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
