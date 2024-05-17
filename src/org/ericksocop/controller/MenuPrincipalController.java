/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ericksocop.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import org.ericksocop.system.Main;

/**
 *
 * @author mauco
 */
public class MenuPrincipalController implements Initializable {

    private Main escenarioPrincipal;
    @FXML
    MenuItem btnMenuClientes;
    @FXML
    MenuItem btnProveedor;
    @FXML
    MenuItem btnProgramador;
    @FXML
    MenuItem btnMenuProductos;
    @FXML
    MenuItem btnMenuEmp;

    @FXML
    MenuItem btnCompra;

    @FXML
    MenuItem btnFacturas;

    @FXML
    private MenuItem btnDetalleCompra;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;

    }

    public MenuItem getBtnMenuClientes() {
        return btnMenuClientes;
    }

    public void setBtnMenuClientes(MenuItem btnMenuClientes) {
        this.btnMenuClientes = btnMenuClientes;
    }

    public MenuItem getBtnProgramador() {
        return btnProgramador;
    }

    public void setBtnProgramador(MenuItem btnProgramador) {
        this.btnProgramador = btnProgramador;
    }

    public void menuClientesView() {
        escenarioPrincipal.menuClienteView();
    }

    public void menuCargoEView() {
        escenarioPrincipal.cargoEmpleadosView();
    }

    public MenuItem getBtnProveedor() {
        return btnProveedor;
    }

    public void setBtnProveedor(MenuItem btnProveedor) {
        this.btnProveedor = btnProveedor;
    }

    public MenuItem getBtnMenuProductos() {
        return btnMenuProductos;
    }

    public void setBtnMenuProductos(MenuItem btnMenuProductos) {
        this.btnMenuProductos = btnMenuProductos;
    }

    public MenuItem getBtnCompra() {
        return btnCompra;
    }

    public void setBtnCompra(MenuItem btnCompra) {
        this.btnCompra = btnCompra;
    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnMenuClientes) {
            escenarioPrincipal.menuClienteView();
        }
        if (event.getSource() == btnProgramador) {
            escenarioPrincipal.programadorView();
        }
        if (event.getSource() == btnMenuEmp) {
            escenarioPrincipal.EmpleadosView();
        }
        if (event.getSource() == btnMenuProductos) {
            escenarioPrincipal.ProductosView();
        }
        if (event.getSource() == btnProveedor) {
            escenarioPrincipal.ProveedorView();
        }
        if (event.getSource() == btnCompra) {
            escenarioPrincipal.CompraView();
        }
        if (event.getSource() == btnDetalleCompra) {
            escenarioPrincipal.DetalleCompra();
        }
        if (event.getSource() == btnFacturas) {
            escenarioPrincipal.FacturasView();
        }
    }

}
