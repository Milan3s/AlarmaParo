package models;

import java.time.LocalDate;

public class Calendario {

    private int renovacion_id;
    private int usuario_id;
    private String usuario;
    private LocalDate fecha;
    private String estado;
    private String observaciones;

    public Calendario() {
    }

    public Calendario(int renovacion_id, int usuario_id, String usuario,
            LocalDate fecha, String estado, String observaciones) {

        this.renovacion_id = renovacion_id;
        this.usuario_id = usuario_id;
        this.usuario = usuario;
        this.fecha = fecha;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    public int getRenovacion_id() {
        return renovacion_id;
    }

    public void setRenovacion_id(int renovacion_id) {
        this.renovacion_id = renovacion_id;
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
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

    @Override
    public String toString() {
        return "Calendario{"
                + "renovacion_id=" + renovacion_id
                + ", usuario_id=" + usuario_id
                + ", usuario='" + usuario + '\''
                + ", fecha=" + fecha
                + ", estado='" + estado + '\''
                + ", observaciones='" + observaciones + '\''
                + '}';
    }

}