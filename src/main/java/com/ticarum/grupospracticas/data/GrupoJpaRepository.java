package com.ticarum.grupospracticas.data;

import com.ticarum.grupospracticas.model.Asignatura;
import com.ticarum.grupospracticas.model.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface GrupoJpaRepository extends JpaRepository<Grupo, Long>, JpaSpecificationExecutor<Grupo> {
    Optional<Grupo> findByAsignaturaAndIdGrupo(Asignatura asignatura, Long idGrupo);
}
