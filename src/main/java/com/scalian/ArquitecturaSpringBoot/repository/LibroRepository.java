package com.scalian.ArquitecturaSpringBoot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.scalian.ArquitecturaSpringBoot.model.entity.Libro;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    List<Libro> findByAutorContaining(String autor);

    @Query("SELECT l FROM Libro l WHERE l.paginas > :paginas")
    List<Libro> findByPaginasGreaterThan(int paginas);

}
