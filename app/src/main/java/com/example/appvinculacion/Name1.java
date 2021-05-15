package com.example.appvinculacion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Name1  {

    private String codigo;
    private String horaFin;
    private int estado;

    public Name1(String codigo, String horaFin, int estado) {
        this.codigo = codigo;
        this.horaFin = horaFin;
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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