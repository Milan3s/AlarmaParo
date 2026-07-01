package main;

import dao.RenovacionDAO;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import models.Renovacion;

/**
 * FXML Controller class
 *
 * @author Mila
 */
public class FormRenovacionBorrarAlarmaController implements Initializable {

    @FXML
    private ComboBox<String> cmbUsuario;

    @FXML
    private DatePicker dpFechaRenovacion;

    @FXML
    private DatePicker dpFechaSiguiente;

    @FXML
    private Spinner<Integer> spDiasAntes;

    @FXML
    private TextArea txtObservaciones;

    @FXML
    private Button btnBorrar;

    @FXML
    private Button btnCancelar;

    private final RenovacionDAO renovacionDAO = new RenovacionDAO();

    private Renovacion renovacion;

    private boolean eliminado = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarSpinner();
        deshabilitarCampos();
    }

    /**
     * Configura el Spinner con valores por defecto.
     */
    private void configurarSpinner() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 365, 0);
        spDiasAntes.setValueFactory(valueFactory);
    }

    /**
     * Deshabilita todos los campos para que sean de solo lectura.
     */
    private void deshabilitarCampos() {
        cmbUsuario.setDisable(true);
        dpFechaRenovacion.setDisable(true);
        dpFechaSiguiente.setDisable(true);
        spDiasAntes.setDisable(true);
        txtObservaciones.setDisable(true);
    }

    /**
     * Recibe la renovación desde el controlador principal y carga los datos.
     */
    public void setRenovacion(Renovacion renovacion) {
        this.renovacion = renovacion;

        if (renovacion != null) {
            cargarDatosRenovacion();
        }
    }

    /**
     * Carga los datos de la renovación en los campos.
     */
    private void cargarDatosRenovacion() {
        // Cargar usuario en el ComboBox
        if (renovacion.getUsuario() != null) {
            cmbUsuario.getItems().clear();
            cmbUsuario.getItems().add(renovacion.getUsuario());
            cmbUsuario.getSelectionModel().selectFirst();
        }

        // Cargar fechas
        dpFechaRenovacion.setValue(renovacion.getFecha_renovacion());
        dpFechaSiguiente.setValue(renovacion.getFecha_siguiente());

        // Cargar observaciones
        txtObservaciones.setText(renovacion.getObservaciones() != null ? renovacion.getObservaciones() : "");

        // Calcular días antes
        if (renovacion.getFecha_siguiente() != null && renovacion.getFecha_renovacion() != null) {
            long dias = java.time.temporal.ChronoUnit.DAYS.between(renovacion.getFecha_renovacion(), renovacion.getFecha_siguiente());
            spDiasAntes.getValueFactory().setValue((int) dias);
        }
    }

    /**
     * Retorna true si se eliminó la renovación.
     */
    public boolean isEliminado() {
        return eliminado;
    }

    @FXML
    private void borrar(ActionEvent event) {
        if (renovacion == null) {
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.WARNING);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Estás completamente seguro?");
        confirmacion.setContentText(
                "Esta acción no se puede deshacer.\n\n"
                + "Se eliminará la renovación de:\n"
                + "Usuario: " + renovacion.getUsuario() + "\n"
                + "ID: " + renovacion.getId() + "\n"
                + "Próximo sellado: " + (renovacion.getFecha_siguiente() != null
                ? renovacion.getFecha_siguiente().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                : "Sin fecha")
        );

        Optional<ButtonType> resultado = confirmacion.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            boolean ok = renovacionDAO.eliminar(renovacion.getId());

            if (ok) {
                eliminado = true;
                mostrarAlerta("Renovación eliminada correctamente.");
                cerrarVentana();
            } else {
                mostrarAlerta("No se pudo eliminar la renovación de la base de datos.");
            }
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        cerrarVentana();
    }

    /**
     * Cierra la ventana actual.
     */
    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    /**
     * Muestra una alerta informativa.
     */
    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
