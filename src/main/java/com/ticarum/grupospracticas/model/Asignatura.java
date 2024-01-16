package com.ticarum.grupospracticas.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "asignatura")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Asignatura {
    @Id
    private Long idAsignatura;

    @Column(name="codigo", unique = true)
    private String codigoAsignatura;

    @Column(name="nombre")
    private String nombreAsignatura;

    @Column(name="descripcion")
    private String descripcionAsignatura;

    @OneToMany(mappedBy = "asignatura", cascade = CascadeType.ALL)
    @Size(max = 5)
    @JsonIgnoreProperties("asignatura")
    private List<Grupo> grupos;
}