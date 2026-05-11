package com.scalian.ArquitecturaSpringBoot.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalian.ArquitecturaSpringBoot.model.dto.LibroDTO;
import com.scalian.ArquitecturaSpringBoot.model.entity.Libro;
import com.scalian.ArquitecturaSpringBoot.repository.LibroRepository;
import com.scalian.ArquitecturaSpringBoot.service.LibroService;

@WebMvcTest(LibroController.class)
public class LibroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LibroService libroService;

    // Necesario porque ArquitecturaSpringBootApplication define un @Bean CommandLineRunner
    // que depende de LibroRepository, el cual no está disponible en el slice @WebMvcTest
    @MockitoBean
    private LibroRepository libroRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void postLibroValidoRetorna201() throws Exception {
        // Arrange
        LibroDTO libroDTO = new LibroDTO("Clean Code", "Robert C. Martin", 464);
        Libro libroGuardado = new Libro(1L, "Clean Code", "Robert C. Martin", 464);
        when(libroService.crearLibro(any(LibroDTO.class))).thenReturn(libroGuardado);

        // Act + Assert
        mockMvc.perform(post("/api/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(libroDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Clean Code"))
                .andExpect(jsonPath("$.autor").value("Robert C. Martin"))
                .andExpect(jsonPath("$.paginas").value(464));
    }

    @Test
    public void postLibroInvalidoRetorna400() throws Exception {
        // Arrange — título vacío y páginas 0, violando @NotBlank y @Min(1)
        LibroDTO libroInvalido = new LibroDTO("", "Autor", 0);

        // Act + Assert
        mockMvc.perform(post("/api/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(libroInvalido)))
                .andExpect(status().isBadRequest());

        verify(libroService, never()).crearLibro(any());
    }

    @Test
    public void getLibrosBuscarPorAutorRetorna200() throws Exception {
        // Arrange
        String autor = "martin";
        List<LibroDTO> libros = List.of(
                new LibroDTO("Clean Code", "Robert C. Martin", 464),
                new LibroDTO("The Clean Coder", "Robert C. Martin", 256));
        when(libroService.buscarLibrosPorAutor(autor)).thenReturn(libros);

        // Act + Assert
        mockMvc.perform(get("/api/libros/buscar")
                .param("autor", autor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].titulo").value("Clean Code"))
                .andExpect(jsonPath("$[1].titulo").value("The Clean Coder"));
    }

    @Test
    public void getLibrosRetorna200ConPaginable() throws Exception {
        // Arrange
        List<LibroDTO> libros = List.of(
                new LibroDTO("El Quijote", "Miguel de Cervantes", 863),
                new LibroDTO("Don Juan Tenorio", "José Zorrilla", 200));
        when(libroService.obtenerLibros(any())).thenReturn(libros);

        // Act + Assert
        mockMvc.perform(get("/api/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].titulo").value("El Quijote"))
                .andExpect(jsonPath("$[1].titulo").value("Don Juan Tenorio"));

        verify(libroService).obtenerLibros(any());
    }

    @Test
    public void getLibrosMayoresRetorna200() throws Exception {
        // Arrange
        int paginas = 500;
        List<LibroDTO> libros = List.of(
                new LibroDTO("El Quijote", "Miguel de Cervantes", 863),
                new LibroDTO("Cien años de soledad", "Gabriel García Márquez", 417));
        when(libroService.obtenerLibrosMayores(paginas)).thenReturn(libros);

        // Act + Assert
        mockMvc.perform(get("/api/libros/mayores")
                .param("paginas", String.valueOf(paginas)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].titulo").value("El Quijote"))
                .andExpect(jsonPath("$[0].paginas").value(863));

        verify(libroService).obtenerLibrosMayores(paginas);
    }

    @Test
    public void getLibrosBuscarSinResultadosRetorna200VacioOK() throws Exception {
        // Arrange
        String autor = "noexiste";
        when(libroService.buscarLibrosPorAutor(autor)).thenReturn(List.of());

        // Act + Assert
        mockMvc.perform(get("/api/libros/buscar")
                .param("autor", autor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        }

        @Test
        public void getLibrosMayoresdosSinResultadosRetorna200Empty() throws Exception {
                // Arrange
                int paginas = 999;
                when(libroService.obtenerLibrosMayores(paginas)).thenReturn(List.of());

                // Act + Assert
                mockMvc.perform(get("/api/libros/mayores")
                                .param("paginas", String.valueOf(paginas)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(0));

                verify(libroService).obtenerLibrosMayores(paginas);
        }

        @Test
        public void getLibrosConPaginableConParametros() throws Exception {
                // Arrange
                List<LibroDTO> libros = List.of(
                                new LibroDTO("Prueba", "Autor Prueba", 100));
                when(libroService.obtenerLibros(any())).thenReturn(libros);

                // Act + Assert
                mockMvc.perform(get("/api/libros")
                                .param("page", "0")
                                .param("size", "20"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(1));
        }

}
