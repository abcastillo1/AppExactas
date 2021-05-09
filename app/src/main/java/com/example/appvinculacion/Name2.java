package com.example.appvinculacion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Name2  {

    private String codigo;
    private String fecha;
    private String horaInicio;
    private String horaFin;
    private String foto;
    private String longitud;
    private String latitud;
    private int estado;

    public Name2(String codigo, String fecha, String horaInicio, String horaFin, String foto, String longitud, String latitud, int estado) {
        this.codigo = codigo;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.foto = foto;
        this.longitud = longitud;
        this.latitud = latitud;
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

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public int getStatus() {
        return estado;
    }

    public void setStatus(int estado) {
        this.estado = estado;
    }
}