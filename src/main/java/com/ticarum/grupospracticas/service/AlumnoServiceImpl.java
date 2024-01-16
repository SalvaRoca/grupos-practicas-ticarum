package com.ticarum.grupospracticas.service;

import com.ticarum.grupospracticas.data.AlumnoRepository;
import com.ticarum.grupospracticas.model.Alumno;
import com.ticarum.grupospracticas.model.AlumnoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlumnoServiceImpl implements AlumnoService {
    @Autowired
    private AlumnoRepository alumnoRepository;

    @Override
    public List<Alumno> obtenerListaAlumnos() {
        return alumnoRepository.obtenerListaAlumnos();
    }

    @Override
    public Alumno obtenerDetalleAlumno(Long idAlumno) {
        return alumnoRepository.obtenerDetalleAlumno(idAlumno);
    }

    @Override
    public Alumno crearAlumno(AlumnoDto alumnoDto) {
        if (
                alumnoDto.getDniAlumno() != null // Este atributo con el que se crea el objeto no debe ser null
                && alumnoDto.getNombreAlumno() != null // Este atributo con el que se crea el objeto no debe ser null
                && alumnoDto.getApellidosAlumno() != null // Este atributo con el que se crea el objeto no debe ser null
        ) {
            Alumno alumno = Alumno.builder()
                    .dniAlumno(alumnoDto.getDniAlumno())
                    .nombreAlumno(alumnoDto.getNombreAlumno())
                    .apellidosAlumno(alumnoDto.getApellidosAlumno())
                    .build();

            return alumnoRepository.crearAlumno(alumno);
        } else {
            return null;
        }
    }
}
