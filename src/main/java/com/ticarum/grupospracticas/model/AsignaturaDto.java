package com.ticarum.grupospracticas.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AsignaturaDto {
    private String codigoAsignatura;

    private String nombreAsignatura;

    private String descripcionAsignatura;
}
