package com.alura.literalura.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Servicio {
    //Clase para conectar con API
    String json;

    private String conexionAPI(String conexionUrl){
        System.out.println(conexionUrl);
        System.out.println("Buscando...");

        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(conexionUrl))
                .build();

        HttpResponse<String> response = null;

        try{
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        if(response.statusCode()!=404){
            json = response.body();
        }
        else{
            json = null;
        }
        System.out.println("Status :" + response.statusCode()+"\n");
        return json;
    }
    public String obtenerLibroPorId(String conexionUrl){
        return conexionAPI(conexionUrl);
    }

    public String buscarLibrosPorAutor(String conexionUrl){
        return conexionAPI(conexionUrl);
    }

    public String buscarLibrosPorIdioma(String conexionUrl){
        return conexionAPI(conexionUrl);
    }

    public String buscarAutoresVivosEnDeterminadoAnio(String conexionUrl){
        return conexionAPI(conexionUrl);
    }
}
