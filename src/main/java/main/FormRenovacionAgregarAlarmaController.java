package main;

import dao.RenovacionDAO;
import dao.UsuarioDAO;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
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
import models.Usuario;

public class FormRenovacionAgregarAlarmaController implements Initializable {

    @FXML
    private DatePicker dpFechaRenovacion;

    @FXML
    private Spinner<Integer> spDiasAntes;

    @FXML
    private DatePicker dpFechaAviso;

    @FXML
    private TextArea txtObservaciones;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnCancelar;

    @FXML
    private ComboBox<Usuario> cmbUsuario;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final RenovacionDAO renovacionDAO = new RenovacionDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        cargarUsuarios();

        spDiasAntes.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 365, 30)
        );

    }

    private void cargarUsuarios() {

        cmbUsuario.setItems(
                FXCollections.observableArrayList(usuarioDAO.listar())
        );

        cmbUsuario.getSelectionModel().clearSelection();

    }

    @FXML
    private void guardar(ActionEvent event) {

        if (cmbUsuario.getValue() == null
                || dpFechaRenovacion.getValue() == null
                || dpFechaAviso.getValue() == null) {

            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Campos obligatorios");
            alerta.setHeaderText(null);
            alerta.setContentText("Debe completar todos los campos obligatorios.");
            alerta.showAndWait();
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Desea guardar la renovación?");

        Optional<ButtonType> opcion = confirmacion.showAndWait();

        if (opcion.isPresent() && opcion.get() == ButtonType.OK) {

            Renovacion renovacion = new Renovacion();

            renovacion.setUsuario_id(cmbUsuario.getValue().getId());
            renovacion.setFecha_renovacion(dpFechaRenovacion.getValue());
            renovacion.setFecha_siguiente(dpFechaAviso.getValue());
            renovacion.setEstado("Pendiente");
            renovacion.setObservaciones(txtObservaciones.getText().trim());

            int id = renovacionDAO.insertar(renovacion);

            if (id > 0) {

                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Correcto");
                alerta.setHeaderText(null);
                alerta.setContentText("La renovación se ha guardado correctamente.");
                alerta.showAndWait();

                limpiar(null);

            } else {

                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Error");
                alerta.setHeaderText(null);
                alerta.setContentText("No se ha podido guardar la renovación.");
                alerta.showAndWait();

            }
        }
    }

    @FXML
    private void limpiar(ActionEvent event) {

        cmbUsuario.getSelectionModel().clearSelection();
        dpFechaRenovacion.setValue(null);
        dpFechaAviso.setValue(null);
        spDiasAntes.getValueFactory().setValue(30);
        txtObservaciones.clear();

    }

    @FXML
    private void cancelar(ActionEvent event) {

        ((Stage) btnCancelar.getScene().getWindow()).close();

    }

}
