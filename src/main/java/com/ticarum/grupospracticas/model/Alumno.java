package com.ticarum.grupospracticas.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.List;



@Entity
@Table(name = "alumno")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Alumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAlumno;

    @Column(name="dni", unique = true)
    private String dniAlumno;

    @Column(name="nombre")
    private String nombreAlumno;

    @Column(name="apellidos")
    private String apellidosAlumno;

    @ManyToMany(mappedBy = "alumnos")
    @JsonIgnoreProperties("alumnos")
    private List<Grupo> grupos;
}