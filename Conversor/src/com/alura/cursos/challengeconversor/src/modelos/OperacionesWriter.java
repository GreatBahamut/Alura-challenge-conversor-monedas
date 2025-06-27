package com.alura.cursos.challengeconversor.src.modelos;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class OperacionesWriter {
    private static final String archivo = "Historial de operaciones.txt";
    public static void guardarHistorial(List<Operacion> operaciones) {
        try (FileWriter writer = new FileWriter(archivo)) {
            for (int i = 0; i < operaciones.size(); i++) {
                writer.write("[OPERACIÓN " + (i + 1) + "] ");
                writer.write(operaciones.get(i).toString());
            }
            System.out.println("\nHistorial guardado como texto en '" + archivo + "'.");
        } catch (IOException e) {
            System.out.println("Error al guardar historial como texto: " + e.getMessage());
        }
    }
}