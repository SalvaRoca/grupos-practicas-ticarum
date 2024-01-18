package com.ticarum.grupospracticas.controller;

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
 * Test de integración para el controlador de alumnos.
 * IMPORTANTE: realizar tests unitarios POR SEPARADO, al no disponer de métodos para borrado de alumnos, no es posible
 * establecer condiciones @BeforeEach y @AfterEach para los tests unitarios.
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@WithMockUser(username = "profesor", password = "profesor1234", roles = "PROFESOR")
public class AlumnoControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void obtenerListaAlumnos() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/alumnos"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        String body = "{\n" +
                      "  \"dniAlumno\": \"12345678A\",\n" +
                      "  \"nombreAlumno\": \"Nombre1\",\n" +
                      "  \"apellidosAlumno\": \"Apellidos1\"\n" +
                      "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/alumnos")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/alumnos"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[{\"idAlumno\":1,\"dniAlumno\":\"12345678A\",\"nombreAlumno\":\"Nombre1\",\"apellidosAlumno\":\"Apellidos1\"}]"));
    }

    @Test
    void obtenerDetalleAlumno() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/alumnos/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        String body = "{\n" +
                      "  \"dniAlumno\": \"12345678A\",\n" +
                      "  \"nombreAlumno\": \"Nombre1\",\n" +
                      "  \"apellidosAlumno\": \"Apellidos1\"\n" +
                      "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/alumnos")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/alumnos/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"idAlumno\":1,\"dniAlumno\":\"12345678A\",\"nombreAlumno\":\"Nombre1\",\"apellidosAlumno\":\"Apellidos1\"}"));
    }

    @Test
    void guardarAlumno() throws Exception {
        String bodyInfoFaltante = "{\n" +
                                  "  \"dniAlumno\": \"12345678A\",\n" +
                                  "  \"nombreAlumno\": \"Nombre1\"\n" +
                                  "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/alumnos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyInfoFaltante)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        String body = "{\n" +
                      "  \"dniAlumno\": \"12345678A\",\n" +
                      "  \"nombreAlumno\": \"Nombre1\",\n" +
                      "  \"apellidosAlumno\": \"Apellidos1\"\n" +
                      "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/alumnos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("{\"idAlumno\":1,\"dniAlumno\":\"12345678A\",\"nombreAlumno\":\"Nombre1\",\"apellidosAlumno\":\"Apellidos1\"}"));

        String bodyDniExistente = "{\n" +
                                  "  \"dniAlumno\": \"12345678A\",\n" +
                                  "  \"nombreAlumno\": \"Nombre2\",\n" +
                                  "  \"apellidosAlumno\": \"Apellidos2\"\n" +
                                  "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/alumnos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyDniExistente)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
