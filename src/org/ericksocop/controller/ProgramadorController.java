/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.ericksocop.controller;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.ericksocop.system.Main;

/**
 * FXML Controller class
 *
 * @author mauco
 */
public class ProgramadorController implements Initializable {

    private Main escenarioPrincipal;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private JFXButton btnCerrar;
    @FXML
    private FontAwesomeIcon iconoCerrar;
    @FXML
    private JFXButton btnMinimizar;
    @FXML
    private FontAwesomeIcon iconMinimizar;
    @FXML
    private JFXButton btnMaximizar;
    @FXML
    private FontAwesomeIcon iconMaximizar;
    @FXML
    private JFXButton btnRegresarPro;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
   public void handleButtonAction(ActionEvent event) throws Exception {
        if (event.getSource() == btnRegresarPro) {
            escenarioPrincipal.menuPrincipalView();
        }if (event.getSource() == iconoCerrar || event.getSource() == btnCerrar) {
            System.exit(0);
        }
        if (event.getSource() == iconMinimizar || event.getSource() == btnMinimizar) {
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            stage.setIconified(true);
        }

    }

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void verificacionRotacion() {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        if (stage.isMaximized()) {
            RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.5), iconMaximizar);
            rotateTransition.setByAngle(180);
            rotateTransition.setCycleCount(1);
            rotateTransition.setAutoReverse(false);
            stage.setMaximized(false);
        }
    }

    public void ventana(ActionEvent event) {

        if (event.getSource() == iconMaximizar || event.getSource() == btnMaximizar) {
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.5), iconMaximizar);
            rotateTransition.setByAngle(180);
            rotateTransition.setCycleCount(1);
            rotateTransition.setAutoReverse(false);

            if (stage.isMaximized()) {

                stage.setMaximized(false);
                escenarioPrincipal.setIsMaximized(false);
                iconMaximizar.setRotate(180);
            } else {
                stage.setMaximized(true);
                escenarioPrincipal.setIsMaximized(true);
            }
            rotateTransition.play();
        }
    }
    
    public void actualizarIconoMaximizar(boolean isMaximized) {
        if (isMaximized) {
            iconMaximizar.setRotate(180);
        } else {
            iconMaximizar.setRotate(0);
        }
    }
}
