/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.ericksocop.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.ericksocop.bean.CargoEmpleados;
import org.ericksocop.dao.Conexion;
import org.ericksocop.system.Main;

/**
 * FXML Controller class
 *
 * @author mauco
 */
public class CargoEmpleadosViewController implements Initializable {

    private enum operador {
        AGREGRAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operador tipoDeOperador = operador.NINGUNO;
    @FXML
    private TableView tvCargoE;
    @FXML
    private Button btnAgregarCE;
    @FXML
    private Button btnEditarCE;
    @FXML
    private TableColumn colCodigoCargoE;
    @FXML
    private TableColumn colNombreC;
    @FXML
    private TableColumn colDescripcionC;
    @FXML
    private Button btnEliminarCE;
    @FXML
    private Button btnReportesCE;
    @FXML
    private Button btnRegresarCE;
    @FXML
    private TextField txtCodigoCargoE;
    @FXML
    private TextField txtNombreCargo;
    @FXML
    private TextField txtDescripcionC;
    private ObservableList<CargoEmpleados> listaCargo;
    private Main escenarioPrincipal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarDatos();
        desactivarControles();
    }

    public void cargarDatos() {
        tvCargoE.setItems(getCargoE());
        colCodigoCargoE.setCellValueFactory(new PropertyValueFactory<CargoEmpleados, Integer>("codigoCargoEmpleado"));
        colDescripcionC.setCellValueFactory(new PropertyValueFactory<CargoEmpleados, String>("descripcionCargo"));
        colNombreC.setCellValueFactory(new PropertyValueFactory<CargoEmpleados, String>("nombreCargo"));

    }

    public void seleccionarElemento() {

        txtCodigoCargoE.setText(String.valueOf(((CargoEmpleados) tvCargoE.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado()));
        txtNombreCargo.setText((((CargoEmpleados) tvCargoE.getSelectionModel().getSelectedItem()).getNombreCargo()));
        txtDescripcionC.setText((((CargoEmpleados) tvCargoE.getSelectionModel().getSelectedItem()).getDescripcionCargo()));
    }

    public ObservableList<CargoEmpleados> getCargoE() {
        ArrayList<CargoEmpleados> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_mostrarCargoEmpleado()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new CargoEmpleados(resultado.getInt("codigoCargoEmpleado"),
                        resultado.getString("nombreCargo"),
                        resultado.getString("descripcionCargo")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaCargo = FXCollections.observableList(lista);
    }

    public void Agregar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnAgregarCE.setText("Guardar");
                btnEliminarCE.setText("Cancelar");
                btnEditarCE.setDisable(true);
                btnReportesCE.setDisable(true);
                //imgAgregar.setImage(new Image("URL"));
                tipoDeOperador = operador.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                limpiarControles();
                cargarDatos();
                desactivarControles();
                btnAgregarCE.setText("Agregar");
                btnEliminarCE.setText("Eliminar");
                btnEditarCE.setDisable(false);
                btnReportesCE.setDisable(false);
                /*regresar de nuevo a sus imagenes originales
                imgAgregar.setImage(new Image("URL"));*/
                tipoDeOperador = operador.NINGUNO;
                break;
        }

    }

    public void guardar() {
        CargoEmpleados registro = new CargoEmpleados();
        registro.setCodigoCargoEmpleado(Integer.parseInt(txtCodigoCargoE.getText()));
        registro.setNombreCargo(txtNombreCargo.getText());
        registro.setDescripcionCargo(txtDescripcionC.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_agregarCargoEmpleado(?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoCargoEmpleado());
            procedimiento.setString(2, registro.getNombreCargo());
            procedimiento.setString(3, registro.getDescripcionCargo());
            procedimiento.execute();
            listaCargo.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar() {
        switch (tipoDeOperador) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregarCE.setText("Agregar");
                btnEliminarCE.setText("Eliminar");
                btnEditarCE.setDisable(false);
                btnReportesCE.setDisable(false);
                /*regresar de nuevo a sus imagenes originales
                imgAgregar.setImage(new Image("URL"));*/
                tipoDeOperador = operador.NINGUNO;
                break;
            default:
                if (tvCargoE.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmas la eliminacion del registro?", "Eliminar Cargo Empleados", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarCargoEmpleado(?)}");
                            procedimiento.setInt(1, ((CargoEmpleados) tvCargoE.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado());
                            procedimiento.execute();
                            listaCargo.remove(tvCargoE.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un cliente para eliminar");
                }

                break;
        }
    }

    public void editar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                if (tvCargoE.getSelectionModel().getSelectedItem() != null) {
                    btnEditarCE.setText("Actualizar");
                    btnReportesCE.setText("Cancelar");
                    btnEliminarCE.setDisable(true);
                    btnAgregarCE.setDisable(true);
                    activarControles();
                    txtCodigoCargoE.setEditable(false);
                    tipoDeOperador = operador.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un cliente para editar");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditarCE.setText("Editar");
                btnReportesCE.setText("Reportes");
                btnEliminarCE.setDisable(false);
                btnAgregarCE.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperador = operador.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void reportes() {
        switch (tipoDeOperador) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditarCE.setText("Editar");
                btnReportesCE.setText("Reportes");
                btnAgregarCE.setDisable(false);
                btnEliminarCE.setDisable(false);
                tipoDeOperador = operador.NINGUNO;

        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_editarCargoEmpleado(?,?,?)}");
            CargoEmpleados registro = (CargoEmpleados) tvCargoE.getSelectionModel().getSelectedItem();
            registro.setNombreCargo(txtNombreCargo.getText());
            registro.setDescripcionCargo(txtDescripcionC.getText());
            procedimiento.setInt(1, registro.getCodigoCargoEmpleado());
            procedimiento.setString(2, registro.getNombreCargo());
            procedimiento.setString(3, registro.getDescripcionCargo());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void activarControles() {
        txtCodigoCargoE.setEditable(true);
        txtDescripcionC.setEditable(true);
        txtNombreCargo.setEditable(true);
    }

    public void limpiarControles() {
        txtCodigoCargoE.clear();
        txtDescripcionC.clear();
        txtNombreCargo.clear();
    }

    public void desactivarControles() {
        txtCodigoCargoE.setEditable(false);
        txtDescripcionC.setEditable(false);
        txtNombreCargo.setEditable(false);
    }

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresarCE) {
            escenarioPrincipal.EmpleadosView();
        }
    }
}
