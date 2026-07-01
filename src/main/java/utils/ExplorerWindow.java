package utils;

import java.io.File;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class ExplorerWindow {

    private TreeView<String> treeView;
    private Label lblRuta;
    private File carpetaSeleccionada;
    private Dialog<ButtonType> dialog;

    public ExplorerWindow() {
        inicializar();
    }

    private void inicializar() {
        dialog = new Dialog<>();
        dialog.setTitle("Seleccionar carpeta");
        dialog.setHeaderText("Elige la carpeta donde guardar el archivo");

        treeView = new TreeView<>();
        treeView.setPrefWidth(500);
        treeView.setPrefHeight(400);

        TreeItem<String> raiz = new TreeItem<>("Equipo");
        raiz.setExpanded(true);

        // Escritorio
        File escritorio = new File(System.getProperty("user.home") + File.separator + "Desktop");
        if (escritorio.exists()) {
            TreeItem<String> nodoEscritorio = crearNodoCarpeta("Escritorio", escritorio);
            raiz.getChildren().add(nodoEscritorio);
        }

        // Documentos
        File documentos = new File(System.getProperty("user.home") + File.separator + "Documents");
        if (documentos.exists()) {
            TreeItem<String> nodoDocumentos = crearNodoCarpeta("Documentos", documentos);
            raiz.getChildren().add(nodoDocumentos);
        }

        // Descargas
        File descargas = new File(System.getProperty("user.home") + File.separator + "Downloads");
        if (descargas.exists()) {
            TreeItem<String> nodoDescargas = crearNodoCarpeta("Descargas", descargas);
            raiz.getChildren().add(nodoDescargas);
        }

        // Unidades del sistema
        File[] unidades = File.listRoots();
        for (File unidad : unidades) {
            TreeItem<String> nodoUnidad = crearNodoCarpeta(unidad.getPath(), unidad);
            raiz.getChildren().add(nodoUnidad);
        }

        treeView.setRoot(raiz);
        treeView.setShowRoot(false);

        treeView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                carpetaSeleccionada = (File) newVal.getGraphic().getUserData();
                lblRuta.setText(carpetaSeleccionada.getAbsolutePath());
            }
        });

        lblRuta = new Label("Selecciona una carpeta...");
        lblRuta.setStyle("-fx-padding: 10; -fx-font-size: 13px; -fx-text-fill: #212529;");

        ScrollPane scrollPane = new ScrollPane(treeView);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        VBox vbox = new VBox(lblRuta, scrollPane);
        vbox.setPrefSize(550, 500);

        dialog.getDialogPane().setContent(vbox);

        ButtonType btnSeleccionar = new ButtonType("Seleccionar");
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonType.CANCEL.getButtonData());
        dialog.getDialogPane().getButtonTypes().addAll(btnSeleccionar, btnCancelar);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == btnSeleccionar && carpetaSeleccionada != null) {
                return buttonType;
            }
            return null;
        });
    }

    private TreeItem<String> crearNodoCarpeta(String nombre, File carpeta) {
        TreeItem<String> nodo = new TreeItem<>(nombre);
        nodo.setExpanded(false);

        Label icono = new Label("📁");
        icono.setUserData(carpeta);
        nodo.setGraphic(icono);

        nodo.addEventHandler(TreeItem.branchExpandedEvent(), e -> {
            if (nodo.getChildren().size() == 1 && nodo.getChildren().get(0).getValue().equals("...")) {
                nodo.getChildren().clear();
                cargarSubcarpetas(nodo, carpeta);
            }
        });

        TreeItem<String> placeholder = new TreeItem<>("...");
        nodo.getChildren().add(placeholder);

        return nodo;
    }

    private void cargarSubcarpetas(TreeItem<String> padre, File carpeta) {
        File[] archivos = carpeta.listFiles(File::isDirectory);

        if (archivos != null) {
            for (File archivo : archivos) {
                if (!archivo.isHidden()) {
                    TreeItem<String> hijo = new TreeItem<>(archivo.getName());
                    hijo.setExpanded(false);

                    Label icono = new Label("📁");
                    icono.setUserData(archivo);
                    hijo.setGraphic(icono);

                    hijo.addEventHandler(TreeItem.branchExpandedEvent(), e -> {
                        if (hijo.getChildren().size() == 1 && hijo.getChildren().get(0).getValue().equals("...")) {
                            hijo.getChildren().clear();
                            cargarSubcarpetas(hijo, archivo);
                        }
                    });

                    TreeItem<String> placeholder = new TreeItem<>("...");
                    hijo.getChildren().add(placeholder);
                    padre.getChildren().add(hijo);
                }
            }
        }
    }

    /**
     * Abre el explorador y retorna la carpeta seleccionada.
     */
    public File mostrar() {
        Optional<ButtonType> resultado = dialog.showAndWait();
        return resultado.isPresent() ? carpetaSeleccionada : null;
    }

    /**
     * Método estático para seleccionar una carpeta.
     */
    public static File seleccionarCarpeta() {
        ExplorerWindow explorer = new ExplorerWindow();
        return explorer.mostrar();
    }
}
