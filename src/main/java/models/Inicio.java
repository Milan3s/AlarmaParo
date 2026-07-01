package models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class Inicio {

    private int usuarioId;
    private String nombreUsuario;
    private LocalDate fechaRenovacion;
    private LocalDate fechaSiguiente;
    private long diasRestantes;
    private String diasRestantesLetras;
    private String fechaSiguienteFormateada;
    private boolean puedeSellar;
    private String enlaceSellar;

    public Inicio() {
    }

    public Inicio(int usuarioId, String nombreUsuario, LocalDate fechaRenovacion, LocalDate fechaSiguiente) {
        this.usuarioId = usuarioId;
        this.nombreUsuario = nombreUsuario;
        this.fechaRenovacion = fechaRenovacion;
        this.fechaSiguiente = fechaSiguiente;
        calcularDiasRestantes();
        formatearFechaSiguiente();
        verificarPuedeSellar();
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public LocalDate getFechaRenovacion() {
        return fechaRenovacion;
    }

    public void setFechaRenovacion(LocalDate fechaRenovacion) {
        this.fechaRenovacion = fechaRenovacion;
    }

    public LocalDate getFechaSiguiente() {
        return fechaSiguiente;
    }

    public void setFechaSiguiente(LocalDate fechaSiguiente) {
        this.fechaSiguiente = fechaSiguiente;
        calcularDiasRestantes();
        formatearFechaSiguiente();
        verificarPuedeSellar();
    }

    public long getDiasRestantes() {
        return diasRestantes;
    }

    public String getDiasRestantesLetras() {
        return diasRestantesLetras;
    }

    public String getFechaSiguienteFormateada() {
        return fechaSiguienteFormateada;
    }

    public boolean isPuedeSellar() {
        return puedeSellar;
    }

    public String getEnlaceSellar() {
        return enlaceSellar;
    }

    private void calcularDiasRestantes() {
        if (fechaSiguiente != null) {
            LocalDate hoy = LocalDate.now();
            this.diasRestantes = ChronoUnit.DAYS.between(hoy, fechaSiguiente);
            this.diasRestantesLetras = convertirDiasALetras(this.diasRestantes);
        } else {
            this.diasRestantes = 0;
            this.diasRestantesLetras = "Sin fecha";
        }
    }

    private void formatearFechaSiguiente() {
        if (fechaSiguiente != null) {
            DateTimeFormatter formateador = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
            this.fechaSiguienteFormateada = fechaSiguiente.format(formateador);
            if (this.fechaSiguienteFormateada != null && !this.fechaSiguienteFormateada.isEmpty()) {
                this.fechaSiguienteFormateada = this.fechaSiguienteFormateada.substring(0, 1).toUpperCase()
                        + this.fechaSiguienteFormateada.substring(1);
            }
        } else {
            this.fechaSiguienteFormateada = "Sin fecha";
        }
    }

    /**
     * Verifica si hoy es el día de sellado (fechaSiguiente) y son las 00:00 o más.
     */
    private void verificarPuedeSellar() {
        if (fechaSiguiente == null) {
            this.puedeSellar = false;
            this.enlaceSellar = null;
            return;
        }

        LocalDate hoy = LocalDate.now();
        LocalDateTime ahora = LocalDateTime.now();

        // Si hoy es el día de sellado y ya son las 00:00
        if (hoy.equals(fechaSiguiente) || hoy.isAfter(fechaSiguiente)) {
            this.puedeSellar = true;
            this.enlaceSellar = "https://sefoficinavirtual.carm.es/sefoficinavirtual/public/app/renovar-demanda";
        } else {
            this.puedeSellar = false;
            this.enlaceSellar = null;
        }
    }

    private String convertirDiasALetras(long dias) {
        if (dias < 0) {
            return "Vencido hace " + Math.abs(dias) + " días";
        } else if (dias == 0) {
            return "Hoy";
        } else if (dias == 1) {
            return "Mañana";
        } else if (dias <= 15) {
            return "En " + dias + " días";
        } else if (dias <= 30) {
            long semanas = dias / 7;
            long diasRest = dias % 7;
            if (diasRest == 0) {
                return semanas == 1 ? "En 1 semana" : "En " + semanas + " semanas";
            } else {
                String texto = semanas == 1 ? "En 1 semana" : "En " + semanas + " semanas";
                texto += diasRest == 1 ? " y 1 día" : " y " + diasRest + " días";
                return texto;
            }
        } else if (dias <= 365) {
            long meses = dias / 30;
            long diasRest = dias % 30;
            if (diasRest == 0) {
                return meses == 1 ? "En 1 mes" : "En " + meses + " meses";
            } else {
                String texto = meses == 1 ? "En 1 mes" : "En " + meses + " meses";
                texto += diasRest == 1 ? " y 1 día" : " y " + diasRest + " días";
                return texto;
            }
        } else {
            long anios = dias / 365;
            long meses = (dias % 365) / 30;
            if (meses == 0) {
                return anios == 1 ? "En 1 año" : "En " + anios + " años";
            } else {
                String texto = anios == 1 ? "En 1 año" : "En " + anios + " años";
                texto += meses == 1 ? " y 1 mes" : " y " + meses + " meses";
                return texto;
            }
        }
    }

    @Override
    public String toString() {
        return nombreUsuario;
    }
}