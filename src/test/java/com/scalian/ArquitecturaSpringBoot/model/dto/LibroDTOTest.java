package com.scalian.ArquitecturaSpringBoot.model.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class LibroDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void dtoValidoNoTieneViolaciones() {
        // Arrange
        LibroDTO libroDTO = new LibroDTO("El Quijote", "Miguel de Cervantes", 863);

        // Act
        Set<ConstraintViolation<LibroDTO>> violaciones = validator.validate(libroDTO);

        // Assert
        assertTrue(violaciones.isEmpty());
    }

    @Test
    public void tituloVacioGeneraViolacion() {
        // Arrange
        LibroDTO libroDTO = new LibroDTO(" ", "Miguel de Cervantes", 863);

        // Act
        Set<ConstraintViolation<LibroDTO>> violaciones = validator.validate(libroDTO);

        // Assert
        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("titulo")));
    }

    @Test
    public void tituloNuloGeneraViolacion() {
        // Arrange
        LibroDTO libroDTO = new LibroDTO(null, "Miguel de Cervantes", 863);

        // Act
        Set<ConstraintViolation<LibroDTO>> violaciones = validator.validate(libroDTO);

        // Assert
        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("titulo")));
    }

    @Test
    public void paginasCeroGeneraViolacion() {
        // Arrange
        LibroDTO libroDTO = new LibroDTO("El Quijote", "Miguel de Cervantes", 0);

        // Act
        Set<ConstraintViolation<LibroDTO>> violaciones = validator.validate(libroDTO);

        // Assert
        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("paginas")));
    }

    @Test
    public void paginasNegativasGeneraViolacion() {
        // Arrange
        LibroDTO libroDTO = new LibroDTO("El Quijote", "Miguel de Cervantes", -5);

        // Act
        Set<ConstraintViolation<LibroDTO>> violaciones = validator.validate(libroDTO);

        // Assert
        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("paginas")));
    }

    @Test
    public void variosErroresSimultaneos() {
        // Arrange
        LibroDTO libroDTO = new LibroDTO("", null, 0);

        // Act
        Set<ConstraintViolation<LibroDTO>> violaciones = validator.validate(libroDTO);

        // Assert
        assertTrue(violaciones.size() >= 3);
    }

}
