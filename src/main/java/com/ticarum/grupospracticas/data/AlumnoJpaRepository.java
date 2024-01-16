package com.ticarum.grupospracticas.data;

import com.ticarum.grupospracticas.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AlumnoJpaRepository extends JpaRepository<Alumno, Long>, JpaSpecificationExecutor<Alumno> {
    Optional<Alumno> findByDniAlumno(String dniAlumno);
}
