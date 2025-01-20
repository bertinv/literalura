package com.alura.literalura.principal;

import com.alura.literalura.entity.Autor;
import com.alura.literalura.entity.Libro;
import com.alura.literalura.model.DatosLibro;
import com.alura.literalura.model.ResultadoLibros;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    private final Scanner teclado = new Scanner(System.in);
    private final ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private final ConvierteDatos conversor = new ConvierteDatos();

    private LibroRepository repositorio;

    private AutorRepository autorRepository;

    public Principal(LibroRepository repository) {
        this.repositorio = repository;
    }

    public void muestraElMenu() {
        System.out.println("\n +++++++ Biblioteca +++++++ \n");
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro por titulo
                    2 - Mostrar los autores registrados
                    3 - Mostrar libros por idiomas
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    mostrarAutoresRegistrados();
                case 3:
                    buscarLibrosPorIdioma();
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private void buscarLibroPorTitulo() {
        DatosLibro datos = getDatosLibro();
        Libro libro = new Libro(datos);
        repositorio.save(libro);
        System.out.println(datos);
    }

    private void mostrarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados en la base de datos.");
        } else {
            autores.forEach(autor -> System.out.println("- " + autor));
        }
    }

    private void buscarLibrosPorIdioma() {
        System.out.println("Ingrese un idioma: ");
        var idioma = teclado.nextLine().trim();
        List<Libro> librosPorIdioma = repositorio.findByLanguage(idioma);
        System.out.println("Libros escritos en " + idioma + ":");
        librosPorIdioma.forEach(System.out::println);
    }

    private DatosLibro getDatosLibro() {
        System.out.println("-- Escribe el nombre del libro que deseas buscar --");
        var nombreLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE +"?search="+ nombreLibro.replace(" ", "+"));
        System.out.println(json);
        ResultadoLibros datos = conversor.obtenerDatos(json, ResultadoLibros.class);
        DatosLibro libroBuscado = datos.libros().get(0);
        return libroBuscado;
    }
}
