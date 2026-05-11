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
import org.springframework.data.domain.PageImpl;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Test
    public void obtenerLibrosConPageableRetornaLibrosDTO() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Libro> libros = List.of(
                new Libro(1L, "El Quijote", "Miguel de Cervantes", 863),
                new Libro(2L, "Don Juan Tenorio", "José Zorrilla", 200));
        PageImpl<Libro> page = new PageImpl<>(libros, pageable, 2);
        when(libroRepository.findAll(pageable)).thenReturn(page);

        // Act
        List<LibroDTO> resultado = libroService.obtenerLibros(pageable);

        // Assert
        verify(libroRepository).findAll(pageable);
        assertAll(
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("El Quijote", resultado.get(0).getTitulo()),
                () -> assertEquals("Don Juan Tenorio", resultado.get(1).getTitulo()));
    }

    @Test
    public void obtenerLibrosConPageableVaciaRetornaListaVacia() {
        // Arrange
        Pageable pageable = PageRequest.of(5, 10);
        PageImpl<Libro> emptyPage = new PageImpl<>(List.of(), pageable, 0);
        when(libroRepository.findAll(pageable)).thenReturn(emptyPage);

        // Act
        List<LibroDTO> resultado = libroService.obtenerLibros(pageable);

        // Assert
        verify(libroRepository).findAll(pageable);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void obtenerLibrosMayoresPaginasRetornaLibrosMayores() {
        // Arrange
        int paginas = 500;
        List<Libro> libros = List.of(
                new Libro(1L, "El Quijote", "Miguel de Cervantes", 863),
                new Libro(2L, "Cien años de soledad", "Gabriel García Márquez", 417));
        when(libroRepository.findByPaginasGreaterThan(paginas)).thenReturn(libros);

        // Act
        List<LibroDTO> resultado = libroService.obtenerLibrosMayores(paginas);

        // Assert
        verify(libroRepository).findByPaginasGreaterThan(paginas);
        assertAll(
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("El Quijote", resultado.get(0).getTitulo()),
                () -> assertEquals(863, resultado.get(0).getPaginas()));
    }

    @Test
    public void obtenerLibrosMayoresSinResultadosRetornaListaVacia() {
        // Arrange
        int paginas = 1000;
        when(libroRepository.findByPaginasGreaterThan(paginas)).thenReturn(List.of());

        // Act
        List<LibroDTO> resultado = libroService.obtenerLibrosMayores(paginas);

        // Assert
        verify(libroRepository).findByPaginasGreaterThan(paginas);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void crearLibroVerificaInteraccionConRepositorio() {
        // Arrange
        LibroDTO libroDTO = new LibroDTO("Refactoring", "Martin Fowler", 454);
        Libro libroGuardado = new Libro(3L, "Refactoring", "Martin Fowler", 454);
        when(libroRepository.save(any(Libro.class))).thenReturn(libroGuardado);

        // Act
        Libro resultado = libroService.crearLibro(libroDTO);

        // Assert
        ArgumentCaptor<Libro> captor = ArgumentCaptor.forClass(Libro.class);
        verify(libroRepository).save(captor.capture());
        Libro libroCapturado = captor.getValue();
        assertAll(
                () -> assertNull(libroCapturado.getId()),
                () -> assertEquals("Refactoring", libroCapturado.getTitulo()),
                () -> assertEquals("Martin Fowler", libroCapturado.getAutor()),
                () -> assertEquals(454, libroCapturado.getPaginas()));
    }

}
