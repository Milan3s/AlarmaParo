package models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Renovacion {

    private int id;
    private int usuario_id;
    private String usuario;
    private LocalDate fecha_renovacion;
    private LocalDate fecha_siguiente;
    private int dias_antes;
    private String estado;
    private String observaciones;
    private LocalDateTime fecha_creacion;

    public Renovacion() {
    }

    public Renovacion(int id, int usuario_id, String usuario,
            LocalDate fecha_renovacion, LocalDate fecha_siguiente,
            int dias_antes, String estado,
            String observaciones, LocalDateTime fecha_creacion) {

        this.id = id;
        this.usuario_id = usuario_id;
        this.usuario = usuario;
        this.fecha_renovacion = fecha_renovacion;
        this.fecha_siguiente = fecha_siguiente;
        this.dias_antes = dias_antes;
        this.estado = estado;
        this.observaciones = observaciones;
        this.fecha_creacion = fecha_creacion;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public LocalDate getFecha_renovacion() {
        return fecha_renovacion;
    }

    public void setFecha_renovacion(LocalDate fecha_renovacion) {
        this.fecha_renovacion = fecha_renovacion;
    }

    public LocalDate getFecha_siguiente() {
        return fecha_siguiente;
    }

    public void setFecha_siguiente(LocalDate fecha_siguiente) {
        this.fecha_siguiente = fecha_siguiente;
    }

    public int getDias_antes() {
        return dias_antes;
    }

    public void setDias_antes(int dias_antes) {
        this.dias_antes = dias_antes;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(LocalDateTime fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    @Override
    public String toString() {
        return "Renovacion{"
                + "id=" + id
                + ", usuario_id=" + usuario_id
                + ", usuario='" + usuario + '\''
                + ", fecha_renovacion=" + fecha_renovacion
                + ", fecha_siguiente=" + fecha_siguiente
                + ", dias_antes=" + dias_antes
                + ", estado='" + estado + '\''
                + ", observaciones='" + observaciones + '\''
                + ", fecha_creacion=" + fecha_creacion
                + '}';
    }

}