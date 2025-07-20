package com.alura.literalura.controller;

import com.alura.literalura.model.Libro;
import com.alura.literalura.model.ResultadoAPI;
import com.alura.literalura.service.Conversor;
import com.alura.literalura.service.ILibroServicio;
import com.alura.literalura.service.Servicio;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Index {
    private final ILibroServicio libroServicio;
    private final String URL_BASE = "https://gutendex.com/books";

    public Index(ILibroServicio libroServicio){
        this.libroServicio=libroServicio;
    }


    public void mostrarMenu(){

        Servicio servicio = new Servicio();
        Conversor conversor= new Conversor();

        String seleccion="";
        boolean salir=false;
        Scanner entrada = new Scanner(System.in);

        List<Libro> librosEncontrados= new ArrayList<>();

        while(!salir)
        {
            System.out.println("""
                1.-Encontrar libro por ID
                2.-Listar libros guardados
                3.-Buscar libros por autor
                4.-Buscar libros por idioma
                5.-Buscar Autores vivos en determinado año
                6.-Buscar libro por parte del titulo
                0.-Salir
                """);

            seleccion = entrada.nextLine();

            switch (seleccion){
                case "0":{
                    System.out.println("Has salido de la aplicación");
                    salir=true;
                }
                break;

                case "1":{
                    System.out.println("ID: ");
                    String idLibro= entrada.nextLine();

                    String conexionUrl = URL_BASE+"/"+idLibro+"/";

                    String respuesta = servicio.obtenerLibroPorId(conexionUrl);

                    if(respuesta!=null){
                        Libro libro = conversor.obtenerDatos(respuesta, Libro.class);
                        if(revisarPorDuplicados(librosEncontrados, idLibro)){
                            System.out.println("Este libro ya se encuentra en la lista");
                        }else{
                            System.out.println(libro.toString());
                            System.out.println("¿Desea guardar este libro? \n1.-Sí\n2.-No");
                            seleccion = entrada.nextLine();
                            if(seleccion.equals("1")){
                                librosEncontrados.add(libro);
                                libroServicio.save(libro);
                            }
                        }
                    }else{
                        System.out.println("No se encontró nada");
                    }
                }
                break;

                case "2": {
                    libroServicio.findAll().stream()
                            .forEach(l-> System.out.println(l));
                }
                break;

                case "3":{
                    System.out.println("Autor: ");
                    String autor = entrada.nextLine().trim().replace(" ", "%20");

                    String conexionUrl=URL_BASE+"?search="+autor;

                    String respuesta = servicio.buscarLibrosPorAutor(conexionUrl);
                    if(respuesta!=null){
                        ResultadoAPI resultado = conversor.obtenerDatos(respuesta, ResultadoAPI.class);
                        resultado.getLibros().stream()
                                .forEach(l-> System.out.println(l));

                        navegarPaginas(resultado, entrada, respuesta, servicio, conversor, "libros");


                    }else{
                        System.out.println("No se encontró nada");
                    }
                }
                break;

                case "4":{
                    String sel ="";
                    String idioma="en";
                    System.out.println("""
                            1.-Español
                            2.-Inglés
                            3.-Francés
                            """);

                    sel = entrada.nextLine();

                    switch (sel){
                        case "1":{
                            idioma="es";
                        }
                        break;

                        case "2":{
                            idioma="en";
                        }
                        break;

                        case "3":{
                            idioma="fr";
                        }
                        break;

                        default:{
                            System.out.println("No se encontró el idioma");
                        }
                        break;
                    }

                    String conexionUrl=URL_BASE+"?languages="+idioma;

                    String respuesta = servicio.buscarLibrosPorIdioma(conexionUrl);
                    ResultadoAPI resultado = conversor.obtenerDatos(respuesta, ResultadoAPI.class);
                    resultado.getLibros().stream()
                            .forEach(l-> System.out.println(l));

                    navegarPaginas(resultado, entrada, respuesta, servicio, conversor, "libros");
                }

                break;

                case "5":{
                    System.out.println("Año: ");
                    String anio = Integer.toString(entrada.nextInt()).trim();
                    entrada.nextLine(); //Absorbe el salto de linea del nextInt que tiene por defecto

                    String conexionUrl=URL_BASE+"?author_year_start="+anio;

                    String respuesta = servicio.buscarAutoresVivosEnDeterminadoAnio(conexionUrl);

                    ResultadoAPI resultado = conversor.obtenerDatos(respuesta, ResultadoAPI.class);
                    resultado.getLibros().stream()
                            .forEach(a-> a.getAutores()
                                    .stream().forEach(d-> System.out.println(d.toString())));

                    navegarPaginas(resultado, entrada, respuesta, servicio, conversor, "autores");

                }

                case "6":{
                    System.out.println("Titulo: ");
                    String titulo = entrada.nextLine().trim().replace(" ", "%20");

                    String conexionUrl=URL_BASE+"?search="+titulo;

                    String respuesta = servicio.buscarLibrosPorAutor(conexionUrl);
                    if(respuesta!=null){
                        ResultadoAPI resultado = conversor.obtenerDatos(respuesta, ResultadoAPI.class);
                        resultado.getLibros().stream()
                                .forEach(l-> System.out.println(l));

                        navegarPaginas(resultado, entrada, respuesta, servicio, conversor, "libros");

                    }else{
                        System.out.println("No se encontró nada");
                    }
                }

                default: {
                    System.out.println("Selecciona una opción válida");
                }
                break;

        }
        }
    }

    private boolean revisarPorDuplicados(List<Libro> lista, String libroId){
        boolean respuesta=false;

        for(int i =0; i<lista.size(); i++){
            if(lista.get(i).getId()== Float.parseFloat(libroId)){
                respuesta=true;
                break;
            }else{
                respuesta=false;
            }
        }

        return respuesta;
    }

    private void navegarPaginas(ResultadoAPI resultado, Scanner entrada, String respuesta, Servicio servicio, Conversor conversor, String seccion){
        boolean continuarExplorando=true;

        while(continuarExplorando){
            String siguiente = resultado.getNext();
            String anterior = resultado.getPrevious();

            System.out.println("0.-Volver al menú principal");

            if(siguiente!=null){
                System.out.println("1.-Página siguiente");
            }
            if(anterior!=null){
                System.out.println("2.-Página Anterior");
            }

            String paginaSel=entrada.nextLine();

            switch (paginaSel){
                case "0":{
                    System.out.println("Volviendo al menú principal\n");
                    continuarExplorando=false;
                }
                break;

                case "1":{
                    if(siguiente==null){
                        System.out.println("No hay nada que mostrar");
                    }else{
                        respuesta = servicio.buscarLibrosPorIdioma(siguiente);
                        resultado = conversor.obtenerDatos(respuesta, ResultadoAPI.class);
                        if(seccion=="libros"){
                            resultado.getLibros().stream()
                                    .forEach(l-> System.out.println(l));
                        }
                        if(seccion=="autores"){
                            resultado.getLibros().stream()
                                    .forEach(a-> a.getAutores()
                                            .stream().forEach(d-> System.out.println(d.toString())));
                        }

                    }
                }
                break;

                case "2":{
                    if(anterior==null){
                        System.out.println("No hay nada que mostrar");
                    }else{
                        respuesta = servicio.buscarLibrosPorIdioma(anterior);
                        resultado = conversor.obtenerDatos(respuesta, ResultadoAPI.class);
                        resultado.getLibros().stream()
                                .forEach(l-> System.out.println(l));
                    }
                }
                break;

                default:{
                    System.out.println("Selecciona una opción válida");
                }
            }
        }
    }


}
