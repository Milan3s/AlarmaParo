package main;

import dao.InicioDAO;
import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import models.Inicio;

public class InicioController implements Initializable {

    @FXML
    private Label lblContador;

    @FXML
    private Label lblDiasLetras;

    @FXML
    private Label lblProximoSellado;

    @FXML
    private Label lblUltimoSellado;

    @FXML
    private Label lblEstado;

    @FXML
    private Hyperlink linkSellar;

    @FXML
    private ComboBox<Inicio> cmbUsuario;

    private final InicioDAO dao = new InicioDAO();

    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Timeline timeline;

    private Inicio inicioActual;

    private static final String URL_SELLAR = "https://sefoficinavirtual.carm.es/sefoficinavirtual/public/app/renovar-demanda";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarUsuarios();
        cmbUsuario.setOnAction(event -> cargarInicio());
    }

    private void cargarUsuarios() {
        cmbUsuario.setItems(FXCollections.observableArrayList(dao.obtenerUsuarios()));

        if (!cmbUsuario.getItems().isEmpty()) {
            cmbUsuario.getSelectionModel().selectFirst();
            cargarInicio();
        }
    }

    private void cargarInicio() {
        Inicio usuario = cmbUsuario.getValue();

        if (usuario == null) {
            return;
        }

        inicioActual = dao.obtenerInicio(usuario.getUsuarioId());

        if (inicioActual == null) {
            lblUltimoSellado.setText("-");
            lblProximoSellado.setText("-");
            lblContador.setText("-");
            lblDiasLetras.setText("-");
            lblEstado.setText("Sin datos");
            linkSellar.setDisable(true);
            linkSellar.setText("Ir a sellar la demanda (no disponible)");
            return;
        }

        lblUltimoSellado.setText(inicioActual.getFechaRenovacion() != null
                ? inicioActual.getFechaRenovacion().format(formatoFecha)
                : "-");

        lblProximoSellado.setText(inicioActual.getFechaSiguienteFormateada() != null
                ? inicioActual.getFechaSiguienteFormateada()
                : "-");

        lblDiasLetras.setText(inicioActual.getDiasRestantesLetras() != null
                ? inicioActual.getDiasRestantesLetras()
                : "-");

        actualizarEstado();
        actualizarEnlace();
        iniciarContador(inicioActual);
    }

    private void actualizarEstado() {
        if (inicioActual.getDiasRestantes() < 0) {
            lblEstado.setText("La demanda de empleo está CADUCADA.");
        } else if (inicioActual.getDiasRestantes() == 0) {
            lblEstado.setText("¡Hoy es el día de sellar la demanda!");
        } else if (inicioActual.getDiasRestantes() == 1) {
            lblEstado.setText("La demanda de empleo vence mañana.");
        } else if (inicioActual.getDiasRestantes() <= 7) {
            lblEstado.setText("La demanda de empleo está activa. ¡Queda menos de una semana!");
        } else {
            lblEstado.setText("La demanda de empleo se encuentra activa.");
        }
    }

    /**
     * Activa o desactiva el enlace según si puede sellar. El enlace siempre
     * está visible pero cambia su estado.
     */
    private void actualizarEnlace() {
        if (inicioActual.isPuedeSellar()) {
            linkSellar.setDisable(false);
            linkSellar.setText("Ir a sellar la demanda");
            linkSellar.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2563eb;");
        } else {
            linkSellar.setDisable(true);
            linkSellar.setText("Ir a sellar la demanda (disponible el día del sellado)");
            linkSellar.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #9ca3af;");
        }
    }

    private void iniciarContador(Inicio inicio) {
        if (timeline != null) {
            timeline.stop();
        }

        timeline = new Timeline(
                new KeyFrame(
                        javafx.util.Duration.seconds(1),
                        e -> actualizarContador(inicio)
                )
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        actualizarContador(inicio);
    }

    private void actualizarContador(Inicio inicio) {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime siguiente = inicio.getFechaSiguiente().atStartOfDay();

        java.time.Duration diferencia = java.time.Duration.between(ahora, siguiente);

        if (diferencia.isNegative() || diferencia.isZero()) {
            if (diferencia.isNegative()) {
                lblContador.setText("CADUCADO");
            } else {
                lblContador.setText("¡HOY!");
            }

            // Activar el enlace si aún no lo está
            if (linkSellar.isDisable()) {
                linkSellar.setDisable(false);
                linkSellar.setText("Ir a sellar la demanda");
                linkSellar.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2563eb;");
                lblEstado.setText("¡Hoy es el día de sellar la demanda!");
            }
            return;
        }

        long dias = diferencia.toDays();
        long horas = diferencia.toHours() % 24;
        long minutos = diferencia.toMinutes() % 60;
        long segundos = diferencia.getSeconds() % 60;

        if (dias > 0) {
            lblContador.setText(String.format("%d días %02d:%02d:%02d", dias, horas, minutos, segundos));
        } else {
            lblContador.setText(String.format("%02d:%02d:%02d", horas, minutos, segundos));
        }
    }

    @FXML
    private void abrirEnlaceSellar(ActionEvent event) {
        if (linkSellar.isDisable()) {
            return;
        }

        try {
            Desktop.getDesktop().browse(new URI(URL_SELLAR));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
