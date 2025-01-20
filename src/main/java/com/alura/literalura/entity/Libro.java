package com.alura.literalura.entity;

import com.alura.literalura.model.DatosLibro;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="Libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer libroId;
    @Column(unique = true)
    private String titulo;
    private String idioma;
    private Integer numeroDescargas;

    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Autor> autores;

    public Integer getLibroId() {
        return libroId;
    }

    public void setLibroId(Integer libroId) {
        this.libroId = libroId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Integer numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        autores.forEach(e -> e.setLibro(this));
        this.autores = autores;
    }

    public Libro(DatosLibro datos) {
        this.titulo = datos.titulo();
        try {
            this.idioma = datos.idiomas().get(0);
        } catch (NullPointerException e) {
            this.idioma = "";
        }
        this.numeroDescargas = datos.numeroDescargas();
    }

    @Override
    public String toString() {
        return  ", titulo='" + titulo + '\'' +
                ", idioma='" + idioma + '\'' +
                ", numeroDescargas=" + numeroDescargas + '\'' +
                ", autor=" + autores;
    }
}