package com.ticarum.grupospracticas.data;

import com.ticarum.grupospracticas.model.Asignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AsignaturaJpaRepository  extends JpaRepository<Asignatura, Long>, JpaSpecificationExecutor<Asignatura> {
}
