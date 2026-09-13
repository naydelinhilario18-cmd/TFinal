package com.example.tfinal;

public class RegistroModelo {
    private String tipo_residuo;
    private String categoria;
    private double cantidad;
    private String ubicacion;

    public RegistroModelo(String tipo_residuo, String categoria, double cantidad, String ubicacion) {
        this.tipo_residuo = tipo_residuo;
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.ubicacion = ubicacion;
    }

    // Getters y Setters
    public String getTipo_residuo() { return tipo_residuo; }
    public void setTipo_residuo(String tipo_residuo) { this.tipo_residuo = tipo_residuo; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
}