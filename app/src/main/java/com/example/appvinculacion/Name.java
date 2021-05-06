package com.example.appvinculacion;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Name  {

    private String codigo;
    private String nombre;
    private int estado;


    public Name(String codigo, String nombre, int estado) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getName() {
        return nombre;
    }

    public void setName(String nombre) {
        this.nombre = nombre;
    }

    public int getStatus() {
        return estado;
    }

    public void setStatus(int estado) {
        this.estado = estado;
    }

}