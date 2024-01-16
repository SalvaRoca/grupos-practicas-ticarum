package com.ticarum.grupospracticas.controller;

import com.ticarum.grupospracticas.model.Asignatura;
import com.ticarum.grupospracticas.model.AsignaturaDto;
import com.ticarum.grupospracticas.service.AsignaturaService;
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
@RequestMapping("/asignaturas/{idAsignatura}")
@RequiredArgsConstructor
@Tag(name = "Asignaturas", description = "Controlador para la gestión de asignaturas")
public class AsignaturaController {
    private final AsignaturaService asignaturaService;

    @GetMapping
    @Operation(
            operationId = "Consultar una asignatura",
            description = "Operación de lectura",
            summary = "La API devuelve la asignatura según su identificador",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Asignatura.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    )
            }
    )
    public ResponseEntity<Asignatura> obtenerAsignatura(@PathVariable Long idAsignatura) {
        Asignatura asignatura = asignaturaService.obtenerAsignatura(idAsignatura);

        if (asignatura != null) {
            // Si existe la asignatura con el ID indicado, devuelve código 200 OK con la asignatura correspondiente
            return ResponseEntity.ok(asignatura);
        } else {
            // Si no existe la asignatura ocn el ID indicado, devuelve código 404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(
            operationId = "Crear una asignatura",
            description = "Operación de escritura",
            summary = "La API crea una asignatura con los datos indicados",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Asignatura.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    )
            }
    )
    public ResponseEntity<Asignatura> crearAsignatura(@PathVariable Long idAsignatura, @RequestBody AsignaturaDto asignaturaDto) {
        try {
            Asignatura asignatura = asignaturaService.crearAsignatura(idAsignatura, asignaturaDto);
            if (asignatura != null) {
                // Si se ha creado correctamente, devuelve código 201 CREATED con la asignatura creada
                return ResponseEntity.status(HttpStatus.CREATED).body(asignatura);
            } else {
                // Si no se ha podido crear, devuelve código 400 BAD REQUEST
                return ResponseEntity.badRequest().build();
            }
        } catch (DataIntegrityViolationException e) {
            // Bloque try-catch necesario para gestionar excepción en caso de que ya exista una asignatura con el mismo código
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    @Operation(
            operationId = "Modificar una asignatura",
            description = "Operación de escritura",
            summary = "La API modifica una asignatura según el ID y los datos indicados",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Asignatura.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    )
            }
    )
    public ResponseEntity<Asignatura> modificarAsignatura(@PathVariable Long idAsignatura, @RequestBody AsignaturaDto asignaturaDto) {
        Asignatura asignaturaExistente = asignaturaService.obtenerAsignatura(idAsignatura);

        if (asignaturaExistente != null) {
            // Si existe la asignatura con el ID indicado, continúa la lógica
            Asignatura asignaturaModificada = asignaturaService.modificarAsignatura(asignaturaExistente, asignaturaDto);
            if (asignaturaModificada != null) {
                // Si se ha modificado correctamente la asignatura, devuelve código 200 OK con la asignatura modificada
                return ResponseEntity.ok(asignaturaModificada);
            } else {
                // Si no se ha modificado correctamente la asignatura, devuelve código 400 BAD REQUEST
                return ResponseEntity.badRequest().build();
            }
        } else {
            // Si no existe la asignatura con el ID indicado, devuelve código 404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    @Operation(
            operationId = "Eliminar una asignatura",
            description = "Operación de escritura",
            summary = "La API elimina una asignatura según el ID indicado",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    )
            }
    )
    public ResponseEntity<Asignatura> deleteAsignatura(@PathVariable Long idAsignatura) {
        Asignatura asignatura = asignaturaService.obtenerAsignatura(idAsignatura);

        if (asignatura != null) {
            // Si existe la asignatura con el ID indicado, la elimina y devuelve código 204 NO CONTENT
            asignaturaService.eliminarAsignatura(idAsignatura);
            return ResponseEntity.noContent().build();
        } else {
            // Si no existe la asignatura con el ID indicado, devuelve 404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
    }
}
