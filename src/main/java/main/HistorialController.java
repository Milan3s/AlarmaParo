package main;

import dao.HistorialDAO;
import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
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
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import models.Historial;
import utils.ExplorerWindow;

public class HistorialController implements Initializable {

    @FXML
    private TextField txtBuscar;

    @FXML
    private Button btnVerFicha;

    @FXML
    private Button btnExportar;

    @FXML
    private Button btnEliminar;

    @FXML
    private TableView<Historial> tblHistorial;

    @FXML
    private TableColumn<Historial, Integer> colId;

    @FXML
    private TableColumn<Historial, String> colUsuario;

    @FXML
    private TableColumn<Historial, String> colFechaRenovacion;

    @FXML
    private TableColumn<Historial, String> colFechaSiguiente;

    @FXML
    private TableColumn<Historial, String> colObservaciones;

    @FXML
    private TableColumn<Historial, String> colFechaRegistro;

    private final HistorialDAO dao = new HistorialDAO();

    private ObservableList<Historial> listaHistorial;
    private FilteredList<Historial> listaFiltrada;

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarTabla();
        cargarDatos();
        configurarBuscador();
    }

    private void inicializarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("renovacion"));

        colFechaRenovacion.setCellValueFactory(cellData -> {
            Historial historial = cellData.getValue();
            if (historial != null && historial.getFecha_renovacion() != null) {
                return new SimpleStringProperty(historial.getFecha_renovacion().format(FORMATO_FECHA));
            }
            return new SimpleStringProperty("");
        });

        colFechaSiguiente.setCellValueFactory(cellData -> {
            Historial historial = cellData.getValue();
            if (historial != null && historial.getFecha_siguiente() != null) {
                return new SimpleStringProperty(historial.getFecha_siguiente().format(FORMATO_FECHA));
            }
            return new SimpleStringProperty("");
        });

        colObservaciones.setCellValueFactory(cellData -> {
            Historial historial = cellData.getValue();
            if (historial != null) {
                String obs = historial.getObservaciones();
                return new SimpleStringProperty(obs != null && !obs.isEmpty() ? obs : "-");
            }
            return new SimpleStringProperty("");
        });

        colFechaRegistro.setCellValueFactory(cellData -> {
            Historial historial = cellData.getValue();
            if (historial != null && historial.getFecha_registro() != null) {
                return new SimpleStringProperty(historial.getFecha_registro().format(FORMATO_FECHA_HORA));
            }
            return new SimpleStringProperty("");
        });
    }

    private void cargarDatos() {
        listaHistorial = FXCollections.observableArrayList(dao.listar());
        listaFiltrada = new FilteredList<>(listaHistorial, p -> true);
        tblHistorial.setItems(listaFiltrada);
    }

    private void configurarBuscador() {
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            String filtro = newValue.toLowerCase().trim();

            listaFiltrada.setPredicate(historial -> {
                if (filtro.isEmpty()) {
                    return true;
                }

                if (String.valueOf(historial.getId()).contains(filtro)) {
                    return true;
                }

                if (historial.getRenovacion() != null && historial.getRenovacion().toLowerCase().contains(filtro)) {
                    return true;
                }

                if (historial.getFecha_renovacion() != null && historial.getFecha_renovacion().format(FORMATO_FECHA).contains(filtro)) {
                    return true;
                }

                if (historial.getFecha_siguiente() != null && historial.getFecha_siguiente().format(FORMATO_FECHA).contains(filtro)) {
                    return true;
                }

                if (historial.getObservaciones() != null && historial.getObservaciones().toLowerCase().contains(filtro)) {
                    return true;
                }

                if (historial.getFecha_registro() != null && historial.getFecha_registro().format(FORMATO_FECHA_HORA).contains(filtro)) {
                    return true;
                }

                return false;
            });
        });
    }

    @FXML
    private void verFicha(ActionEvent event) {
        Historial historial = tblHistorial.getSelectionModel().getSelectedItem();

        if (historial == null) {
            mostrarAlerta("Selecciona una fila para ver la ficha.");
            return;
        }

        mostrarFicha(historial);
    }

    @FXML
    private void exportarHistorial(ActionEvent event) {
        if (listaFiltrada.isEmpty()) {
            mostrarAlerta("No hay datos para exportar.");
            return;
        }

        TextInputDialog dialogNombre = new TextInputDialog("historial_renovaciones");
        dialogNombre.setTitle("Exportar historial");
        dialogNombre.setHeaderText("Nombre del archivo");
        dialogNombre.setContentText("Nombre (sin extensión):");

        Optional<String> nombreResult = dialogNombre.showAndWait();

        if (!nombreResult.isPresent() || nombreResult.get().trim().isEmpty()) {
            return;
        }

        String nombreArchivo = nombreResult.get().trim();

        File carpeta = ExplorerWindow.seleccionarCarpeta();

        if (carpeta == null) {
            return;
        }

        File archivo = new File(carpeta, nombreArchivo + ".csv");

        if (exportarCSV(archivo)) {
            mostrarAlerta("Historial exportado correctamente.\n\n"
                    + "Archivo: " + archivo.getAbsolutePath() + "\n\n"
                    + "El archivo CSV es compatible con Excel y LibreOffice.");

            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(archivo);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Error al exportar el historial.");
        }
    }

    private boolean exportarCSV(File archivo) {
        try (FileWriter writer = new FileWriter(archivo)) {
            // BOM UTF-8 para que Excel detecte tildes y eñes
            writer.write('\uFEFF');

            // Cabecera con formato legible
            writer.write("ID\tUsuario\tFecha Renovación\tFecha Siguiente\tObservaciones\tFecha Registro\n");

            // Datos con tabulaciones (Excel lo reconoce como columnas separadas)
            for (Historial h : listaFiltrada) {
                writer.write(String.format("%d\t%s\t%s\t%s\t%s\t%s\n",
                        h.getId(),
                        h.getRenovacion() != null ? h.getRenovacion() : "",
                        h.getFecha_renovacion() != null ? h.getFecha_renovacion().format(FORMATO_FECHA) : "",
                        h.getFecha_siguiente() != null ? h.getFecha_siguiente().format(FORMATO_FECHA) : "",
                        h.getObservaciones() != null ? h.getObservaciones().replace("\t", " ").replace("\n", " ") : "",
                        h.getFecha_registro() != null ? h.getFecha_registro().format(FORMATO_FECHA_HORA) : ""
                ));
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    private void eliminarHistorial(ActionEvent event) {
        Historial historial = tblHistorial.getSelectionModel().getSelectedItem();

        if (historial == null) {
            mostrarAlerta("Selecciona una fila para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.WARNING);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Estás completamente seguro?");
        confirmacion.setContentText(
                "Esta acción no se puede deshacer.\n\n"
                + "Se eliminará el registro:\n"
                + "ID: " + historial.getId() + "\n"
                + "Usuario: " + historial.getRenovacion() + "\n"
                + "Fecha: " + (historial.getFecha_renovacion() != null ? historial.getFecha_renovacion().format(FORMATO_FECHA) : "Sin fecha")
        );

        Optional<ButtonType> resultado = confirmacion.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            boolean ok = dao.eliminar(historial.getId());

            if (ok) {
                mostrarAlerta("Registro eliminado correctamente.");
                cargarDatos();
                configurarBuscador();
            } else {
                mostrarAlerta("No se pudo eliminar el registro.");
            }
        }
    }

    private void mostrarFicha(Historial historial) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ficha de historial");
        dialog.setHeaderText("Detalles del registro");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(12);
        grid.setPadding(new Insets(20, 25, 20, 25));

        String estiloEtiqueta = "-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #212529;";
        String estiloValor = "-fx-font-size: 13px; -fx-text-fill: #212529;";

        Label lblIdTitulo = new Label("ID:");
        lblIdTitulo.setStyle(estiloEtiqueta);
        Label lblIdValor = new Label(String.valueOf(historial.getId()));
        lblIdValor.setStyle(estiloValor);
        grid.add(lblIdTitulo, 0, 0);
        grid.add(lblIdValor, 1, 0);

        Label lblUsuarioTitulo = new Label("Usuario:");
        lblUsuarioTitulo.setStyle(estiloEtiqueta);
        Label lblUsuarioValor = new Label(historial.getRenovacion() != null ? historial.getRenovacion() : "Sin datos");
        lblUsuarioValor.setStyle(estiloValor);
        grid.add(lblUsuarioTitulo, 0, 1);
        grid.add(lblUsuarioValor, 1, 1);

        Label lblFechaRenovacionTitulo = new Label("Fecha renovación:");
        lblFechaRenovacionTitulo.setStyle(estiloEtiqueta);
        Label lblFechaRenovacionValor = new Label(
                historial.getFecha_renovacion() != null ? historial.getFecha_renovacion().format(FORMATO_FECHA) : "Sin datos"
        );
        lblFechaRenovacionValor.setStyle(estiloValor);
        grid.add(lblFechaRenovacionTitulo, 0, 2);
        grid.add(lblFechaRenovacionValor, 1, 2);

        Label lblFechaSiguienteTitulo = new Label("Fecha siguiente:");
        lblFechaSiguienteTitulo.setStyle(estiloEtiqueta);
        Label lblFechaSiguienteValor = new Label(
                historial.getFecha_siguiente() != null ? historial.getFecha_siguiente().format(FORMATO_FECHA) : "Sin datos"
        );
        lblFechaSiguienteValor.setStyle(estiloValor);
        grid.add(lblFechaSiguienteTitulo, 0, 3);
        grid.add(lblFechaSiguienteValor, 1, 3);

        Label lblObservacionesTitulo = new Label("Observaciones:");
        lblObservacionesTitulo.setStyle(estiloEtiqueta);
        Label lblObservacionesValor = new Label(
                historial.getObservaciones() != null && !historial.getObservaciones().isEmpty()
                ? historial.getObservaciones()
                : "Sin observaciones"
        );
        lblObservacionesValor.setStyle(estiloValor);
        lblObservacionesValor.setWrapText(true);
        lblObservacionesValor.setMaxWidth(350);
        grid.add(lblObservacionesTitulo, 0, 4);
        grid.add(lblObservacionesValor, 1, 4);

        Label lblFechaRegistroTitulo = new Label("Fecha registro:");
        lblFechaRegistroTitulo.setStyle(estiloEtiqueta);
        Label lblFechaRegistroValor = new Label(
                historial.getFecha_registro() != null
                ? historial.getFecha_registro().format(FORMATO_FECHA_HORA)
                : "Sin datos"
        );
        lblFechaRegistroValor.setStyle(estiloValor);
        grid.add(lblFechaRegistroTitulo, 0, 5);
        grid.add(lblFechaRegistroValor, 1, 5);

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