package com.jonathanlnb.lynxs.TDA;

import android.graphics.Bitmap;

public class Contacto {
    private Integer id;
    private Bitmap imagen;
    private String nombre;
    private String telefono;
    private String correo;

    public Contacto(Integer id, Bitmap imagen, String nombre, String telefono, String correo) {
        this.id = id;
        this.imagen = imagen;
        this.nombre = nombre;
        telefono = telefono.replace("+521", "");
        this.telefono = telefono;
        this.correo = correo;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public Integer getId() {
        return id;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }
}
