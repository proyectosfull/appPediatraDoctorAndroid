package com.diazmiranda.juanjose.pediatra.Requests.Model;

public class PrescriptionsSpinner {
    private int id;
    private String nombre;

    public PrescriptionsSpinner() {
    }

    public PrescriptionsSpinner(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("%02d", id)).append(" - ").append(nombre);
        return sb.toString();
    }
}