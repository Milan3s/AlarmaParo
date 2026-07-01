package models;

import java.time.LocalDateTime;

public class Usuario {

    private int id;
    private String nombre;
    private String dni;
    private String email;
    private LocalDateTime fecha_alta;

    public Usuario() {
    }

    public Usuario(int id, String nombre, String dni, String email, LocalDateTime fecha_alta) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.email = email;
        this.fecha_alta = fecha_alta;
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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getFecha_alta() {
        return fecha_alta;
    }

    public void setFecha_alta(LocalDateTime fecha_alta) {
        this.fecha_alta = fecha_alta;
    }

    @Override
    public String toString() {
        return nombre;
    }

}