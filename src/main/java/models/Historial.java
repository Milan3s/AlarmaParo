package models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Historial {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final DateTimeFormatter FORMATO_FECHA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private int id;
    private int usuario_id;
    private String renovacion;
    private LocalDate fecha_renovacion;
    private LocalDate fecha_siguiente;
    private String observaciones;
    private LocalDateTime fecha_registro;

    public Historial() {
    }

    public Historial(int id,
            int usuario_id,
            String renovacion,
            LocalDate fecha_renovacion,
            LocalDate fecha_siguiente,
            String observaciones,
            LocalDateTime fecha_registro) {

        this.id = id;
        this.usuario_id = usuario_id;
        this.renovacion = renovacion;
        this.fecha_renovacion = fecha_renovacion;
        this.fecha_siguiente = fecha_siguiente;
        this.observaciones = observaciones;
        this.fecha_registro = fecha_registro;

    }

    //==================================================
    // GETTERS & SETTERS
    //==================================================

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

    public String getRenovacion() {
        return renovacion;
    }

    public void setRenovacion(String renovacion) {
        this.renovacion = renovacion;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(LocalDateTime fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    //==================================================
    // GETTERS PARA LA VISTA
    //==================================================

    public String getFechaRenovacionTexto() {

        if (fecha_renovacion == null) {
            return "";
        }

        return fecha_renovacion.format(FORMATO_FECHA);

    }

    public String getFechaSiguienteTexto() {

        if (fecha_siguiente == null) {
            return "";
        }

        return fecha_siguiente.format(FORMATO_FECHA);

    }

    public String getObservacionesTexto() {

        if (observaciones == null || observaciones.isBlank()) {
            return "";
        }

        return observaciones;

    }

    public String getFechaRegistroTexto() {

        if (fecha_registro == null) {
            return "";
        }

        return fecha_registro.format(FORMATO_FECHA_HORA);

    }

    @Override
    public String toString() {
        return "Historial{"
                + "id=" + id
                + ", usuario_id=" + usuario_id
                + ", renovacion='" + renovacion + '\''
                + ", fecha_renovacion=" + fecha_renovacion
                + ", fecha_siguiente=" + fecha_siguiente
                + ", observaciones='" + observaciones + '\''
                + ", fecha_registro=" + fecha_registro
                + '}';
    }

}