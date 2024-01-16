package com.ticarum.grupospracticas.service;

import com.ticarum.grupospracticas.data.AsignaturaRepository;
import com.ticarum.grupospracticas.model.Asignatura;
import com.ticarum.grupospracticas.model.AsignaturaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AsignaturaServiceImpl implements AsignaturaService {
    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Override
    public Asignatura obtenerAsignatura(Long idAsignatura) {
        return asignaturaRepository.obtenerAsignatura(idAsignatura);
    }

    @Override
    public Asignatura crearAsignatura(Long idAsignatura, AsignaturaDto asignaturaDto) {
        if (
                asignaturaRepository.obtenerAsignatura(idAsignatura) == null // No debe existir una asignatura con el mismo ID
                && asignaturaDto.getCodigoAsignatura() != null // Este atributo con el que se crea el objeto no debe ser null
                && asignaturaDto.getNombreAsignatura() != null // Este atributo con el que se crea el objeto no debe ser null
                && asignaturaDto.getDescripcionAsignatura() != null // Este atributo con el que se crea el objeto no debe ser null
        ) {
            // Si se cumplen las condiciones, construimos el nuevo objeto con el patrón Builder y lo guardamos en la base de datos
            Asignatura asignatura = Asignatura.builder()
                    .idAsignatura(idAsignatura)
                    .codigoAsignatura(asignaturaDto.getCodigoAsignatura())
                    .nombreAsignatura(asignaturaDto.getNombreAsignatura())
                    .descripcionAsignatura(asignaturaDto.getDescripcionAsignatura())
                    .build();

            return asignaturaRepository.guardarAsignatura(asignatura);
        } else {
            // Si no se cumplen las condiciones, devolvemos null para gestionar la respuesta HTTP
            return null;
        }
    }

    @Override
    public Asignatura modificarAsignatura(Asignatura asignatura, AsignaturaDto asignaturaDto) {
        // Comprobamos que ningún atributo a modificar venga vacío y que la modificación no sea parcial (deben modificarse Nombre y Descripción de la asignatura)
        if (
                asignaturaDto != null
                && asignaturaDto.getCodigoAsignatura() == null // Este atributo, que no debe modificarse, debe ser null
                && asignaturaDto.getNombreAsignatura() != null // El atributo a modificar no debe ser null
                && asignaturaDto.getDescripcionAsignatura() != null // El atributo a modificar no debe ser null
                && !asignaturaDto.getNombreAsignatura().equals(asignatura.getNombreAsignatura()) // El nuevo atributo debe ser diferente del existente
                && !asignaturaDto.getDescripcionAsignatura().equals(asignatura.getDescripcionAsignatura()) // El nuevo atributo debe ser diferente del existente
        ) {
            // Si se cumplen las condiciones, modificamos Nombre y Descripción de la asignatura
            asignatura.setNombreAsignatura(asignaturaDto.getNombreAsignatura());
            asignatura.setDescripcionAsignatura(asignaturaDto.getDescripcionAsignatura());
            return asignaturaRepository.guardarAsignatura(asignatura);
        } else {
            // Si no se cumplen las condiciones, devolvemos null para gestionar la respuesta HTTP
            return null;
        }
    }

    @Override
    public void eliminarAsignatura(Long id) {
        asignaturaRepository.eliminarAsignatura(id);
    }
}
