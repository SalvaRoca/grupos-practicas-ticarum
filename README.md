[![TICARUM](https://www.ticarum.es/wp-content/uploads/2017/12/TICARUM_larga-derecha_naranja_web_400px.png)](https://www.ticarum.es)

# Grupos de Prácticas - Universidad de Murcia

## API para la gestión de grupos de prácticas en asignaturas de la Universidad de Murcia

_Desarrollada por **Salva Roca** para TICARUM_

#### Descripción

Esta API REST desarrollada en `Spring Boot 2.7.18` y `Java 11` permite la gestión de grupos de prácticas de asignaturas
en
el
sistema de gestión educativa de la Universidad de Murcia. Permite diversas operaciones CRUD siguiendo los requisitos de
diseño planteados para las entidades que componen el sistema: Asignaturas, Grupos y Alumnos.

Se ha implementado `Spring Security 5.8.9` para la autenticación y autorización de usuarios, lo que además aporta una
capa de protección adicional contra amenazas comunes como SQLi, XSS o CSRF. Existen dos niveles de acceso a la API:
profesor y alumno.

* Profesor: puede realizar todas las operaciones CRUD sobre las entidades, puede autenticarse con las siguientes
  credenciales de prueba:
  * usuario: `profesor`
  * contraseña `profesor1234`.
* Alumno: puede realizar operaciones de lectura sobre las entidadespuede autenticarse con las siguientes credenciales de
  prueba:
  * usuario: `alumno`
  * contraseña `alumno1234`.

La aplicación cuenta con tests unitarios para todos los métodos de los controladores, de forma que se puede probar la
implementación. Los métodos de los controladores con `Swagger OpenAPI`, al que se recomienda acceder con las
credenciales de `profesor` para realizar pruebas de cada endpoint. Estas pruebas se pueden realizar más cómodamente con
la [colección de Postman](grupos-practicas.postman_collection.json) presente
en el proyecto y accesible en el siguiente botón:

[<img src="https://run.pstmn.io/button.svg" alt="Run In Postman" style="width: 128px; height: 32px;">](https://app.getpostman.com/run-collection/24695878-2c982b7c-7096-43c1-861d-7629e43f17c4?action=collection%2Ffork&source=rip_markdown&collection-url=entityId%3D24695878-2c982b7c-7096-43c1-861d-7629e43f17c4%26entityType%3Dcollection%26workspaceId%3Dbcc164f4-70ef-4288-b5c5-34c63e8c45f0)

### Base de Datos

La aplicación utiliza una base de datos H2 en memoria, por lo que la información persiste mientras la aplicación esté
en ejecución. Las operaciones de lectura y escritura se realizan mediante ORM a través de Hibernate con Spring Data JPA.
El modelo de datos sigue el diagrama mostrado a continuación:
![db-diagram.png](db-diagram.png)