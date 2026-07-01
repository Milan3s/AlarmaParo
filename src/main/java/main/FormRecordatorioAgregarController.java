package main;

import dao.RecordatorioDAO;
import dao.RenovacionDAO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import models.Recordatorio;
import models.Renovacion;

public class FormRecordatorioAgregarController implements Initializable {

    @FXML
    private ComboBox<Renovacion> cmbRenovacion;

    @FXML
    private Spinner<Integer> spDiasAntes;

    @FXML
    private TextArea txtObservaciones;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnCancelar;

    private final RecordatorioDAO recordatorioDAO = new RecordatorioDAO();
    private final RenovacionDAO renovacionDAO = new RenovacionDAO();

    private boolean guardado = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarSpinner();
        cargarRenovaciones();
    }

    private void configurarSpinner() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 365, 7);
        spDiasAntes.setValueFactory(valueFactory);
    }

    private void cargarRenovaciones() {
        cmbRenovacion.setItems(FXCollections.observableArrayList(renovacionDAO.listar()));
    }

    public boolean isGuardado() {
        return guardado;
    }

    @FXML
    private void guardar(ActionEvent event) {
        Renovacion renovacion = cmbRenovacion.getValue();

        if (renovacion == null) {
            mostrarAlerta("Selecciona una renovación.");
            return;
        }

        // Verificar si ya existe un recordatorio para esta renovación
        Recordatorio existente = recordatorioDAO.obtenerPorRenovacion(renovacion.getId());
        if (existente != null) {
            mostrarAlerta("Ya existe un recordatorio para esta renovación.");
            return;
        }

        Recordatorio recordatorio = new Recordatorio();
        recordatorio.setRenovacion_id(renovacion.getId());
        recordatorio.setDias_antes(spDiasAntes.getValue());
        recordatorio.setEnviado(false);

        boolean ok = recordatorioDAO.insertar(recordatorio);

        if (ok) {
            guardado = true;
            mostrarAlerta("Recordatorio guardado correctamente.");
            cerrarVentana();
        } else {
            mostrarAlerta("Error al guardar el recordatorio.");
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
