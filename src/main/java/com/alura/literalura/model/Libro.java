package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name="libros")
public class Libro {
    @Id
    private Long id;
    @JsonAlias("title")
    private String titulo;
    @JsonAlias("authors")
    @ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Autor> autores;
    @JsonAlias("languages")
    private List<String> idiomas;
    @JsonAlias("download_count")
    private Long descargas;

    public Libro(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public Long getDescargas() {
        return descargas;
    }

    public void setDescargas(Long descargas) {
        this.descargas = descargas;
    }

    @Override
    public String toString() {
        String nombresAutores="";

        for(int i=0; i<autores.size(); i++){
            nombresAutores += autores.get(i).getNombre()+" ";
        }

        return """
                ID: %s
                Título: %s
                Autor(es): %s
                Idiomas: %s
                Descargas: %s
                """.formatted(this.id, this.titulo, nombresAutores, this.idiomas, this.descargas);
    }
}
