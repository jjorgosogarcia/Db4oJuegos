package com.example.sadarik.dbo4juegos;

import java.io.Serializable;

public class Juego implements Comparable<Juego>, Serializable {

    private String titulo, genero, plataforma, prestado;

    public Juego() {
        titulo="";
        genero="";
        plataforma="";
        prestado="";
    }

    public Juego(String titulo, String genero, String plataforma, String prestado) {
        this.titulo = titulo;
        this.genero = genero;
        this.plataforma = plataforma;
        this.prestado = prestado;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public String getPrestado() {
        return prestado;
    }

    public void setPrestado(String prestado) {
        this.prestado = prestado;
    }


    //No se repiten por titulo y genero
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Juego juego = (Juego) o;
        if (!plataforma.equals(juego.plataforma)) return false;
        if (!titulo.equals(juego.titulo)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = titulo.hashCode();
        result = 31 * result + plataforma.hashCode();
        return result;
    }

    //Comparador por titulo
    public int compareTo(Juego juego) {
        String a = getTitulo().toLowerCase();
        String b = juego.getTitulo().toLowerCase();
        return a.compareTo(b);
    }
}
