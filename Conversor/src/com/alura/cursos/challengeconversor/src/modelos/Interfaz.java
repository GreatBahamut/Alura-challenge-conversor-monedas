package com.alura.cursos.challengeconversor.src.modelos;

import java.util.ArrayList;
import java.util.Scanner;

import static com.alura.cursos.challengeconversor.src.modelos.OperacionesWriter.guardarHistorial;

public class Interfaz {
    private final Scanner teclado    = new Scanner(System.in);
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
              0. CANCELAR
              """;

    public void inicio() {
        while (true) {
            String moneda1 = null;
            String moneda2 = null;
            double monto = 0.0;

            System.out.println("\n********** Bienvenido al conversor de monedas **********\n");
            while (moneda1 == null) {
                System.out.println("Seleccione la moneda de ORIGEN o '0' para SALIR:\n");
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
                while (true) {
                    System.out.println("¿Es CORRECTO el MONTO? $" + monto);
                    System.out.println("'1' = SÍ | '2' = NO");
                    String confirmacion = teclado.nextLine().trim().toLowerCase();
                    if (confirmacion.equals("1")) {
                        montoValido = true;
                        break;
                    } else if (confirmacion.equals("2")) {
                        break;
                    } else {
                        System.out.println("Entrada inválida. Ingrese '1' o '2'.\n");
                    }
                }
            }

            while (moneda2 == null) {
                System.out.println("Seleccione la moneda DESTINO:");
                System.out.println(menuMonedas);
                int respuesta2 = leerOpcion();
                if (respuesta2 == 0) {
                    System.out.println("CANCELANDO OPERACIÓN actual.");
                    moneda1 = null;
                    mostrarHistorial();
                    break;
                }
                moneda2 = obtenerMonedaDesdeSeleccion(respuesta2);
            }

            if (moneda1 != null && moneda2 != null) {
                System.out.println("\nPor favor, espere...");
                System.out.println("Cargando información del servidor...\n");
                try {
                    double valorOrigen = ConversorAPI.tasaUSD(moneda1);
                    double valorDestino = ConversorAPI.tasaUSD(moneda2);
                    double resultado = monto * (valorOrigen / valorDestino);
                    double equivalenteUnitario = valorOrigen / valorDestino;

                    resultado = Math.round(resultado * 100.0) / 100.0;
                    equivalenteUnitario = Math.round(equivalenteUnitario * 100.0) / 100.0;

                    Operacion operacion = new Operacion(moneda1, moneda2, monto);
                    operacion.setMontoDestino(resultado);
                    operaciones.add(operacion);

                    System.out.println("OPERACIÓN REGISTRADA CON ÉXITO");
                    System.out.println(moneda1 + " → " + moneda2 + " | Monto: " + monto);
                    System.out.println("$1 " + moneda1 + " → $" + equivalenteUnitario + " " + moneda2 + "\n");
                    preguntarModificarOperacion();
                }
                catch (Exception e) {
                    System.out.println("Error al obtener tasa de cambio desde la API: " + e.getMessage());
                }
            }

            while (true) {
                System.out.println("¿Desea realizar otra operación?");
                System.out.println("1 = 'SÍ' | 2 = 'NO'");
                String continuar = teclado.nextLine().trim().toLowerCase();
                if (continuar.equals("1")) break;
                else if (continuar.equals("2")) {
                    mostrarHistorial();
                    guardarHistorial(operaciones);
                    return;
                } else {
                    System.out.println("Entrada inválida. Ingrese '1' o '2'.\n");
                }
            }
        }
    }

    private int leerOpcion() {
        try {
            return Integer.parseInt(teclado.nextLine());
        }
        catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Intente nuevamente.\n");
            return -1;
        }
    }

    private String obtenerMonedaDesdeSeleccion(int opcion) {
        if (opcion >= 1 && opcion <= monedasPrincipales.length) {
            return monedasPrincipales[opcion - 1];
        }
        else if (opcion == 9) {
            System.out.println("Ingrese el código ISO de la moneda (ej. CHF, MXN, CLP):");
            return teclado.nextLine().trim().toUpperCase();
        }
        else {
            return null;
        }
    }

    private double pedirMonto(String moneda) {
        System.out.println("Ingrese el monto en '$" + moneda + "' a convertir.");
        try {
            return Double.parseDouble(teclado.nextLine());
        }
        catch (NumberFormatException e) {
            System.out.println("Monto inválido. Intente nuevamente.\n");
            return pedirMonto(moneda);
        }
    }

    private void mostrarHistorial() {
        System.out.println("\nOperaciones realizadas:");
        if (operaciones.isEmpty()) {
            System.out.println("-NINGUNA-");
        } else {
            for (int i = 0; i < operaciones.size(); i++) {
                Operacion op = operaciones.get(i);
                System.out.println(
                        "[OPERACIÓN : " + (i + 1) + "] " + "(Convertidos : $" + op.getMontoActual() + "'" + op.getMonedaActual() + "' → $" + op.getMontoDestino() +"'" +op.getMonedaDestino() + "')"
                );
            }
        }
    }



    private void preguntarModificarOperacion() {
        while (true){
            if (operaciones.isEmpty()) break;

            System.out.println("\n¿Desea borrar o editar una operación?");
            System.out.println("'1' para EDITAR | '2' para CONTINUAR:");
            String respuesta = teclado.nextLine().trim().toLowerCase();

            if (respuesta.equals("2")) break;

            else if (respuesta.equals("1")) {
                while (true){
                    mostrarHistorial();
                    System.out.println("Ingrese el número de la OPERACIÓN que desea EDITAR o ELIMINAR. O ingrese '0' para CANCELAR");
                    String entrada = teclado.nextLine().trim();
                    if (Integer.parseInt(entrada)<=operaciones.size() && Integer.parseInt(entrada)>0){
                        try {
                            int indice = Integer.parseInt(entrada) - 1;
                            if (indice >= 0 && indice < operaciones.size()) {
                                System.out.println("¿Qué desea hacer con la OPERACIÓN " + entrada + "?");
                                System.out.println("'1' = EDITAR | '2' = ELIMINAR | 'Otro' = CANCELAR");
                                String accion = teclado.nextLine().trim();

                                if (accion.equals("1")) {
                                    Operacion original = operaciones.get(indice);
                                    System.out.println("Editando operación: " + original);

                                    System.out.println("Seleccione la nueva moneda ORIGEN o ENTER para dejar igual:");
                                    System.out.println(menuMonedas);
                                    String nuevaOrigenStr = teclado.nextLine().trim();
                                    String nuevaOrigen = nuevaOrigenStr.isEmpty() ? original.getMonedaActual() : obtenerMonedaDesdeSeleccion(Integer.parseInt(nuevaOrigenStr));

                                    System.out.println("Seleccione la nueva moneda DESTINO o ENTER para dejar igual:");
                                    System.out.println(menuMonedas);
                                    String nuevaDestinoStr = teclado.nextLine().trim();
                                    String nuevaDestino = nuevaDestinoStr.isEmpty() ? original.getMonedaDestino() : obtenerMonedaDesdeSeleccion(Integer.parseInt(nuevaDestinoStr));

                                    System.out.println("Nuevo monto (actual: " + original.getMontoActual() + ") o ENTER para dejar igual:");
                                    String nuevoMontoStr = teclado.nextLine().trim();
                                    double nuevoMonto = nuevoMontoStr.isEmpty() ? original.getMontoActual() : Double.parseDouble(nuevoMontoStr);

                                    System.out.println("\nPor favor espere...");
                                    System.out.println("Cargando información del servidor...\n");

                                    try {
                                        double valorOrigen = ConversorAPI.tasaUSD(nuevaOrigen);
                                        double valorDestino = ConversorAPI.tasaUSD(nuevaDestino);
                                        double resultado = nuevoMonto * (valorOrigen / valorDestino);
                                        resultado = Math.round(resultado * 100.0) / 100.0;

                                        Operacion nuevaOperacion = new Operacion(nuevaOrigen, nuevaDestino, nuevoMonto);
                                        nuevaOperacion.setMontoDestino(resultado);
                                        operaciones.set(indice, nuevaOperacion);

                                        System.out.println("OPERACIÓN EDITADA con ÉXITO");
                                        System.out.println( valorOrigen+ " → " + valorDestino + " | Monto: " + nuevoMonto);
                                        System.out.println("$1 " + valorOrigen + " → $" + resultado + " " + valorDestino + ".\n");
                                        preguntarModificarOperacion();
                                        break;
                                    } catch (Exception e) {
                                        System.out.println("Error al obtener la tasa de cambio: " + e.getMessage());
                                        preguntarModificarOperacion();
                                    }
                                } else if (accion.equals("2")) {
                                    while (true) {
                                        System.out.println("¿Está seguro que desea ELIMINAR la OPERACIÓN " + entrada + "?");
                                        System.out.println("'1' = SÍ | '2' = NO");
                                        String confirmar = teclado.nextLine().trim();
                                        if (confirmar.equals("1")) {
                                            operaciones.remove(indice);
                                            System.out.println("OPERACIÓN ELIMINADA con ÉXITO.\n");
                                            preguntarModificarOperacion();
                                            break;
                                        }
                                        else if (confirmar.equals("2")) {
                                            System.out.println("Eliminación CANCELADA.\n");
                                            break;
                                        }
                                        else {
                                            System.out.println("Entrada inválida. Ingrese '1' para confirmar o '2' para cancelar.\n");
                                        }
                                    }
                                    break;
                                }
                                else{
                                    System.out.println("Acción CANCELADA.\n");
                                }
                            }
                            else {
                                System.out.println("Índice inválido. No se realizó ninguna modificación.\n");
                            }
                        }
                        catch (NumberFormatException e) {
                            System.out.println("Entrada inválida. No se realizó ninguna modificación.\n");
                        }
                    }
                    else if (Integer.parseInt(entrada) == 0){
                        System.out.println("CANCELANDO la selección de OPERACIÓN a EDITAR.");
                        preguntarModificarOperacion();
                        break;
                    }
                     else System.out.println("Índice inválido. Por favor, intente nuevamente con otro índice.\n");
                }
                break;
            }
            else System.out.println("Entrada invlálida.\n");
        }
    }
}