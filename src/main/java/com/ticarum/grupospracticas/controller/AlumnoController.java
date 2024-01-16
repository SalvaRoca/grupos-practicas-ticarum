package com.ticarum.grupospracticas.controller;

import com.ticarum.grupospracticas.model.Alumno;
import com.ticarum.grupospracticas.model.AlumnoDto;
import com.ticarum.grupospracticas.service.AlumnoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alumnos")
@RequiredArgsConstructor
@Tag(name = "Alumnos", description = "Controlador para la gestión de alumnos")
public class AlumnoController {
    private final AlumnoService alumnoService;

    @GetMapping
    @Operation(
            operationId = "Consultar la lista de alumnos",
            description = "Operación de lectura",
            summary = "La API devuelve la lista completa de alumnos",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Alumno.class))
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    )
            }
    )
    public ResponseEntity<List<Alumno>> obtenerListaAlumnos() {
        List<Alumno> listaAlumnos = alumnoService.obtenerListaAlumnos();

        if (listaAlumnos != null) {
            // Si existe algún alumno en la base de datos, devuelve código 200 OK con la lista completa de alumnos
            return ResponseEntity.ok(listaAlumnos);
        } else {
            // Si no existe ningún alumno en la base de datos, devuelve código 204 No Content
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{idAlumno}")
    @Operation(
            operationId = "Consultar un alumno",
            description = "Operación de lectura",
            summary = "La API devuelve el alumno según su identificador",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Alumno.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    )
            }
    )
    public ResponseEntity<Alumno> obtenerDetalleAlumno(@PathVariable Long idAlumno) {
        Alumno alumno = alumnoService.obtenerDetalleAlumno(idAlumno);

        if (alumno != null) {
            // Si existe la asignatura con el ID indicado, devuelve código 200 OK con la asignatura correspondiente
            return ResponseEntity.ok(alumno);
        } else {
            // Si no existe la asignatura ocn el ID indicado, devuelve código 404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(
            operationId = "Crear un alumno",
            description = "Operación de escritura",
            summary = "La API crea un alumno con los datos indicados, verificando antes si ya existe un alumno con el mismo DNI",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Alumno.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    )
            }
    )
    public ResponseEntity<Alumno> guardarAlumno(@RequestBody AlumnoDto alumnoDto) {
        try {
            Alumno alumno = alumnoService.crearAlumno(alumnoDto);

            if (alumno != null) {
                // Si se ha creado correctamente, devuelve código 201 CREATED con el alumno creado
                return ResponseEntity.status(HttpStatus.CREATED).body(alumno);
            } else {
                // Si no se ha podido crear, devuelve código 400 BAD REQUEST
                return ResponseEntity.badRequest().build();
            }
        } catch (DataIntegrityViolationException e) {
            // Bloque try-catch necesario para gestionar excepción en caso de que ya exista un alumno con el mismo DNI
            return ResponseEntity.badRequest().build();
        }
    }
}
