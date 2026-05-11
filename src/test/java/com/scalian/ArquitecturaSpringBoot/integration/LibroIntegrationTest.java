package com.scalian.ArquitecturaSpringBoot.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.scalian.ArquitecturaSpringBoot.model.dto.LibroDTO;
import com.scalian.ArquitecturaSpringBoot.model.entity.Libro;
import com.scalian.ArquitecturaSpringBoot.repository.LibroRepository;
import com.scalian.ArquitecturaSpringBoot.service.LibroService;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers(disabledWithoutDocker = true)
public class LibroIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("postgres")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);

    }

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private LibroService libroService;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void cleanDatabase() {
        libroRepository.deleteAll();
    }

    @Test
    void crearYRecuperarLibroViaRepository() {
        // Arrange
        Libro libro = new Libro(null, "Repo Book", "Autor Repo", 111);

        // Act
        Libro guardado = libroRepository.save(libro);
        Libro recuperado = libroRepository.findById(guardado.getId()).orElseThrow();

        // Assert
        assertAll(
                () -> assertNotNull(guardado.getId()),
                () -> assertEquals("Repo Book", recuperado.getTitulo()),
                () -> assertEquals("Autor Repo", recuperado.getAutor()),
                () -> assertEquals(111, recuperado.getPaginas()));
    }

    @Test
    void crearYRecuperarLibroViaService() {
        // Arrange
        LibroDTO libroDTO = new LibroDTO("Service Book", "Autor Service", 222);

        // Act
        Libro guardado = libroService.crearLibro(libroDTO);
        Libro recuperado = libroRepository.findById(guardado.getId()).orElseThrow();

        // Assert
        assertAll(
                () -> assertNotNull(guardado.getId()),
                () -> assertEquals("Service Book", recuperado.getTitulo()),
                () -> assertEquals("Autor Service", recuperado.getAutor()),
                () -> assertEquals(222, recuperado.getPaginas()));
    }

    @Test
    void crearYRecuperarLibroViaApi() {
        // Arrange
        LibroDTO libroDTO = new LibroDTO("API Book", "Autor API", 333);

        // Act
        ResponseEntity<Libro> response = restTemplate.postForEntity("/api/libros", libroDTO, Libro.class);
        Libro body = response.getBody();

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertNotNull(body),
                () -> assertNotNull(body.getId()),
                () -> assertEquals("API Book", body.getTitulo()));

        Libro recuperado = libroRepository.findById(body.getId()).orElseThrow();
        assertAll(
                () -> assertEquals("API Book", recuperado.getTitulo()),
                () -> assertEquals("Autor API", recuperado.getAutor()),
                () -> assertEquals(333, recuperado.getPaginas()));
    }

}
