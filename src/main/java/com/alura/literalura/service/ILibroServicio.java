package com.alura.literalura.service;

import com.alura.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ILibroServicio extends JpaRepository<Libro, Long> {

}
