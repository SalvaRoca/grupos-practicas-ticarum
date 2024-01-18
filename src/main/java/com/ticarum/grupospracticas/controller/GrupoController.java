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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/asignaturas/{idAsignatura}/grupos/{idGrupo}")
@RequiredArgsConstructor
@Tag(name = "Grupos", description = "Controlador para la gestión de grupos")
public class GrupoController {
    private final GrupoService grupoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('PROFESOR', 'ALUMNO')")
    @Operation(
            operationId = "Consultar un grupo",
            description = "Operación de lectura",
            summary = "La API devuelve el grupo según su identificador",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Grupo.class))
                    ),
                    @ApiResponse(responseCode = "401", content = @Content),
                    @ApiResponse(responseCode = "403", content = @Content),
                    @ApiResponse(responseCode = "404", content = @Content),
                    @ApiResponse(responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<?> obtenerGrupo(@PathVariable Long idAsignatura, @PathVariable Long idGrupo) {
        try {
            Grupo grupo = grupoService.obtenerGrupo(idAsignatura, idGrupo);

            if (grupo != null) {
                // Si existe el grupo con el ID indicado en la asignatura, devuelve código 200 OK con el grupo correspondiente
                return ResponseEntity.ok(grupo);
            } else {
                // Si no existe el grupo con el ID indicado en la asignatura, devuelve código 404 NOT FOUND
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"mensaje\": \"No se encontró el grupo con el ID " + idGrupo + " en la asignatura con el ID " + idAsignatura + "\"}");
            }
        } catch (RuntimeException e) {
            // Gestiona la excepción en caso de error en tiempo de ejecución, devolviendo 500 INTERNAL SERVER ERROR
            return ResponseEntity.internalServerError().body("{\"mensaje\": \"Error interno del servidor\"}");
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PROFESOR', 'ALUMNO')")
    @Operation(
            operationId = "Crear un grupo",
            description = "Operación de escritura",
            summary = "La API crea un grupo con los datos indicados (límite 5 grupos por asignatura)",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Grupo.class))
                    ),
                    @ApiResponse(responseCode = "400", content = @Content),
                    @ApiResponse(responseCode = "401", content = @Content),
                    @ApiResponse(responseCode = "403", content = @Content),
                    @ApiResponse(responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<?> crearGrupo(@PathVariable Long idAsignatura, @PathVariable Long idGrupo, @RequestBody GrupoDto grupoDto) {
        try {
            Grupo grupo = grupoService.crearGrupo(idAsignatura, idGrupo, grupoDto);

            if (grupo != null) {
                // Si se ha creado correctamente, devuelve código 201 CREATED con el grupo creado
                return ResponseEntity.status(HttpStatus.CREATED).body(grupo);
            } else {
                // Si no se ha podido crear, devuelve código 400 BAD REQUEST
                return ResponseEntity.badRequest().body("{\"mensaje\": \"No se ha podido crear el grupo, compruebe que no existan 5 grupos en la asignatura y revise los datos introducidos\"}");
            }
        } catch (DataIntegrityViolationException e) {
            // Gestiona la excepción en caso de que ya exista un grupo con el mismo código
            return ResponseEntity.badRequest().body("{\"mensaje\": \"Ya existe un grupo con código " + grupoDto.getCodigoGrupo() + "\"}");
        } catch (RuntimeException e) {
            // Gestiona la excepción en caso de error en tiempo de ejecución, devolviendo 500 INTERNAL SERVER ERROR
            return ResponseEntity.internalServerError().body("{\"mensaje\": \"Error interno del servidor\"}");
        }
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('PROFESOR', 'ALUMNO')")
    @Operation(
            operationId = "Modificar un grupo",
            description = "Operación de escritura",
            summary = "La API modifica un grupo con los datos indicados, sin afectar a los alumnos que ya pertenecen al grupo",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Grupo.class))
                    ),
                    @ApiResponse(responseCode = "400", content = @Content),
                    @ApiResponse(responseCode = "401", content = @Content),
                    @ApiResponse(responseCode = "403", content = @Content),
                    @ApiResponse(responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<?> modificarGrupo(@PathVariable Long idAsignatura, @PathVariable Long idGrupo, @RequestBody GrupoDto grupoDto) {
        try {
            Grupo grupoExistente = grupoService.obtenerGrupo(idAsignatura, idGrupo);

            if (grupoExistente != null) {
                // Si existe el grupo con el ID indicado en la asignatura, continúa la lógica
                Grupo grupoModificado = grupoService.modificarGrupo(grupoExistente, grupoDto);
                if (grupoModificado != null) {
                    // Si se ha modificado correctamente el grupo, devuelve código 200 OK con el grupo modificado
                    return ResponseEntity.ok(grupoModificado);
                } else {
                    // Si no se ha modificado correctamente el grupo, devuelve código 400 BAD REQUEST
                    return ResponseEntity.badRequest().body("{\"mensaje\": \"No se ha podido modificar el grupo, revise los datos introducidos\"}");
                }
            } else {
                // Si no existe el grupo con el ID indicado en la asignatura, devuelve código 404 NOT FOUND
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"mensaje\": \"No se encontró el grupo con ID " + idGrupo + "en la asignatura con ID " + idAsignatura + "\"}");
            }
        } catch (RuntimeException e) {
            // Gestiona la excepción en caso de error en tiempo de ejecución, devolviendo 500 INTERNAL SERVER ERROR
            return ResponseEntity.internalServerError().body("{\"mensaje\": \"Error interno del servidor\"}");
        }
    }

    @PostMapping("/alumnos")
    @PreAuthorize("hasAnyRole('PROFESOR', 'ALUMNO')")
    @Operation(
            operationId = "Agregar un alumno a un grupo",
            description = "Operación de escritura",
            summary = "La API agrega un alumno a un grupo según el ID del alumno y del grupo, verificando antes si el alumno ya pertenece a otro grupo en la misma asignatura",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Grupo.class))
                    ),
                    @ApiResponse(responseCode = "400", content = @Content),
                    @ApiResponse(responseCode = "401", content = @Content),
                    @ApiResponse(responseCode = "403", content = @Content),
                    @ApiResponse(responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<?> agregarAlumnoAGrupo(@PathVariable Long idAsignatura, @PathVariable Long idGrupo, @RequestBody AlumnoDto alumnoDto) {
        try {
            Grupo grupo = grupoService.agregarAlumnoAGrupo(idAsignatura, idGrupo, alumnoDto);

            if (grupo != null) {
                // Si se ha agregado correctamente el alumno, devuelve código 200 OK con el grupo modificado
                return ResponseEntity.ok(grupo);
            } else {
                // Si no se ha agregado correctamente el alumno, devuelve código 400 BAD REQUEST
                return ResponseEntity.badRequest().body("{\"mensaje\": \"No se ha podido agregar el alumno al grupo, revise los datos introducidos\"}");
            }
        } catch (RuntimeException e) {
            // Gestiona la excepción en caso de error en tiempo de ejecución, devolviendo 500 INTERNAL SERVER ERROR
            return ResponseEntity.internalServerError().body("{\"mensaje\": \"Error interno del servidor\"}");
        }
    }

    @DeleteMapping("/alumnos")
    @PreAuthorize("hasAnyRole('PROFESOR', 'ALUMNO')")
    @Operation(
            operationId = "Eliminar un alumno de un grupo",
            description = "Operación de escritura",
            summary = "La API elimina un alumno de un grupo según el ID del alumno y del grupo",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Grupo.class))
                    ),
                    @ApiResponse(responseCode = "400", content = @Content),
                    @ApiResponse(responseCode = "401", content = @Content),
                    @ApiResponse(responseCode = "403", content = @Content),
                    @ApiResponse(responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<?> eliminarAlumnoDeGrupo(@PathVariable Long idAsignatura, @PathVariable Long idGrupo, @RequestParam String dniAlumno) {
        try {
            Grupo grupo = grupoService.eliminarAlumnoDeGrupo(idAsignatura, idGrupo, dniAlumno);

            if (grupo != null) {
                // Si se ha eliminado correctamente el alumno del grupo, devuelve código 200 OK con el grupo modificado
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("El alumno con DNI " + dniAlumno + "se ha eliminado correctamente del grupo con ID " + idGrupo);
            } else {
                // Si no ha eliminado correctamente el alumno del grupo, devuelve código 400 BAD REQUEST
                return ResponseEntity.badRequest().body("{\"mensaje\": \"No se ha podido eliminar el alumno del grupo, revise los datos introducidos\"}");
            }
        } catch (RuntimeException e) {
            // Gestiona la excepción en caso de error en tiempo de ejecución, devolviendo 500 INTERNAL SERVER ERROR
            return ResponseEntity.internalServerError().body("{\"mensaje\": \"Error interno del servidor\"}");
        }
    }
}
