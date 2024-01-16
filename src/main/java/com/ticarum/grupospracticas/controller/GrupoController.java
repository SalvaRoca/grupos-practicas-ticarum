package com.ticarum.grupospracticas.controller;

import com.ticarum.grupospracticas.model.AlumnoDto;
import com.ticarum.grupospracticas.model.Grupo;
import com.ticarum.grupospracticas.model.GrupoDto;
import com.ticarum.grupospracticas.service.GrupoService;
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

@RestController
@RequestMapping("/asignaturas/{idAsignatura}/grupos/{idGrupo}")
@RequiredArgsConstructor
@Tag(name = "Grupos", description = "Controlador para la gestión de grupos")
public class GrupoController {
    private final GrupoService grupoService;

    @GetMapping
    @Operation(
            operationId = "Consultar un grupo",
            description = "Operación de lectura",
            summary = "La API devuelve el grupo según su identificador",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Grupo.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    )
            }
    )
    public ResponseEntity<Grupo> obtenerGrupo(@PathVariable Long idAsignatura, @PathVariable Long idGrupo) {
        Grupo grupo = grupoService.obtenerGrupo(idAsignatura, idGrupo);

        if (grupo != null) {
            // Si existe el grupo con el ID indicado en la asignatura, devuelve código 200 OK con el grupo correspondiente
            return ResponseEntity.ok(grupo);
        } else {
            // Si no existe el grupo con el ID indicado en la asignatura, devuelve código 404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(
            operationId = "Crear un grupo",
            description = "Operación de escritura",
            summary = "La API crea un grupo con los datos indicados (límite 5 grupos por asignatura)",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Grupo.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    )
            }
    )
    public ResponseEntity<Grupo> crearGrupo(@PathVariable Long idAsignatura, @PathVariable Long idGrupo, @RequestBody GrupoDto grupoDto) {
        try {
            Grupo grupo = grupoService.crearGrupo(idAsignatura, idGrupo, grupoDto);

            if (grupo != null) {
                // Si se ha creado correctamente, devuelve código 201 CREATED con el grupo creado
                return ResponseEntity.status(HttpStatus.CREATED).body(grupo);
            } else {
                // Si no se ha podido crear, devuelve código 400 BAD REQUEST
                return ResponseEntity.badRequest().build();
            }
        } catch (DataIntegrityViolationException e) {
            // Bloque try-catch necesario para gestionar excepción en caso de que ya exista un grupo con el mismo código
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    @Operation(
            operationId = "Modificar un grupo",
            description = "Operación de escritura",
            summary = "La API modifica un grupo con los datos indicados, sin afectar a los alumnos que ya pertenecen al grupo",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Grupo.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    )
            }
    )
    public ResponseEntity<Grupo> modificarGrupo(@PathVariable Long idAsignatura, @PathVariable Long idGrupo, @RequestBody GrupoDto grupoDto) {
        Grupo grupoExistente = grupoService.obtenerGrupo(idAsignatura, idGrupo);

        if (grupoExistente != null) {
            // Si existe el grupo con el ID indicado en la asignatura, continúa la lógica
            Grupo grupoModificado = grupoService.modificarGrupo(grupoExistente, grupoDto);
            if (grupoModificado != null) {
                // Si se ha modificado correctamente el grupo, devuelve código 200 OK con el grupo modificado
                return ResponseEntity.ok(grupoModificado);
            } else {
                // Si no se ha modificado correctamente el grupo, devuelve código 400 BAD REQUEST
                return ResponseEntity.badRequest().build();
            }
        } else {
            // Si no existe el grupo con el ID indicado en la asignatura, devuelve código 404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/alumnos")
    @Operation(
            operationId = "Agregar un alumno a un grupo",
            description = "Operación de escritura",
            summary = "La API agrega un alumno a un grupo según el ID del alumno y del grupo, verificando antes si el alumno ya pertenece a otro grupo en la misma asignatura",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Grupo.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    )
            }
    )
    public ResponseEntity<Grupo> agregarAlumnoAGrupo(@PathVariable Long idAsignatura, @PathVariable Long idGrupo, @RequestBody AlumnoDto alumnoDto) {
        Grupo grupo = grupoService.agregarAlumnoAGrupo(idAsignatura, idGrupo, alumnoDto);

        if (grupo != null) {
            // Si se ha agregado correctamente el alumno, devuelve código 200 OK con el grupo modificado
            return ResponseEntity.ok(grupo);
        } else {
            // Si no se ha agregado correctamente el alumno, devuelve código 400 BAD REQUEST
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/alumnos")
    @Operation(
            operationId = "Eliminar un alumno de un grupo",
            description = "Operación de escritura",
            summary = "La API elimina un alumno de un grupo según el ID del alumno y del grupo",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Grupo.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    )
            }
    )
    public ResponseEntity<Grupo> eliminarAlumnoDeGrupo(@PathVariable Long idAsignatura, @PathVariable Long idGrupo, @RequestParam String dniAlumno) {
        Grupo grupo = grupoService.eliminarAlumnoDeGrupo(idAsignatura, idGrupo, dniAlumno);

        if (grupo != null) {
            // Si se ha eliminado correctamente el alumno del grupo, devuelve código 200 OK con el grupo modificado
            return ResponseEntity.ok(grupo);
        } else {
            // Si no ha eliminado correctamente el alumno del grupo, devuelve código 400 BAD REQUEST
            return ResponseEntity.badRequest().build();
        }
    }
}
