package com.alura.cursos.challengeconversor.src.modelos;

public class Operacion {
    private String monedaActual;
    private String monedaDestino;
    private double monto;

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

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Operacion(String monedaActual, String monedaDestino, double monto) {
        this.monedaActual = monedaActual;
        this.monedaDestino = monedaDestino;
        this.monto = monto;
    }
    @Override
    public String toString(){
        return "(Convertidos $" + monto + " '"+monedaActual+"' a " + getMonedaDestino()+")";
    }
}
