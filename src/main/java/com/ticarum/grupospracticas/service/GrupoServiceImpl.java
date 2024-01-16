package com.ticarum.grupospracticas.service;

import com.ticarum.grupospracticas.data.AlumnoRepository;
import com.ticarum.grupospracticas.data.AsignaturaRepository;
import com.ticarum.grupospracticas.data.GrupoRepository;
import com.ticarum.grupospracticas.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class GrupoServiceImpl implements GrupoService {
    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Override
    public Grupo obtenerGrupo(Long idAsignatura, Long idGrupo) {
        return grupoRepository.obtenerGrupo(asignaturaRepository.obtenerAsignatura(idAsignatura), idGrupo);
    }

    @Override
    public Grupo crearGrupo(Long idAsignatura, Long idGrupo, GrupoDto grupoDto) {
        Asignatura asignatura = asignaturaRepository.obtenerAsignatura(idAsignatura);

        if (
                grupoRepository.obtenerGrupo(asignaturaRepository.obtenerAsignatura(idAsignatura), idGrupo) == null // No debe existir un grupo con el mismo ID
                        && asignatura != null // La asignatura debe existir
                        && asignatura.getGrupos().size() < 5 // Máximo 5 grupos por asignatura
                        && grupoDto.getCodigoGrupo() != null // Este atributo con el que se crea el objeto no debe ser null
                        && grupoDto.getNombreGrupo() != null // Este atributo con el que se crea el objeto no debe ser null
        ) {
            // Si se cumplen las condiciones, construimos el nuevo objeto con el patrón Builder y lo guardamos en la base de datos
            Grupo grupo = Grupo.builder()
                    .idGrupo(idGrupo)
                    .asignatura(asignatura)
                    .codigoGrupo(grupoDto.getCodigoGrupo())
                    .nombreGrupo(grupoDto.getNombreGrupo())
                    .build();

            return grupoRepository.guardarGrupo(grupo);
        } else {
            // Si no se cumplen las condiciones, devolvemos null para gestionar la respuesta HTTP
            return null;
        }
    }

    @Override
    public Grupo modificarGrupo(Grupo grupo, GrupoDto grupoDto) {
        if (
                grupoDto.getCodigoGrupo() == null // Este atributo, que no debe modificarse, debe ser null
                        && grupoDto.getNombreGrupo() != null // El atributo a modificar no debe ser null
                        && !grupoDto.getNombreGrupo().equals(grupo.getNombreGrupo()) // El nuevo atributo debe ser diferente del existente
        ) {
            grupo.setNombreGrupo(grupoDto.getNombreGrupo());

            return grupoRepository.guardarGrupo(grupo);
        }
        // Si no se cumplen las condiciones, devolvemos null para gestionar la respuesta HTTP
        return null;
    }

    @Override
    public Grupo agregarAlumnoAGrupo(Long idAsignatura, Long idGrupo, AlumnoDto alumnoDto) {
        Asignatura asignatura = asignaturaRepository.obtenerAsignatura(idAsignatura);
        Grupo grupo = grupoRepository.obtenerGrupo(asignatura, idGrupo);

        if (
                grupo != null // El grupo debe existir
                        && alumnoDto.getDniAlumno() != null // Este atributo con el que se comprueba o crea el objeto no debe ser null
                        && alumnoDto.getNombreAlumno() != null // Este atributo con el que se comprueba o crea el objeto no debe ser null
                        && alumnoDto.getApellidosAlumno() != null // Este atributo con el que se comprueba o crea el objeto no debe ser null

        ) {
            // Si se cumplen las condiciones, comprobamos si existe ya un alumno con el mismo DNI
            Alumno alumnoExistente = alumnoRepository.buscarAlumnoPorDni(alumnoDto.getDniAlumno());

            if (alumnoExistente != null) {
                // Si el alumno con el DNI indicado ya existe en la base de datos, comprobamos que sus datos coincidan y que no pertenezca ya a un grupo en la misma asignatura
                if (
                        Objects.equals(alumnoDto.getDniAlumno(), alumnoExistente.getDniAlumno())
                                && Objects.equals(alumnoDto.getNombreAlumno(), alumnoExistente.getNombreAlumno())
                                && Objects.equals(alumnoDto.getApellidosAlumno(), alumnoExistente.getApellidosAlumno())
                                && alumnoExistente.getGrupos().stream().noneMatch(g -> g.getAsignatura().equals(asignatura))
                ) {
                    grupo.getAlumnos().add(alumnoExistente);
                    return grupoRepository.guardarGrupo(grupo);
                } else {
                    // Si no coinciden, devolvemos null para gestionar la respuesta HTTP
                    return null;
                }
            } else {
                // Si el alumno con el DNI indicado no existe, lo añadimos a la base de datos
                Alumno alumnoNuevo = Alumno.builder()
                        .dniAlumno(alumnoDto.getDniAlumno())
                        .nombreAlumno(alumnoDto.getNombreAlumno())
                        .apellidosAlumno(alumnoDto.getApellidosAlumno())
                        .build();

                alumnoRepository.crearAlumno(alumnoNuevo);
                grupo.getAlumnos().add(alumnoNuevo);
                return grupoRepository.guardarGrupo(grupo);
            }
        } else {
            // Si se cumplen las condiciones, comprobamos si existe ya un alumno con el mismo DNI
            return null;
        }
    }

    @Override
    public Grupo eliminarAlumnoDeGrupo(Long idAsignatura, Long idGrupo, String dniAlumno) {
        Asignatura asignatura = asignaturaRepository.obtenerAsignatura(idAsignatura);
        Grupo grupo = grupoRepository.obtenerGrupo(asignatura, idGrupo);

        if (
                grupo != null // El grupo debe existir
                        && grupo.getAlumnos().removeIf(alumno -> alumno.getDniAlumno().equals(dniAlumno)) // Intenta eliminar el alumno por su DNI, si lo consigue devuelve true
        ) {

            return grupoRepository.guardarGrupo(grupo);
        }

        return null;
    }
}
