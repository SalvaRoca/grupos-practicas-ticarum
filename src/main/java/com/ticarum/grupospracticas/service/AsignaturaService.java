package com.ticarum.grupospracticas.service;

import com.ticarum.grupospracticas.model.Asignatura;
import com.ticarum.grupospracticas.model.AsignaturaDto;

public interface AsignaturaService {
    Asignatura obtenerAsignatura(Long idAsignatura);

    Asignatura crearAsignatura(Long idAsignatura, AsignaturaDto asignaturaDto);

    Asignatura modificarAsignatura(Asignatura asignatura, AsignaturaDto asignaturaDto);

    void eliminarAsignatura(Long id);
}
