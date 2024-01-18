package com.ticarum.grupospracticas.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@WithMockUser(username = "profesor", password = "profesor1234", roles = "PROFESOR")
public class AsignaturaControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() throws Exception {
        String body = "{\n" +
                      "  \"codigoAsignatura\": \"ABC123\",\n" +
                      "  \"nombreAsignatura\": \"Prueba 1\",\n" +
                      "  \"descripcionAsignatura\": \"Asignatura de prueba 1\"\n" +
                      "}\n";

        mockMvc.perform(MockMvcRequestBuilders.post("/asignaturas/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        );
    }

    @AfterEach
    void tearDown() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/asignaturas/1").with(csrf()));
        mockMvc.perform(MockMvcRequestBuilders.delete("/asignaturas/2").with(csrf()));
    }

    @Test
    void obtenerAsignaturaTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/asignaturas/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"idAsignatura\":1,\"codigoAsignatura\":\"ABC123\",\"nombreAsignatura\":\"Prueba 1\",\"descripcionAsignatura\":\"Asignatura de prueba 1\"}"));
        mockMvc.perform(MockMvcRequestBuilders.get("/asignaturas/99"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void crearAsignaturaTest() throws Exception {
        String bodyCodigoExistente = "{\n" +
                                     "  \"codigoAsignatura\": \"ABC123\",\n" +
                                     "  \"nombreAsignatura\": \"Prueba 2\",\n" +
                                     "  \"descripcionAsignatura\": \"Asignatura de prueba 2\"\n" +
                                     "}\n";

        mockMvc.perform(MockMvcRequestBuilders.post("/asignaturas/2")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyCodigoExistente)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        String bodyInfoFaltante = "{\n" +
                                  "  \"codigoAsignatura\": \"ABC123\",\n" +
                                  "  \"nombreAsignatura\": \"Prueba 2\",\n" +
                                  "}\n";

        mockMvc.perform(
                MockMvcRequestBuilders.post("/asignaturas/2")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyInfoFaltante)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());

        String body = "{\n" +
                      "  \"codigoAsignatura\": \"DEF456\",\n" +
                      "  \"nombreAsignatura\": \"Prueba 2\",\n" +
                      "  \"descripcionAsignatura\": \"Asignatura de prueba 2\"\n" +
                      "}\n";

        mockMvc.perform(MockMvcRequestBuilders.post("/asignaturas/2")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("{\"idAsignatura\":2,\"codigoAsignatura\":\"DEF456\",\"nombreAsignatura\":\"Prueba 2\",\"descripcionAsignatura\":\"Asignatura de prueba 2\"}"));
    }

    @Test
    void modificarAsignaturaTest() throws Exception {
        String bodyInfoFaltante = "{\n" +
                                  "  \"nombreAsignatura\": \"Prueba 1 modificada\",\n" +
                                  "}\n";

        mockMvc.perform(MockMvcRequestBuilders.put("/asignaturas/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyInfoFaltante)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        String body = "{\n" +
                      "  \"nombreAsignatura\": \"Prueba 1 modificada\",\n" +
                      "  \"descripcionAsignatura\": \"Asignatura de prueba 1 modificada\"\n" +
                      "}\n";

        mockMvc.perform(MockMvcRequestBuilders.put("/asignaturas/99")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders.put("/asignaturas/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"idAsignatura\":1,\"codigoAsignatura\":\"ABC123\",\"nombreAsignatura\":\"Prueba 1 modificada\",\"descripcionAsignatura\":\"Asignatura de prueba 1 modificada\"}"));
    }

    @Test
    void eliminarAsignaturaTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/asignaturas/1").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        mockMvc.perform(MockMvcRequestBuilders.delete("/asignaturas/1").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
