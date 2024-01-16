package com.ticarum.grupospracticas.service;

import com.ticarum.grupospracticas.model.Alumno;
import com.ticarum.grupospracticas.model.AlumnoDto;

import java.util.List;

public interface AlumnoService {
    List<Alumno> obtenerListaAlumnos();

    Alumno obtenerDetalleAlumno(Long idAlumno);

    Alumno crearAlumno(AlumnoDto alumnoDto);
}
