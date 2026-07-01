package main;

import dao.RenovacionDAO;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Renovacion;

public class RenovarController implements Initializable {

    @FXML
    private TextField txtBuscar;

    @FXML
    private TableView<Renovacion> tblRenovaciones;

    @FXML
    private TableColumn<Renovacion, Integer> colId;

    @FXML
    private TableColumn<Renovacion, String> colUsuario;

    @FXML
    private TableColumn<Renovacion, String> colFechaRenovacion;

    @FXML
    private TableColumn<Renovacion, String> colFechaSiguiente;

    @FXML
    private TableColumn<Renovacion, String> colObservaciones;

    @FXML
    private Button btnVerFicha;

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnEliminar;

    private final RenovacionDAO renovacionDAO = new RenovacionDAO();

    private ObservableList<Renovacion> listaRenovaciones;

    private FilteredList<Renovacion> listaFiltrada;

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarTabla();
        cargarRenovaciones();
        configurarBuscador();
    }

    private void inicializarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));

        colFechaRenovacion.setCellValueFactory(cellData -> {
            Renovacion renovacion = cellData.getValue();
            if (renovacion != null && renovacion.getFecha_renovacion() != null) {
                return new SimpleStringProperty(renovacion.getFecha_renovacion().format(FORMATO_FECHA));
            }
            return new SimpleStringProperty("");
        });

        colFechaSiguiente.setCellValueFactory(cellData -> {
            Renovacion renovacion = cellData.getValue();
            if (renovacion != null && renovacion.getFecha_siguiente() != null) {
                return new SimpleStringProperty(renovacion.getFecha_siguiente().format(FORMATO_FECHA));
            }
            return new SimpleStringProperty("");
        });

        colObservaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
    }

    private void cargarRenovaciones() {
        listaRenovaciones = FXCollections.observableArrayList(renovacionDAO.listar());
        listaFiltrada = new FilteredList<>(listaRenovaciones, p -> true);
        tblRenovaciones.setItems(listaFiltrada);
    }

    private void configurarBuscador() {
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            String filtro = newValue.toLowerCase().trim();

            listaFiltrada.setPredicate(renovacion -> {
                if (filtro.isEmpty()) {
                    return true;
                }

                if (String.valueOf(renovacion.getId()).contains(filtro)) {
                    return true;
                }

                if (renovacion.getUsuario() != null && renovacion.getUsuario().toLowerCase().contains(filtro)) {
                    return true;
                }

                if (renovacion.getFecha_renovacion() != null && renovacion.getFecha_renovacion().format(FORMATO_FECHA).contains(filtro)) {
                    return true;
                }

                if (renovacion.getFecha_siguiente() != null && renovacion.getFecha_siguiente().format(FORMATO_FECHA).contains(filtro)) {
                    return true;
                }

                return renovacion.getObservaciones() != null && renovacion.getObservaciones().toLowerCase().contains(filtro);
            });
        });
    }

    @FXML
    private void verFicha(ActionEvent event) {
        Renovacion renovacion = tblRenovaciones.getSelectionModel().getSelectedItem();

        if (renovacion == null) {
            mostrarAlerta("Selecciona una fila para ver la ficha.");
            return;
        }

        mostrarFicha(renovacion);
    }

    @FXML
    private void agregarRenovacion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/forms/FormRenovacionAgregarAlarma.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Agregar Alarma");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(root));

            stage.showAndWait();

            cargarRenovaciones();
            configurarBuscador();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir el formulario de agregar.");
        }
    }

    @FXML
    private void editarRenovacion(ActionEvent event) {
        Renovacion renovacion = tblRenovaciones.getSelectionModel().getSelectedItem();

        if (renovacion == null) {
            mostrarAlerta("Selecciona una fila para editar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/forms/FormRenovacionEditarAlarma.fxml"));
            Parent root = loader.load();

            FormRenovacionEditarAlarmaController controller = loader.getController();
            controller.setRenovacion(renovacion);

            Stage stage = new Stage();
            stage.setTitle("Editar Alarma");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(root));

            stage.showAndWait();

            cargarRenovaciones();
            configurarBuscador();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir el formulario de edición.");
        }
    }

    @FXML
    private void eliminarRenovacion(ActionEvent event) {
        Renovacion renovacion = tblRenovaciones.getSelectionModel().getSelectedItem();

        if (renovacion == null) {
            mostrarAlerta("Selecciona una fila para eliminar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/forms/FormRenovacionBorrarAlarma.fxml"));
            Parent root = loader.load();

            FormRenovacionBorrarAlarmaController controller = loader.getController();
            controller.setRenovacion(renovacion);

            Stage stage = new Stage();
            stage.setTitle("Borrar Alarma");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(root));

            stage.showAndWait();

            if (controller.isEliminado()) {
                cargarRenovaciones();
                configurarBuscador();
            }

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir el formulario de borrar.");
        }
    }

    /**
     * Muestra un diálogo con todos los detalles de la renovación.
     */
    private void mostrarFicha(Renovacion renovacion) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ficha de renovación");
        dialog.setHeaderText("Detalles de la renovación");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(12);
        grid.setPadding(new Insets(20, 25, 20, 25));

        String estiloEtiqueta = "-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #212529;";
        String estiloValor = "-fx-font-size: 13px; -fx-text-fill: #212529;";

        Label lblIdTitulo = new Label("ID:");
        lblIdTitulo.setStyle(estiloEtiqueta);
        Label lblIdValor = new Label(String.valueOf(renovacion.getId()));
        lblIdValor.setStyle(estiloValor);
        grid.add(lblIdTitulo, 0, 0);
        grid.add(lblIdValor, 1, 0);

        Label lblUsuarioTitulo = new Label("Usuario:");
        lblUsuarioTitulo.setStyle(estiloEtiqueta);
        Label lblUsuarioValor = new Label(renovacion.getUsuario() != null ? renovacion.getUsuario() : "Sin datos");
        lblUsuarioValor.setStyle(estiloValor);
        grid.add(lblUsuarioTitulo, 0, 1);
        grid.add(lblUsuarioValor, 1, 1);

        Label lblFechaRenovacionTitulo = new Label("Fecha renovación:");
        lblFechaRenovacionTitulo.setStyle(estiloEtiqueta);
        Label lblFechaRenovacionValor = new Label(
                renovacion.getFecha_renovacion() != null ? renovacion.getFecha_renovacion().format(FORMATO_FECHA) : "Sin datos"
        );
        lblFechaRenovacionValor.setStyle(estiloValor);
        grid.add(lblFechaRenovacionTitulo, 0, 2);
        grid.add(lblFechaRenovacionValor, 1, 2);

        Label lblFechaSiguienteTitulo = new Label("Próximo sellado:");
        lblFechaSiguienteTitulo.setStyle(estiloEtiqueta);
        Label lblFechaSiguienteValor = new Label(
                renovacion.getFecha_siguiente() != null ? renovacion.getFecha_siguiente().format(FORMATO_FECHA) : "Sin datos"
        );
        lblFechaSiguienteValor.setStyle(estiloValor);
        grid.add(lblFechaSiguienteTitulo, 0, 3);
        grid.add(lblFechaSiguienteValor, 1, 3);

        Label lblEstadoTitulo = new Label("Estado:");
        lblEstadoTitulo.setStyle(estiloEtiqueta);
        Label lblEstadoValor = new Label(renovacion.getEstado() != null ? renovacion.getEstado() : "Sin datos");
        lblEstadoValor.setStyle(estiloValor);
        grid.add(lblEstadoTitulo, 0, 4);
        grid.add(lblEstadoValor, 1, 4);

        Label lblObservacionesTitulo = new Label("Observaciones:");
        lblObservacionesTitulo.setStyle(estiloEtiqueta);
        Label lblObservacionesValor = new Label(
                renovacion.getObservaciones() != null && !renovacion.getObservaciones().isEmpty()
                        ? renovacion.getObservaciones()
                        : "Sin observaciones"
        );
        lblObservacionesValor.setStyle(estiloValor);
        lblObservacionesValor.setWrapText(true);
        lblObservacionesValor.setMaxWidth(350);
        grid.add(lblObservacionesTitulo, 0, 5);
        grid.add(lblObservacionesValor, 1, 5);

        Label lblFechaCreacionTitulo = new Label("Fecha creación:");
        lblFechaCreacionTitulo.setStyle(estiloEtiqueta);
        Label lblFechaCreacionValor = new Label(
                renovacion.getFecha_creacion() != null
                        ? renovacion.getFecha_creacion().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
                        : "Sin datos"
        );
        lblFechaCreacionValor.setStyle(estiloValor);
        grid.add(lblFechaCreacionTitulo, 0, 6);
        grid.add(lblFechaCreacionValor, 1, 6);

        dialog.getDialogPane().setContent(grid);

        ButtonType btnCerrar = new ButtonType("Cerrar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(btnCerrar);

        dialog.showAndWait();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}