package main;

import dao.RenovacionDAO;
import dao.UsuarioDAO;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
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
import models.Usuario;

/**
 * FXML Controller class
 *
 * @author Mila
 */
public class FormRenovacionEditarAlarmaController implements Initializable {

    @FXML
    private ComboBox<Usuario> cmbUsuario;

    @FXML
    private DatePicker dpFechaRenovacion;

    @FXML
    private Spinner<Integer> spDiasAntes;

    @FXML
    private TextArea txtObservaciones;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnCancelar;

    @FXML
    private DatePicker dpFechaSiguiente;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final RenovacionDAO renovacionDAO = new RenovacionDAO();

    private Renovacion renovacion;

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
    private void seleccionarUsuario(ActionEvent event) {

    }

    @FXML
    private void guardar(ActionEvent event) {

        if (cmbUsuario.getValue() == null
                || dpFechaRenovacion.getValue() == null
                || dpFechaSiguiente.getValue() == null) {

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
        confirmacion.setContentText("¿Desea actualizar la renovación?");

        Optional<ButtonType> opcion = confirmacion.showAndWait();

        if (opcion.isPresent() && opcion.get() == ButtonType.OK) {

            renovacion.setUsuario_id(cmbUsuario.getValue().getId());
            renovacion.setFecha_renovacion(dpFechaRenovacion.getValue());
            renovacion.setFecha_siguiente(dpFechaSiguiente.getValue());
            renovacion.setDias_antes(spDiasAntes.getValue());
            renovacion.setEstado("Pendiente");
            renovacion.setObservaciones(txtObservaciones.getText().trim());

            if (renovacionDAO.actualizar(renovacion)) {

                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Correcto");
                alerta.setHeaderText(null);
                alerta.setContentText("La renovación se ha actualizado correctamente.");
                alerta.showAndWait();

                ((Stage) btnGuardar.getScene().getWindow()).close();

            } else {

                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Error");
                alerta.setHeaderText(null);
                alerta.setContentText("No se ha podido actualizar la renovación.");
                alerta.showAndWait();

            }

        }

    }

    @FXML
    private void limpiar(ActionEvent event) {

        if (renovacion != null) {

            setRenovacion(renovacion);

        }

    }

    @FXML
    private void cancelar(ActionEvent event) {

        ((Stage) btnCancelar.getScene().getWindow()).close();

    }

    void setRenovacion(Renovacion renovacion) {

        this.renovacion = renovacion;

        for (Usuario usuario : cmbUsuario.getItems()) {

            if (usuario.getId() == renovacion.getUsuario_id()) {

                cmbUsuario.getSelectionModel().select(usuario);
                break;

            }

        }

        dpFechaRenovacion.setValue(renovacion.getFecha_renovacion());
        dpFechaSiguiente.setValue(renovacion.getFecha_siguiente());
        spDiasAntes.getValueFactory().setValue(renovacion.getDias_antes());
        txtObservaciones.setText(renovacion.getObservaciones());

    }

}
