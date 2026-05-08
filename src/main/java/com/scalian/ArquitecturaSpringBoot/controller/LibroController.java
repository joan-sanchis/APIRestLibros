package com.scalian.ArquitecturaSpringBoot.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scalian.ArquitecturaSpringBoot.model.dto.LibroDTO;
import com.scalian.ArquitecturaSpringBoot.model.entity.Libro;
import com.scalian.ArquitecturaSpringBoot.service.LibroService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/apit/libros")
@AllArgsConstructor
public class LibroController {

    private final LibroService libroService;

    @PostMapping
    public Libro crearLibro(@Valid @RequestBody LibroDTO libro) {
        return libroService.crearLibro(libro);
    }

    @GetMapping
    public List<LibroDTO> obtenerLibros(@PageableDefault(size = 10) Pageable pageable) {
        return libroService.obtenerLibros(pageable);
    }

    @GetMapping("/buscar")
    public List<LibroDTO> buscarLibro(@RequestParam String autor) {
        return libroService.buscarLibrosPorAutor(autor);
       
    }

    @GetMapping("/mayores")
    public List<LibroDTO> obtenerLibrosMayores(@RequestParam int paginas) {
       return libroService.obtenerLibrosMayores(paginas);
    }

}
