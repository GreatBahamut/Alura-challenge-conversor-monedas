package com.alura.cursos.challengeconversor.src.modelos;

import java.util.ArrayList;
import java.util.Scanner;

public class Interfaz {
    private final Scanner teclado = new Scanner(System.in);
    private final String[] monedasPrincipales = {"ARS", "USD", "PEN", "BRL", "EUR", "JPY", "CNY", "GBP"};
    private final ArrayList<Operacion> operaciones = new ArrayList<>();

    private final String menuMonedas = """
              1. PESO ARGENTINO
              2. DÓLAR ESTADOUNIDENSE
              3. SOL PERUANO
              4. REAL BRASILEÑO
              5. EURO
              6. YEN JAPONÉS
              7. YUAN CHINO
              8. LIBRA ESTERLINA
              9. OTRA (Ingresar código ISO)
              0. SALIR
              """;

    public void inicio() {
        while (true) {
            String moneda1 = null;
            String moneda2 = null;
            double monto = 0.0;

            System.out.println("********** Bienvenido al conversor de monedas **********");
            while (moneda1 == null) {
                System.out.println("Seleccione la moneda de origen o '0' para salir:");
                System.out.println(menuMonedas);
                int respuesta1 = leerOpcion();
                if (respuesta1 == 0) {
                    System.out.println("Saliendo del sistema. Gracias por usar el conversor.");
                    mostrarHistorial();
                    return;
                }
                moneda1 = obtenerMonedaDesdeSeleccion(respuesta1);
            }

            boolean montoValido = false;
            while (!montoValido) {
                monto = pedirMonto(moneda1);
                System.out.println("¿Es correcto este monto? " + monto + " (s/n)");
                String confirmacion = teclado.nextLine().trim().toLowerCase();
                if (confirmacion.equals("s")) {
                    montoValido = true;
                }
            }

            while (moneda2 == null) {
                System.out.println("Seleccione la moneda destino:");
                System.out.println(menuMonedas);
                int respuesta2 = leerOpcion();
                if (respuesta2 == 0) {
                    System.out.println("Cancelando operación actual.");
                    moneda1 = null; // reiniciar para permitir corrección
                    break;
                }
                moneda2 = obtenerMonedaDesdeSeleccion(respuesta2);
            }

            if (moneda1 != null && moneda2 != null) {
                registrarOperacion(moneda1, moneda2, monto);
            }

            mostrarHistorial();
            preguntarModificarOperacion();

            System.out.println("¿Desea realizar otra operación? (s/n)");
            String continuar = teclado.nextLine().trim().toLowerCase();
            if (!continuar.equals("s")) {
                mostrarHistorial();
                break;
            }
        }
    }

    private int leerOpcion() {
        try {
            return Integer.parseInt(teclado.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Intente nuevamente.");
            return -1;
        }
    }

    private String obtenerMonedaDesdeSeleccion(int opcion) {
        if (opcion >= 1 && opcion <= monedasPrincipales.length) {
            return monedasPrincipales[opcion - 1];
        } else if (opcion == 9) {
            System.out.println("Ingrese el código ISO de la moneda (ej. CHF, MXN, CLP):");
            return teclado.nextLine().trim().toUpperCase();
        } else {
            System.out.println("Opción inválida.");
            return null;
        }
    }

    private double pedirMonto(String moneda) {
        System.out.printf("Ingrese el monto en %s a convertir: ", moneda);
        try {
            return Double.parseDouble(teclado.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Monto inválido. Intente nuevamente.");
            return pedirMonto(moneda);
        }
    }

    private void registrarOperacion(String moneda1, String moneda2, double monto) {
        Operacion operacion = new Operacion(moneda1, moneda2, monto);
        operaciones.add(operacion);
        System.out.println("Operación registrada: " + moneda1 + " → " + moneda2 + " | Monto: " + monto);
    }

    private void mostrarHistorial() {
        System.out.println("\nOperaciones realizadas:");
        if (operaciones.isEmpty()) {
            System.out.println("(ninguna)");
        } else {
            for (int i = 0; i < operaciones.size(); i++) {
                System.out.println("[" + i + "] " + operaciones.get(i));
            }
        }
    }

    private void preguntarModificarOperacion() {
        if (operaciones.isEmpty()) return;

        System.out.println("¿Desea borrar o editar una operación? Ingrese el número, 'e' para editar, o 'n' para continuar:");
        String entrada = teclado.nextLine().trim().toLowerCase();

        if (entrada.equals("n")) return;

        try {
            if (entrada.startsWith("e")) {
                int indice = Integer.parseInt(entrada.substring(1));
                if (indice >= 0 && indice < operaciones.size()) {
                    Operacion original = operaciones.get(indice);
                    System.out.println("Editando operación: " + original);

                    System.out.println("Nueva moneda origen (actual: " + original.getMonedaDestino() + ") o ENTER para dejar igual:");
                    String nuevaOrigen = teclado.nextLine().trim().toUpperCase();
                    if (nuevaOrigen.isEmpty()) nuevaOrigen = original.getMonedaDestino();

                    System.out.println("Nueva moneda destino (actual: " + original.getMonedaDestino() + ") o ENTER para dejar igual:");
                    String nuevaDestino = teclado.nextLine().trim().toUpperCase();
                    if (nuevaDestino.isEmpty()) nuevaDestino = original.getMonedaDestino();

                    System.out.println("Nuevo monto (actual: " + original.getMonto() + ") o ENTER para dejar igual:");
                    String nuevoMontoStr = teclado.nextLine().trim();
                    double nuevoMonto = nuevoMontoStr.isEmpty() ? original.getMonto() : Double.parseDouble(nuevoMontoStr);

                    operaciones.set(indice, new Operacion(nuevaOrigen, nuevaDestino, nuevoMonto));
                    System.out.println("Operación actualizada correctamente.");
                } else {
                    System.out.println("Índice fuera de rango. No se modificó nada.");
                }
            } else {
                int indice = Integer.parseInt(entrada);
                if (indice >= 0 && indice < operaciones.size()) {
                    Operacion eliminada = operaciones.remove(indice);
                    System.out.println("Operación eliminada: " + eliminada);
                } else {
                    System.out.println("Índice fuera de rango. No se eliminó nada.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. No se realizó ninguna modificación.");
        }
    }
}
