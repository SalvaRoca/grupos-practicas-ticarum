package com.ticarum.grupospracticas.data;

import com.ticarum.grupospracticas.model.Asignatura;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AsignaturaRepository {
    private final AsignaturaJpaRepository asignaturaJpaRepository;

    public Asignatura obtenerAsignatura(Long idAsignatura) {
        return asignaturaJpaRepository.findById(idAsignatura).orElse(null);
    }

    public Asignatura guardarAsignatura(Asignatura asignatura) {
        return asignaturaJpaRepository.save(asignatura);
    }

    public void eliminarAsignatura (Long idAsignatura) {
        asignaturaJpaRepository.deleteById(idAsignatura);
    }
}
