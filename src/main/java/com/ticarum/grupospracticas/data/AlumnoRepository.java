package com.ticarum.grupospracticas.data;

import com.ticarum.grupospracticas.model.Alumno;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlumnoRepository {
    private final AlumnoJpaRepository alumnoJpaRepository;

    public List<Alumno> obtenerListaAlumnos() {
        return alumnoJpaRepository.findAll();
    }

    public Alumno obtenerDetalleAlumno(Long idAlumno) {
        return alumnoJpaRepository.findById(idAlumno).orElse(null);
    }

    public Alumno buscarAlumnoPorDni(String dniAlumno) {
        return alumnoJpaRepository.findByDniAlumno(dniAlumno).orElse(null);
    }

    public Alumno crearAlumno(Alumno alumno) {
        return alumnoJpaRepository.save(alumno);
    }
}
