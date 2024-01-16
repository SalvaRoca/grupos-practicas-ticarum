package com.ticarum.grupospracticas;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title = "Grupos de Prácticas - Universidad de Murcia",
		description = "API para la gestión de grupos de prácticas en asignaturas de la Universidad de Murcia.",
		version = "1.0.0"))
public class GruposPracticasApplication {

	public static void main(String[] args) {
		SpringApplication.run(GruposPracticasApplication.class, args);
	}

}
