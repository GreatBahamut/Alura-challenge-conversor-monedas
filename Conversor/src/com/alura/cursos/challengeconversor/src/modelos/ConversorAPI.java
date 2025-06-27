package com.alura.cursos.challengeconversor.src.modelos;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class ConversorAPI {
 public static double tasaUSD (String codigo) throws IOException, InterruptedException {
        Scanner teclado = new Scanner(System.in);
        String URL = "https://v6.exchangerate-api.com/v6/0b33f6308ffb02c5b6c2aaa4/latest/";

        String URLFinal = URL + codigo;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URLFinal))
                .build();

        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        RespuestaAPI resultado = gson.fromJson(response.body(), RespuestaAPI.class);

        return resultado.getUSD();
    }
}