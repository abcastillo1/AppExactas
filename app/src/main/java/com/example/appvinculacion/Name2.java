package com.example.appvinculacion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Name2  {

    private String codigo;
    private String fecha;
    private String horaFin;
    private int estado;

    public Name2(String codigo, String fecha, String horaFin, int estado) {
        this.codigo = codigo;
        this.fecha = fecha;
        this.horaFin = horaFin;
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public int getStatus() {
        return estado;
    }

    public void setStatus(int estado) {
        this.estado = estado;
    }
}