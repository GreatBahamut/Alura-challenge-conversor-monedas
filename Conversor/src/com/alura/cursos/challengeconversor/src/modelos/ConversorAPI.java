package com.alura.cursos.challengeconversor.src.modelos;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class ConversorAPI {
 public static void conversor (String codigo) throws IOException, InterruptedException {
        Scanner teclado = new Scanner(System.in);
        String URL = "https://v6.exchangerate-api.com/v6/0b33f6308ffb02c5b6c2aaa4/latest/";

        System.out.println("que moneda desea ver");

        String URLFinal = URL + codigo;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URLFinal))
                .build();

        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        String json = response.body();
     System.out.println(json);
    }
}