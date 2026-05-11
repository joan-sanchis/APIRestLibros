package com.scalian.ArquitecturaSpringBoot.model.entity;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.scalian.ArquitecturaSpringBoot.repository.LibroRepository;

@JsonTest
public class LibroJsonTest {

    @Autowired
    private JacksonTester<Libro> json;

    @MockitoBean
    private LibroRepository libroRepository;

    @Test
    void serializaLibroAJson() throws IOException {
        // Arrange
        Libro libro = new Libro(1L, "El Quijote", "Miguel de Cervantes", 863);

        // Act
        JsonContent<Libro> resultado = json.write(libro);

        // Assert
        assertThat(resultado).extractingJsonPathNumberValue("@.id").isEqualTo(1);
        assertThat(resultado).extractingJsonPathStringValue("@.titulo").isEqualTo("El Quijote");
        assertThat(resultado).extractingJsonPathStringValue("@.autor").isEqualTo("Miguel de Cervantes");
        assertThat(resultado).extractingJsonPathNumberValue("@.paginas").isEqualTo(863);
    }

    @Test
    void deserializaJsonALibro() throws IOException {
        // Arrange
        String jsonString = """
                {
                    "id": 5,
                    "titulo": "Cien años de soledad",
                    "autor": "Gabriel García Márquez",
                    "paginas": 417
                }
                """;

        // Act
            Libro result = json.parseObject(jsonString);

        // Assert
        assertThat(result.getId()).isEqualTo(5L);
        assertThat(result.getTitulo()).isEqualTo("Cien años de soledad");
        assertThat(result.getAutor()).isEqualTo("Gabriel García Márquez");
        assertThat(result.getPaginas()).isEqualTo(417);
    }

    @Test
    void serializaLibroCompletoCicloRedondo() throws IOException {
        // Arrange
        Libro libro = new Libro(10L, "1984", "George Orwell", 328);

        // Act
        JsonContent<Libro> resultado = json.write(libro);

        // Assert
        assertThat(resultado).isEqualToJson("""
                {
                    "id": 10,
                    "titulo": "1984",
                    "autor": "George Orwell",
                    "paginas": 328
                }
                """);
    }

    @Test
    void deserializaJsonSinId() throws IOException {
        // Arrange - JSON sin ID (caso común de inserción)
        String jsonString = """
                {
                    "titulo": "Don Juan Tenorio",
                    "autor": "José Zorrilla",
                    "paginas": 200
                }
                """;

        // Act
            Libro result = json.parseObject(jsonString);

        // Assert
        assertThat(result.getId()).isNull();
        assertThat(result.getTitulo()).isEqualTo("Don Juan Tenorio");
        assertThat(result.getAutor()).isEqualTo("José Zorrilla");
        assertThat(result.getPaginas()).isEqualTo(200);
    }

}
