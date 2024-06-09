/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ericksocop.controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.ericksocop.system.Main;

/**
 *
 * @author mauco
 */
public class MenuPrincipalController implements Initializable {

    private Main escenarioPrincipal;
    @FXML
    private ImageView exit;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label menu;
    @FXML
    private Label menuClose;
    @FXML
    private AnchorPane slider;
    @FXML
    private JFXButton btnProgramador;
    @FXML
    private ImageView iconProgra;
    @FXML
    private JFXButton btnProductos;
    @FXML
    private JFXButton btnProveedores;
    @FXML
    private JFXButton btnClientes;
    @FXML
    private JFXButton btnEmpleados;
    @FXML
    private JFXButton btnCompras;
    @FXML
    private JFXButton btnFacturas;
    @FXML
    private ImageView minimize;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        slider.setTranslateX(-176);
        menu.setVisible(true);
        menuClose.setVisible(false);

    }
    
    

    @FXML
    public void menuSlider(MouseEvent event) {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.5));
        slide.setNode(slider);

        slide.setToX(0);
        slide.play();

        slider.setTranslateX(-176);

        slide.setOnFinished((ActionEvent e) -> {
            menu.setVisible(false);
            menuClose.setVisible(true);
        });

    }

    @FXML
    public void menuSliderClose(MouseEvent event) {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.5));
        slide.setNode(slider);

        slide.setToX(-176);
        slide.play();

        slider.setTranslateX(0);

        slide.setOnFinished((ActionEvent e) -> {
            menu.setVisible(true);
            menuClose.setVisible(false);
        });
    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
         if (event.getSource() == btnProgramador || event.getSource() == iconProgra) {
            escenarioPrincipal.programadorView();
        }if (event.getSource() == btnClientes) {
            escenarioPrincipal.menuClienteView();
        }if (event.getSource() == btnCompras) {
            escenarioPrincipal.CompraView();
        }if (event.getSource() == btnEmpleados) {
            escenarioPrincipal.EmpleadosView();
        }if (event.getSource() == btnFacturas) {
            escenarioPrincipal.FacturasView();
        }if (event.getSource() == btnProductos) {
            escenarioPrincipal.ProductosView();
        }if (event.getSource() == btnProveedores) {
            escenarioPrincipal.ProveedorView();
        }
    }
    

    public void minimizar(MouseEvent event) {
        if(event.getSource() == minimize){
            Stage stage = (Stage)anchorPane.getScene().getWindow();
            stage.setIconified(true);
        }
    }
    
    @FXML
    public void salir(MouseEvent event) {
        if (event.getSource() == exit) {
            System.exit(0);
        }
    }

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    
    

}
