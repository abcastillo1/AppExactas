package com.example.appvinculacion;

public class UsuarioModel {

    private String nombre;
    private String contrasena;

    public UsuarioModel() {
    }

    public UsuarioModel(String nombre,String contrasena) {
        this.nombre = nombre;
        this.contrasena = contrasena;
    }

    public String get_nombre(){
        return nombre;
    }

    public void set_nombre(String nombre){
        this.nombre=nombre;
    }

    public String get_contrasena(){
        return contrasena;
    }

    public void set_contrasena(String contrasena){
        this.contrasena=contrasena;
    }
}
