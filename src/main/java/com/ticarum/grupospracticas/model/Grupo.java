package com.ticarum.grupospracticas.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "grupo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Grupo {
    @Id
    private Long idGrupo;

    @ManyToOne
    @JoinColumn(name = "id_asignatura", nullable = false)
    @JsonIgnoreProperties("grupos")
    private Asignatura asignatura;

    @Column(name = "codigo", unique = true)
    private String codigoGrupo;

    @Column(name = "nombre")
    private String nombreGrupo;

    @ManyToMany
    @JoinTable(
            name = "alumno_grupo",
            joinColumns = @JoinColumn(name = "id_grupo"),
            inverseJoinColumns = @JoinColumn(name = "id_alumno")
    )
    @JsonIgnoreProperties("grupos")
    private List<Alumno> alumnos;
}