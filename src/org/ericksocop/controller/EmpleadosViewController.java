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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.ericksocop.bean.CargoEmpleados;
import org.ericksocop.bean.Empleados;
import org.ericksocop.dao.Conexion;
import org.ericksocop.system.Main;

/**
 * FXML Controller class
 *
 * @author mauco
 */
public class EmpleadosViewController implements Initializable {

    @FXML
    private TableView tvEmpleados;
    @FXML
    private TableColumn colCodEmp;
    @FXML
    private TableColumn colNomEmp;
    @FXML
    private TableColumn colApeEmp;
    @FXML
    private TableColumn colSueldoEmp;
    @FXML
    private TableColumn colDireEmp;
    @FXML
    private TableColumn colTurnoEmp;
    @FXML
    private TableColumn colCodCargEmp;
    @FXML
    private Button btnAgregarE;
    @FXML
    private Button btnEditarE;
    @FXML
    private Button btnEliminarE;
    @FXML
    private Button btnReportesE;
    @FXML
    private TextField txtCodigoEmp;
    @FXML
    private TextField txtnomEmp;
    @FXML
    private TextField txtApellidosEmp;
    @FXML
    private TextField txtSueldo;
    @FXML
    private TextField txtDireccionEmp;
    @FXML
    private ComboBox cmbCargoEmp;
    @FXML
    private TextField txtTurno;
    @FXML
    private Button btnRegresarE;
    @FXML
    private Button btnCargoE;

    private Main escenarioPrincipal;
    private ObservableList<Empleados> listaEmpleados;
    private ObservableList<CargoEmpleados> listaCargo;

    private enum operador {
        AGREGRAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operador tipoDeOperador = operador.NINGUNO;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarDatos();
        cmbCargoEmp.setItems(getCargoE());
    }

    public void cargarDatos(){
        tvEmpleados.setItems(getEmpleados());
        colCodEmp.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoEmpleado"));
        colNomEmp.setCellValueFactory(new PropertyValueFactory <Empleados, String>("nombresEmpleado"));
        colApeEmp.setCellValueFactory(new PropertyValueFactory <Empleados, String>("apellidosEmpleado"));
        colSueldoEmp.setCellValueFactory(new PropertyValueFactory<Empleados, Double>("sueldo"));
        colDireEmp.setCellValueFactory(new PropertyValueFactory <Empleados, String> ("direccion"));
        colTurnoEmp.setCellValueFactory(new PropertyValueFactory <Empleados, String>("turno"));
        colCodCargEmp.setCellValueFactory(new PropertyValueFactory <Empleados, Integer> ("codigoCargoEmpleado"));
        
    }

    public void seleccionarElemento(){
    txtCodigoEmp.setText(String.valueOf(((Empleados)tvEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
        txtnomEmp.setText((((Empleados)tvEmpleados.getSelectionModel().getSelectedItem()).getNombresEmpleado()));
        txtApellidosEmp.setText((((Empleados)tvEmpleados.getSelectionModel().getSelectedItem()).getApellidosEmpleado()));
        txtSueldo.setText(String.valueOf(((Empleados)tvEmpleados.getSelectionModel().getSelectedItem()).getSueldo()));
        txtDireccionEmp.setText((((Empleados)tvEmpleados.getSelectionModel().getSelectedItem()).getDireccion()));
        txtTurno.setText((((Empleados)tvEmpleados.getSelectionModel().getSelectedItem()).getTurno()));
        cmbCargoEmp.getSelectionModel().select(buscarCodigoEmp(((Empleados)tvEmpleados.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado()));
    }
    
    public CargoEmpleados buscarCodigoEmp(int codigoEmpleado){
        CargoEmpleados resultado = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_buscarCargoEmpleado(?)}");
                procedimiento.setInt(1, codigoEmpleado);
                ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                    resultado = new CargoEmpleados(registro.getInt("codigoCargoEmpleado"),
                        registro.getString("nombreCargo"),
                        registro.getString("descripcionCargo"));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        return resultado;
    }
    
    public ObservableList <Empleados> getEmpleados(){
        ArrayList <Empleados> listaEmp = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_mostrarEmpleados()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                listaEmp.add(new Empleados(resultado.getInt("codigoEmpleado"),
                        resultado.getString("nombresEmpleado"),
                        resultado.getString("apellidosEmpleado"),
                        resultado.getDouble("sueldo"),
                        resultado.getString("direccion"),
                        resultado.getNString("turno"),
                        resultado.getInt("codigoCargoEmpleado")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return  listaEmpleados = FXCollections.observableList(listaEmp);
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
    }

    public void editar() {
    }

    public void eliminar() {
    }

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    

    public void handleButtonAction(ActionEvent event) {
        if(event.getSource() == btnRegresarE){
            escenarioPrincipal.menuPrincipalView();
        }
        if(event.getSource() == btnCargoE){
            escenarioPrincipal.cargoEmpleadosView();
        }
    }

}
