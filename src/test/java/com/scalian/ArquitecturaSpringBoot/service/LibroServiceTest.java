package com.scalian.ArquitecturaSpringBoot.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.convention.TestBean;

import com.scalian.ArquitecturaSpringBoot.model.dto.LibroDTO;
import com.scalian.ArquitecturaSpringBoot.model.entity.Libro;
import com.scalian.ArquitecturaSpringBoot.repository.LibroRepository;

@ExtendWith(MockitoExtension.class)
public class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository;

    @InjectMocks
    private LibroService libroService;

    @Test
    public void crearLibroExitosamente() {
        // Arrange
        LibroDTO libroDTO = new LibroDTO("El Quijote", "Miguel de Cervantes", 863);
        Libro libroGuardado = new Libro(1L, "El Quijote", "Miguel de Cervantes", 863);
        when(libroRepository.save(any(Libro.class))).thenReturn(libroGuardado);

        // Act
        Libro resultado = libroService.crearLibro(libroDTO);

        // Assert
        verify(libroRepository).save(any(Libro.class));
        assertAll(
                () -> assertNotNull(resultado),
                () -> assertEquals("El Quijote", resultado.getTitulo()),
                () -> assertEquals("Miguel de Cervantes", resultado.getAutor()),
                () -> assertEquals(863, resultado.getPaginas()));
    }

    @Test
    public void buscarPorAutorDelegaEnRepositorio() {
        // Arrange
        String autor = "Cervantes";
        List<Libro> libros = List.of(new Libro(1L, "El Quijote", autor, 863));
        when(libroRepository.findByAutorContaining(autor)).thenReturn(libros);

        // Act
        List<LibroDTO> resultado = libroService.buscarLibrosPorAutor(autor);

        // Assert
        verify(libroRepository).findByAutorContaining(autor);
        assertAll(
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("El Quijote", resultado.get(0).getTitulo()),
                () -> assertEquals(autor, resultado.get(0).getAutor()),
                () -> assertEquals(863, resultado.get(0).getPaginas()));
    }

    @Test
    public void buscarPorAutorSinResultadosRetornaListaVacia() {
        // Arrange
        String autor = "Autor inexistente";
        when(libroRepository.findByAutorContaining(autor)).thenReturn(List.of());

        // Act
        List<LibroDTO> resultado = libroService.buscarLibrosPorAutor(autor);

        // Assert
        verify(libroRepository).findByAutorContaining(autor);
        assertTrue(resultado.isEmpty());
    }

}
