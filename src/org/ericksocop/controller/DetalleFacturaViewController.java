/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.ericksocop.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author mauco
 */
public class DetalleFacturaViewController implements Initializable {

    @FXML
    private TableView tvDetalleF;
    @FXML
    private TableColumn colCodDetalleF;
    @FXML
    private TableColumn colPrecioU;
    @FXML
    private TableColumn colCant;
    @FXML
    private TableColumn colNumF;
    @FXML
    private TableColumn colCodP;
    @FXML
    private Button btnAgregarDeF;
    @FXML
    private Button btnEditarDeF;
    @FXML
    private Button btnEliminarDeF;
    @FXML
    private Button btnReportesDeF;
    @FXML
    private TextField txtCodDetF;
    @FXML
    private TextField txtPrecioU;
    @FXML
    private TextField txtCantidad;
    @FXML
    private ComboBox cmbCodP;
    @FXML
    private ComboBox cmbNumF;
    @FXML
    private Button btnRegresarDetF;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    


    public void seleccionarElemento( ) {
    }


    public void Agregar( ) {
    }


    public void editar( ) {
    }

    public void eliminar( ) {
    }

    public void reportes( ) {
    }


    public void handleButtonAction(ActionEvent event) {
    }
    
}
