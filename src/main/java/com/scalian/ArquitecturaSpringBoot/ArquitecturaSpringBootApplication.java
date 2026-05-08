package com.scalian.ArquitecturaSpringBoot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.scalian.ArquitecturaSpringBoot.model.entity.Libro;
import com.scalian.ArquitecturaSpringBoot.repository.LibroRepository;

@SpringBootApplication
public class ArquitecturaSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArquitecturaSpringBootApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(LibroRepository libroRepository) {
        return args -> {
            libroRepository.save(new Libro(null, "El Quijote", "Miguel de Cervantes", 863));
            libroRepository.save(new Libro(null, "Cien años de soledad", "Gabriel García Márquez", 417));
            libroRepository.save(new Libro(null, "La sombra del viento", "Carlos Ruiz Zafón", 565));
        };
    }

}
