package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Recordatorio {

    private static final DateTimeFormatter FORMATO_FECHA
            = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private int id;
    private int renovacion_id;
    private String renovacion;
    private int dias_antes;
    private boolean enviado;
    private LocalDateTime fecha_envio;

    public Recordatorio() {
    }

    public Recordatorio(int id,
            int renovacion_id,
            String renovacion,
            int dias_antes,
            boolean enviado,
            LocalDateTime fecha_envio) {

        this.id = id;
        this.renovacion_id = renovacion_id;
        this.renovacion = renovacion;
        this.dias_antes = dias_antes;
        this.enviado = enviado;
        this.fecha_envio = fecha_envio;
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

    public int getRenovacion_id() {
        return renovacion_id;
    }

    public void setRenovacion_id(int renovacion_id) {
        this.renovacion_id = renovacion_id;
    }

    public String getRenovacion() {
        return renovacion;
    }

    public void setRenovacion(String renovacion) {
        this.renovacion = renovacion;
    }

    public int getDias_antes() {
        return dias_antes;
    }

    public void setDias_antes(int dias_antes) {
        this.dias_antes = dias_antes;
    }

    public boolean isEnviado() {
        return enviado;
    }

    public void setEnviado(boolean enviado) {
        this.enviado = enviado;
    }

    public LocalDateTime getFecha_envio() {
        return fecha_envio;
    }

    public void setFecha_envio(LocalDateTime fecha_envio) {
        this.fecha_envio = fecha_envio;
    }

    //==================================================
    // GETTERS PARA LA VISTA
    //==================================================
    public String getEnviadoTexto() {
        return enviado ? "Sí" : "No";
    }

    public String getFechaEnvioTexto() {

        if (fecha_envio == null) {
            return "";
        }

        return fecha_envio.format(FORMATO_FECHA);
    }

    @Override
    public String toString() {
        return "Recordatorio{"
                + "id=" + id
                + ", renovacion_id=" + renovacion_id
                + ", renovacion='" + renovacion + '\''
                + ", dias_antes=" + dias_antes
                + ", enviado=" + enviado
                + ", fecha_envio=" + fecha_envio
                + '}';
    }

}
