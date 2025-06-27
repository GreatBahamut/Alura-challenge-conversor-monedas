package com.alura.cursos.challengeconversor.src.modelos;

public class Operacion {
    private String monedaActual;
    private String monedaDestino;
    private double montoAcutal;
    private double montoDestino;

    public String getMonedaActual() {
        return monedaActual;
    }

    public void setMonedaActual(String monedaActual) {
        this.monedaActual = monedaActual;
    }

    public String getMonedaDestino() {
        return monedaDestino;
    }

    public void setMonedaDestino(String monedaDestino) {
        this.monedaDestino = monedaDestino;
    }

    public double getMontoActual() {
        return montoAcutal;
    }

    public double getMontoDestino() {
        return montoDestino;
    }

    public void setMontoDestino(double montoDestino) {
        this.montoDestino = montoDestino;
    }

    public void setMonto(double monto) {
        this.montoAcutal = monto;
    }

    public Operacion(String monedaActual, String monedaDestino, double monto) {
        this.monedaActual = monedaActual;
        this.monedaDestino = monedaDestino;
        this.montoAcutal = monto;
    }
    @Override
    public String toString(){
        return "Convertidos $" + montoAcutal + " '" + monedaActual + "' a $" + montoDestino + " '" + monedaDestino + "'\n";
    }
}
