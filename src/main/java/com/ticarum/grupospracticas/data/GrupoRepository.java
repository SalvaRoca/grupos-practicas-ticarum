package com.ticarum.grupospracticas.data;

import com.ticarum.grupospracticas.model.Asignatura;
import com.ticarum.grupospracticas.model.Grupo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GrupoRepository {
    private final GrupoJpaRepository grupoJpaRepository;

    public Grupo obtenerGrupo(Long idGrupo) {
        return grupoJpaRepository.findById(idGrupo).orElse(null);
    }
    public Grupo obtenerGrupoDeAsignatura(Asignatura asignatura, Long idGrupo) {
        return grupoJpaRepository.findByAsignaturaAndIdGrupo(asignatura, idGrupo).orElse(null);
    }

    public Grupo guardarGrupo(Grupo grupo) {
        return grupoJpaRepository.save(grupo);
    }
}
