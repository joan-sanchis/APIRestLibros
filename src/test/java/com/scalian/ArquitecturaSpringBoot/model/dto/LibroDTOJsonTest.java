package com.scalian.ArquitecturaSpringBoot.model.dto;

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
public class LibroDTOJsonTest {

    @Autowired
    private JacksonTester<LibroDTO> json;

    @MockitoBean
    private LibroRepository libroRepository;

    @Test
    void serializaLibroDTOAJson() throws IOException {
        // Arrange
        LibroDTO libro = new LibroDTO("El Quijote", "Miguel de Cervantes", 863);

        // Act
        JsonContent<LibroDTO> resultado = json.write(libro);

        // Assert
        assertThat(resultado).hasJsonPathStringValue("@.titulo");
        assertThat(resultado).extractingJsonPathStringValue("@.titulo").isEqualTo("El Quijote");
        assertThat(resultado).extractingJsonPathStringValue("@.autor").isEqualTo("Miguel de Cervantes");
        assertThat(resultado).extractingJsonPathNumberValue("@.paginas").isEqualTo(863);
    }

    @Test
    void deserializaJsonALibroDTO() throws IOException {
        // Arrange
        String jsonString = """
                {
                    "titulo": "Cien años de soledad",
                    "autor": "Gabriel García Márquez",
                    "paginas": 417
                }
                """;

        // Act
            LibroDTO result = json.parseObject(jsonString);

        // Assert
        assertThat(result.getTitulo()).isEqualTo("Cien años de soledad");
        assertThat(result.getAutor()).isEqualTo("Gabriel García Márquez");
        assertThat(result.getPaginas()).isEqualTo(417);
    }

    @Test
    void serializaLibroDTOCompleto() throws IOException {
        // Arrange
        LibroDTO libro = new LibroDTO("1984", "George Orwell", 328);

        // Act
        JsonContent<LibroDTO> resultado = json.write(libro);

        // Assert
        assertThat(resultado).isEqualToJson("""
                {
                    "titulo": "1984",
                    "autor": "George Orwell",
                    "paginas": 328
                }
                """);
    }

    @Test
    void deserializaJsonConCamposAdicionales() throws IOException {
        // Arrange - JSON con campo extra (debería ignorarlo)
        String jsonString = """
                {
                    "titulo": "Don Juan Tenorio",
                    "autor": "José Zorrilla",
                    "paginas": 200,
                    "editorial": "Extra"
                }
                """;

        // Act
            LibroDTO result = json.parseObject(jsonString);

        // Assert
        assertThat(result.getTitulo()).isEqualTo("Don Juan Tenorio");
        assertThat(result.getAutor()).isEqualTo("José Zorrilla");
        assertThat(result.getPaginas()).isEqualTo(200);
    }

}
