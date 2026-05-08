package com.scalian.ArquitecturaSpringBoot.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.scalian.ArquitecturaSpringBoot.model.dto.LibroDTO;
import com.scalian.ArquitecturaSpringBoot.model.entity.Libro;
import com.scalian.ArquitecturaSpringBoot.repository.LibroRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LibroService {

    private final LibroRepository libroRepository;

    public Libro crearLibro(LibroDTO libroDTO) {
        Libro libro = new Libro(null, libroDTO.getTitulo(), libroDTO.getAutor(), libroDTO.getPaginas());
        return libroRepository.save(libro);
    }

    public List<LibroDTO> obtenerLibros() {
        List<Libro> libros = libroRepository.findAll();
        return libros.stream()
                .map(libro -> new LibroDTO(libro.getTitulo(), libro.getAutor(), libro.getPaginas()))
                .collect(Collectors.toList());
    }

    public List<LibroDTO> buscarLibrosPorAutor(String autor) {
        List<Libro> libros = libroRepository.findByAutorContaining(autor);
        return libros.stream()
                .map(libro -> new LibroDTO(libro.getTitulo(), libro.getAutor(), libro.getPaginas()))
                .collect(Collectors.toList());
    }

    public List<LibroDTO> obtenerLibrosMayores(int paginas) {
        List<Libro> libros = libroRepository.findByPaginasGreaterThan(paginas);
        return libros.stream()
                .map(libro -> new LibroDTO(libro.getTitulo(), libro.getAutor(), libro.getPaginas()))
                .collect(Collectors.toList());
    }

}
