package com.ticarum.grupospracticas.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlumnoDto {
    private String dniAlumno;

    private String nombreAlumno;

    private String apellidosAlumno;
}
