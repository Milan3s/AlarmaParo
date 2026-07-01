package main;

import dao.RecordatorioDAO;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import models.Recordatorio;

public class FormBorrarRecordatorioController implements Initializable {

    @FXML
    private ComboBox<String> cmbRenovacion;

    @FXML
    private Spinner<Integer> spDiasAntes;

    @FXML
    private Label lblEnviado;

    @FXML
    private Button btnBorrar;

    @FXML
    private Button btnCancelar;

    private final RecordatorioDAO recordatorioDAO = new RecordatorioDAO();

    private Recordatorio recordatorio;

    private boolean eliminado = false;

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarSpinner();
        deshabilitarCampos();
    }

    private void configurarSpinner() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 365, 0);
        spDiasAntes.setValueFactory(valueFactory);
    }

    private void deshabilitarCampos() {
        cmbRenovacion.setDisable(true);
        spDiasAntes.setDisable(true);
    }

    public void setRecordatorio(Recordatorio recordatorio) {
        this.recordatorio = recordatorio;

        if (recordatorio != null) {
            cargarDatosRecordatorio();
        }
    }

    private void cargarDatosRecordatorio() {
        // Cargar renovación en el ComboBox
        if (recordatorio.getRenovacion() != null) {
            cmbRenovacion.getItems().clear();
            cmbRenovacion.getItems().add(recordatorio.getRenovacion());
            cmbRenovacion.getSelectionModel().selectFirst();
        }

        // Cargar días antes
        spDiasAntes.getValueFactory().setValue(recordatorio.getDias_antes());

        // Mostrar estado enviado
        lblEnviado.setText(recordatorio.isEnviado() ? "Sí" : "No");
    }

    public boolean isEliminado() {
        return eliminado;
    }

    @FXML
    private void borrar(ActionEvent event) {
        if (recordatorio == null) {
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.WARNING);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Estás completamente seguro?");
        confirmacion.setContentText(
                "Esta acción no se puede deshacer.\n\n" +
                "Se eliminará el recordatorio:\n" +
                "ID: " + recordatorio.getId() + "\n" +
                "Renovación: " + recordatorio.getRenovacion() + "\n" +
                "Días antes: " + recordatorio.getDias_antes() + "\n" +
                "Enviado: " + (recordatorio.isEnviado() ? "Sí" : "No")
        );

        Optional<ButtonType> resultado = confirmacion.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            boolean ok = recordatorioDAO.eliminar(recordatorio.getId());

            if (ok) {
                eliminado = true;
                mostrarAlerta("Recordatorio eliminado correctamente.");
                cerrarVentana();
            } else {
                mostrarAlerta("No se pudo eliminar el recordatorio de la base de datos.");
            }
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}