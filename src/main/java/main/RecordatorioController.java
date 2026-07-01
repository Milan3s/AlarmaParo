package main;

import dao.RecordatorioDAO;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
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
import models.Recordatorio;

public class RecordatorioController implements Initializable {

    @FXML
    private TextField txtBuscar;

    @FXML
    private Button btnVerFicha;

    @FXML
    private Button btnMarcarEnviado;

    @FXML
    private Button btnDesmarcar;

    @FXML
    private Button btnEliminar;

    @FXML
    private TableView<Recordatorio> tblRecordatorios;

    @FXML
    private TableColumn<Recordatorio, Integer> colId;

    @FXML
    private TableColumn<Recordatorio, String> colRenovacion;

    @FXML
    private TableColumn<Recordatorio, String> colDiasAntes;

    @FXML
    private TableColumn<Recordatorio, String> colEnviado;

    @FXML
    private TableColumn<Recordatorio, String> colFechaEnvio;

    private final RecordatorioDAO recordatorioDAO = new RecordatorioDAO();

    private ObservableList<Recordatorio> listaRecordatorios;

    private FilteredList<Recordatorio> listaFiltrada;

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarTabla();
        cargarRecordatorios();
        configurarBuscador();
    }

    private void inicializarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRenovacion.setCellValueFactory(new PropertyValueFactory<>("renovacion"));

        colDiasAntes.setCellValueFactory(cellData -> {
            Recordatorio recordatorio = cellData.getValue();
            if (recordatorio != null) {
                return new SimpleStringProperty(recordatorio.getDias_antes() + " días");
            }
            return new SimpleStringProperty("");
        });

        colEnviado.setCellValueFactory(cellData -> {
            Recordatorio recordatorio = cellData.getValue();
            if (recordatorio != null) {
                return new SimpleStringProperty(recordatorio.isEnviado() ? "Sí" : "No");
            }
            return new SimpleStringProperty("");
        });

        colFechaEnvio.setCellValueFactory(cellData -> {
            Recordatorio recordatorio = cellData.getValue();
            if (recordatorio != null && recordatorio.getFecha_envio() != null) {
                return new SimpleStringProperty(recordatorio.getFecha_envio().format(FORMATO_FECHA_HORA));
            }
            return new SimpleStringProperty("Pendiente");
        });
    }

    private void cargarRecordatorios() {
        listaRecordatorios = FXCollections.observableArrayList(recordatorioDAO.listar());
        listaFiltrada = new FilteredList<>(listaRecordatorios, p -> true);
        tblRecordatorios.setItems(listaFiltrada);
    }

    private void configurarBuscador() {
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            String filtro = newValue.toLowerCase().trim();

            listaFiltrada.setPredicate(recordatorio -> {
                if (filtro.isEmpty()) {
                    return true;
                }

                if (String.valueOf(recordatorio.getId()).contains(filtro)) {
                    return true;
                }

                if (recordatorio.getRenovacion() != null && recordatorio.getRenovacion().toLowerCase().contains(filtro)) {
                    return true;
                }

                if (String.valueOf(recordatorio.getDias_antes()).contains(filtro)) {
                    return true;
                }

                String enviado = recordatorio.isEnviado() ? "sí" : "no";
                if (enviado.contains(filtro)) {
                    return true;
                }

                if (recordatorio.getFecha_envio() != null && recordatorio.getFecha_envio().format(FORMATO_FECHA_HORA).contains(filtro)) {
                    return true;
                }

                return false;
            });
        });
    }

    @FXML
    private void verFicha(ActionEvent event) {
        Recordatorio recordatorio = tblRecordatorios.getSelectionModel().getSelectedItem();

        if (recordatorio == null) {
            mostrarAlerta("Selecciona una fila para ver la ficha.");
            return;
        }

        mostrarFicha(recordatorio);
    }

    @FXML
    private void marcarEnviado(ActionEvent event) {
        Recordatorio recordatorio = tblRecordatorios.getSelectionModel().getSelectedItem();

        if (recordatorio == null) {
            mostrarAlerta("Selecciona una fila para marcar como enviado.");
            return;
        }

        if (recordatorio.isEnviado()) {
            mostrarAlerta("Este recordatorio ya está marcado como enviado.");
            return;
        }

        boolean confirmado = mostrarConfirmacion("Marcar como enviado", "¿Deseas marcar este recordatorio como enviado?");
        if (confirmado) {
            recordatorio.setEnviado(true);
            recordatorio.setFecha_envio(java.time.LocalDateTime.now());
            boolean actualizado = recordatorioDAO.actualizar(recordatorio);
            if (actualizado) {
                mostrarAlerta("Recordatorio marcado como enviado.");
                cargarRecordatorios();
                configurarBuscador();
            } else {
                mostrarAlerta("Error al actualizar el recordatorio.");
            }
        }
    }

    @FXML
    private void desmarcarEnviado(ActionEvent event) {
        Recordatorio recordatorio = tblRecordatorios.getSelectionModel().getSelectedItem();

        if (recordatorio == null) {
            mostrarAlerta("Selecciona una fila para desmarcar.");
            return;
        }

        if (!recordatorio.isEnviado()) {
            mostrarAlerta("Este recordatorio no está marcado como enviado.");
            return;
        }

        boolean confirmado = mostrarConfirmacion("Desmarcar enviado", "¿Deseas desmarcar este recordatorio como enviado?");
        if (confirmado) {
            recordatorio.setEnviado(false);
            recordatorio.setFecha_envio(null);
            boolean actualizado = recordatorioDAO.actualizar(recordatorio);
            if (actualizado) {
                mostrarAlerta("Recordatorio desmarcado correctamente.");
                cargarRecordatorios();
                configurarBuscador();
            } else {
                mostrarAlerta("Error al actualizar el recordatorio.");
            }
        }
    }

    @FXML
    private void eliminarRecordatorio(ActionEvent event) {
        Recordatorio recordatorio = tblRecordatorios.getSelectionModel().getSelectedItem();

        if (recordatorio == null) {
            mostrarAlerta("Selecciona una fila para eliminar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/forms/FormBorrarRecordatorio.fxml"));
            Parent root = loader.load();

            FormBorrarRecordatorioController controller = loader.getController();
            controller.setRecordatorio(recordatorio);

            Stage stage = new Stage();
            stage.setTitle("Borrar Recordatorio");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(root));

            stage.showAndWait();

            if (controller.isEliminado()) {
                cargarRecordatorios();
                configurarBuscador();
            }

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir el formulario de borrar.");
        }
    }

    /**
     * Muestra un diálogo con todos los detalles del recordatorio.
     */
    private void mostrarFicha(Recordatorio recordatorio) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ficha de recordatorio");
        dialog.setHeaderText("Detalles del recordatorio");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(12);
        grid.setPadding(new Insets(20, 25, 20, 25));

        String estiloEtiqueta = "-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #212529;";
        String estiloValor = "-fx-font-size: 13px; -fx-text-fill: #212529;";

        Label lblIdTitulo = new Label("ID:");
        lblIdTitulo.setStyle(estiloEtiqueta);
        Label lblIdValor = new Label(String.valueOf(recordatorio.getId()));
        lblIdValor.setStyle(estiloValor);
        grid.add(lblIdTitulo, 0, 0);
        grid.add(lblIdValor, 1, 0);

        Label lblRenovacionTitulo = new Label("Renovación:");
        lblRenovacionTitulo.setStyle(estiloEtiqueta);
        Label lblRenovacionValor = new Label(recordatorio.getRenovacion() != null ? recordatorio.getRenovacion() : "Sin datos");
        lblRenovacionValor.setStyle(estiloValor);
        grid.add(lblRenovacionTitulo, 0, 1);
        grid.add(lblRenovacionValor, 1, 1);

        Label lblDiasAntesTitulo = new Label("Días antes:");
        lblDiasAntesTitulo.setStyle(estiloEtiqueta);
        Label lblDiasAntesValor = new Label(recordatorio.getDias_antes() + " días");
        lblDiasAntesValor.setStyle(estiloValor);
        grid.add(lblDiasAntesTitulo, 0, 2);
        grid.add(lblDiasAntesValor, 1, 2);

        Label lblEnviadoTitulo = new Label("Enviado:");
        lblEnviadoTitulo.setStyle(estiloEtiqueta);
        Label lblEnviadoValor = new Label(recordatorio.isEnviado() ? "Sí" : "No");
        lblEnviadoValor.setStyle(estiloValor);
        grid.add(lblEnviadoTitulo, 0, 3);
        grid.add(lblEnviadoValor, 1, 3);

        Label lblFechaEnvioTitulo = new Label("Fecha envío:");
        lblFechaEnvioTitulo.setStyle(estiloEtiqueta);
        Label lblFechaEnvioValor = new Label(
                recordatorio.getFecha_envio() != null
                        ? recordatorio.getFecha_envio().format(FORMATO_FECHA_HORA)
                        : "Pendiente"
        );
        lblFechaEnvioValor.setStyle(estiloValor);
        grid.add(lblFechaEnvioTitulo, 0, 4);
        grid.add(lblFechaEnvioValor, 1, 4);

        dialog.getDialogPane().setContent(grid);

        ButtonType btnCerrar = new ButtonType("Cerrar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(btnCerrar);

        dialog.showAndWait();
    }

    private boolean mostrarConfirmacion(String titulo, String mensaje) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle(titulo);
        confirmacion.setHeaderText(mensaje);
        confirmacion.setContentText("¿Continuar?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}