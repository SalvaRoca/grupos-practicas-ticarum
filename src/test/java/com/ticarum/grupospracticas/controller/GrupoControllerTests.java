package com.ticarum.grupospracticas.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

/**
 * Test de integración para el controlador de grupos.
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@WithMockUser(username = "profesor", password = "profesor1234", roles = "PROFESOR")
public class GrupoControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() throws Exception {
        String bodyAsignatura = "{\n" +
                                "  \"codigoAsignatura\": \"ABC123\",\n" +
                                "  \"nombreAsignatura\": \"Prueba 1\",\n" +
                                "  \"descripcionAsignatura\": \"Asignatura de prueba 1\"\n" +
                                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/asignaturas/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyAsignatura)
        );

        String bodyGrupo = "{\n" +
                           "  \"codigoGrupo\": \"G1\",\n" +
                           "  \"nombreGrupo\": \"Grupo 1\"\n" +
                           "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/asignaturas/1/grupos/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyGrupo)
        );

        String bodyAlumno = "{\n" +
                            "  \"dniAlumno\": \"12345678A\",\n" +
                            "  \"nombreAlumno\": \"Nombre1\",\n" +
                            "  \"apellidosAlumno\": \"Apellidos1\"\n" +
                            "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/asignaturas/1/grupos/1/alumnos")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyAlumno)
        );
    }

    @AfterEach
    void tearDown() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/asignaturas/1").with(csrf()));
    }

    @Test
    void obtenerGrupoTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/asignaturas/1/grupos/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"idGrupo\":1,\"codigoGrupo\":\"G1\",\"nombreGrupo\":\"Grupo 1\",\"alumnos\":[{\"idAlumno\":1,\"dniAlumno\":\"12345678A\",\"nombreAlumno\":\"Nombre1\",\"apellidosAlumno\":\"Apellidos1\"}]}"));
        mockMvc.perform(MockMvcRequestBuilders.get("/asignaturas/1/grupos/2"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void crearGrupoTest() throws Exception {
        String bodyCodigoExistente = "{\n" +
                                     "\"codigoGrupo\": \"G1\",\n" +
                                     "\"nombreGrupo\": \"Grupo 2\"\n" +
                                     "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/asignaturas/1/grupos/2")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyCodigoExistente)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());


        String bodyInfoFaltante = "{\n" +
                                  "\"codigoGrupo\": \"G2\",\n" +
                                  "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/asignaturas/1/grupos/2")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyInfoFaltante)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        String bodyOk = "{\n" +
                        "  \"codigoGrupo\": \"G2\",\n" +
                        "  \"nombreGrupo\": \"Grupo 2\"\n" +
                        "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/asignaturas/1/grupos/2")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyOk)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("{\"idGrupo\":2,\"codigoGrupo\":\"G2\",\"nombreGrupo\":\"Grupo 2\",\"alumnos\":null}"));
    }

    @Test
    void modificarGrupoTest() throws Exception {
        String bodyInfoFaltante = "{\n" +
                                  "}";

        mockMvc.perform(MockMvcRequestBuilders.put("/asignaturas/1/grupos/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyInfoFaltante)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        String body = "{\n" +
                      "  \"nombreGrupo\": \"Grupo 2\"\n" +
                      "}";

        mockMvc.perform(MockMvcRequestBuilders.put("/asignaturas/1/grupos/2")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());


        mockMvc.perform(MockMvcRequestBuilders.put("/asignaturas/1/grupos/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"idGrupo\":1,\"codigoGrupo\":\"G1\",\"nombreGrupo\":\"Grupo 2\",\"alumnos\":[" +
                                                                "{\"idAlumno\":1,\"dniAlumno\":\"12345678A\",\"nombreAlumno\":\"Nombre1\",\"apellidosAlumno\":\"Apellidos1\"}]}"));
    }

    @Test
    void agregarAlumnoAGrupoTest() throws Exception {
        String bodyDniExistente = "{\n" +
                                  "  \"dniAlumno\": \"12345678A\",\n" +
                                  "  \"nombreAlumno\": \"Nombre2\",\n" +
                                  "  \"apellidosAlumno\": \"Apellidos2\"\n" +
                                  "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/asignaturas/1/grupos/1/alumnos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyDniExistente)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        String body = "{\n" +
                      "  \"dniAlumno\": \"98765432B\",\n" +
                      "  \"nombreAlumno\": \"Nombre2\",\n" +
                      "  \"apellidosAlumno\": \"Apellidos2\"\n" +
                      "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/asignaturas/1/grupos/1/alumnos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"idGrupo\":1,\"codigoGrupo\":\"G1\",\"nombreGrupo\":\"Grupo 1\",\"alumnos\":[" +
                        "{\"idAlumno\":1,\"dniAlumno\":\"12345678A\",\"nombreAlumno\":\"Nombre1\",\"apellidosAlumno\":\"Apellidos1\"}," +
                        "{\"idAlumno\":2,\"dniAlumno\":\"98765432B\",\"nombreAlumno\":\"Nombre2\",\"apellidosAlumno\":\"Apellidos2\"}" +
                        "]}"));
    }

    @Test
    void eliminarAlumnoDeGrupo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/asignaturas/1/grupos/1/alumnos?dniAlumno=12345678A").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        mockMvc.perform(MockMvcRequestBuilders.delete("/asignaturas/1/grupos/1/alumnos?dniAlumno=98765432B").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
