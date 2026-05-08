package com.scalian.ArquitecturaSpringBoot.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Libro {
    
    @Id
    @GeneratedValue
    private Long id;

    
    private String titulo;
    private String autor;
    private int paginas;

}
