package main;

import dao.CalendarioDAO;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import models.Calendario;

public class CalendarioController implements Initializable {

    @FXML
    private Button btnAnterior;

    @FXML
    private Button btnSiguiente;

    @FXML
    private Label lblMes;

    @FXML
    private Label lblFechaSeleccionada;

    @FXML
    private ComboBox<Integer> cmbAnio;

    @FXML
    private GridPane gridCalendario;

    private final CalendarioDAO calendarioDAO = new CalendarioDAO();

    private YearMonth mesActual;

    private final Locale locale = new Locale("es", "ES");

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        mesActual = YearMonth.now();

        cargarAnios();

        cmbAnio.setValue(mesActual.getYear());

        lblFechaSeleccionada.setText(
                LocalDate.now().format(
                        DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", locale)
                )
        );

        actualizarCalendario();

    }

    private void cargarAnios() {

        for (int anio = 2020; anio <= 2035; anio++) {
            cmbAnio.getItems().add(anio);
        }

    }

    @FXML
    private void cambiarAnio(ActionEvent event) {

        if (cmbAnio.getValue() != null) {

            mesActual = YearMonth.of(cmbAnio.getValue(), mesActual.getMonthValue());

            actualizarCalendario();

        }

    }

    @FXML
    private void mesAnterior(ActionEvent event) {

        mesActual = mesActual.minusMonths(1);

        cmbAnio.setValue(mesActual.getYear());

        actualizarCalendario();

    }

    @FXML
    private void mesSiguiente(ActionEvent event) {

        mesActual = mesActual.plusMonths(1);

        cmbAnio.setValue(mesActual.getYear());

        actualizarCalendario();

    }

    private void actualizarCalendario() {

        lblMes.setText(
                mesActual.getMonth()
                        .getDisplayName(TextStyle.FULL, locale)
                        .toUpperCase()
                + " "
                + mesActual.getYear()
        );

        generarCalendario();

    }

    private void generarCalendario() {

        gridCalendario.getChildren().clear();

        List<Calendario> eventos = calendarioDAO.listarEventos();

        LocalDate primerDia = mesActual.atDay(1);

        int desplazamiento = primerDia.getDayOfWeek().getValue() - 1;

        int diasMes = mesActual.lengthOfMonth();

        int dia = 1;

        for (int fila = 0; fila < 6; fila++) {

            for (int columna = 0; columna < 7; columna++) {

                VBox celda = crearCelda();

                if ((fila == 0 && columna < desplazamiento) || dia > diasMes) {

                    gridCalendario.add(celda, columna, fila);

                    continue;

                }

                LocalDate fecha = mesActual.atDay(dia);

                Label numero = new Label(String.valueOf(dia));
                numero.setFont(Font.font("System", FontWeight.BOLD, 15));

                celda.getChildren().add(numero);

                for (Calendario evento : eventos) {

                    if (evento.getFecha().equals(fecha)) {

                        Label usuario = new Label(evento.getUsuario());
                        usuario.setStyle("-fx-font-size:11px; -fx-font-weight:bold;");
                        usuario.setWrapText(true);

                        Label estado = new Label(evento.getEstado());
                        estado.setStyle("-fx-font-size:10px; -fx-text-fill:#2E7D32;");

                        celda.getChildren().add(usuario);
                        celda.getChildren().add(estado);

                        if (evento.getObservaciones() != null
                                && !evento.getObservaciones().isBlank()) {

                            Label observacion = new Label(evento.getObservaciones());
                            observacion.setWrapText(true);
                            observacion.setStyle("-fx-font-size:10px; -fx-text-fill:#666666;");

                            celda.getChildren().add(observacion);

                        }

                    }

                }

                if (fecha.equals(LocalDate.now())) {

                    celda.setStyle(
                            "-fx-background-color:#dbeeff;"
                            + "-fx-border-color:#2196F3;"
                            + "-fx-border-width:2;"
                    );

                }

                LocalDate fechaSeleccionada = fecha;

                celda.setOnMouseClicked(e -> {

                    lblFechaSeleccionada.setText(
                            fechaSeleccionada.format(
                                    DateTimeFormatter.ofPattern(
                                            "EEEE, d 'de' MMMM 'de' yyyy",
                                            locale
                                    )
                            )
                    );

                });

                gridCalendario.add(celda, columna, fila);

                dia++;

            }

        }

    }

    private VBox crearCelda() {

        VBox celda = new VBox();

        celda.setSpacing(3);

        celda.setPadding(new Insets(5));

        celda.setPrefSize(140, 100);

        celda.setStyle(
                "-fx-background-color:white;"
                + "-fx-border-color:#d0d0d0;"
        );

        return celda;

    }

}
