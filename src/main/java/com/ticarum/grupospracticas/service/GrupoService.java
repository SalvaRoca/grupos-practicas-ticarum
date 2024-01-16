package com.ticarum.grupospracticas.service;

import com.ticarum.grupospracticas.model.AlumnoDto;
import com.ticarum.grupospracticas.model.Grupo;
import com.ticarum.grupospracticas.model.GrupoDto;

public interface GrupoService {
    Grupo obtenerGrupo(Long idAsignatura, Long idGrupo);

    Grupo crearGrupo(Long idAsignatura, Long idGrupo, GrupoDto grupoDto);

    Grupo modificarGrupo(Grupo grupo, GrupoDto grupoDto);

    Grupo agregarAlumnoAGrupo(Long idAsignatura, Long idGrupo, AlumnoDto alumnoDto);

    Grupo eliminarAlumnoDeGrupo(Long idAsignatura, Long idGrupo, String dniAlumno);
}
