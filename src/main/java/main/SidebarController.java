package main;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class SidebarController {

    @FXML
    private Button inicioButton;

    @FXML
    private Button renovarButton;

    @FXML
    private Button recordatorioButton;

    @FXML
    private Button historialButton;

    @FXML
    private Button calendarioButton;

    @FXML
    private BorderPane mainContainer;
    @FXML
    private VBox contentPane;

    public void initialize() {
        loadContent("/main/Inicio.fxml");
    }

    private void loadContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent content = loader.load();
            mainContainer.setCenter(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToInicio(ActionEvent event) {
        loadContent("/main/Inicio.fxml");
    }

    @FXML
    private void switchToRenovar(ActionEvent event) {
        loadContent("/main/Renovar.fxml");
    }

    @FXML
    private void switchToRecordatorio(ActionEvent event) {
        loadContent("/main/Recordatorio.fxml");
    }

    @FXML
    private void switchToHistorial(ActionEvent event) {
        loadContent("/main/Historial.fxml");
    }

    @FXML
    private void switchToCalendario(ActionEvent event) {
        loadContent("/main/Calendario.fxml");
    }
}
