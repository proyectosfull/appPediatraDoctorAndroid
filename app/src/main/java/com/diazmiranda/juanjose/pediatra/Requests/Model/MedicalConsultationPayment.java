package com.diazmiranda.juanjose.pediatra.Requests.Model;

public class MedicalConsultationPayment {

    String nombre, fecRegistro, nombreCompleto, nombrePaciente;
    int idDependiente, idPayment;

    public MedicalConsultationPayment(String nombre, String fecRegistro, String nombreCompleto, String nombrePaciente, int idDependiente, int idPayment) {
        this.nombre = nombre;
        this.fecRegistro = fecRegistro;
        this.nombreCompleto = nombreCompleto;
        this.nombrePaciente = nombrePaciente;
        this.idDependiente = idDependiente;
        this.idPayment = idPayment;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecRegistro() {
        return fecRegistro;
    }

    public void setFecRegistro(String fecRegistro) {
        this.fecRegistro = fecRegistro;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public int getIdDependiente() {
        return idDependiente;
    }

    public void setIdDependiente(int idUser) {
        this.idDependiente = idUser;
    }

    public int getIdPayment() {
        return idPayment;
    }

    public void setIdPayment(int idPayment) {
        this.idPayment = idPayment;
    }
}