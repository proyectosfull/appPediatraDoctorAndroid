package com.diazmiranda.juanjose.pediatra.Requests.Model;

public class MedicalConsultation {
    String parentesco, nombre, nombreCompleto;
    int id;

    public MedicalConsultation(String parentesco, String nombre, String nombreCompleto, int id) {
        this.parentesco = parentesco;
        this.nombre = nombre;
        this.nombreCompleto = nombreCompleto;
        this.id = id;
    }

    public String getParentesco() {
        return parentesco;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public int getId() {
        return id;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setId(int id) {
        this.id = id;
    }
}