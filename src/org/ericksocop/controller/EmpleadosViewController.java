/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.ericksocop.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javax.swing.JOptionPane;
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
        desactivarControles();
    }

    public void cargarDatos() {
        tvEmpleados.setItems(getEmpleados());
        colCodEmp.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoEmpleado"));
        colNomEmp.setCellValueFactory(new PropertyValueFactory<Empleados, String>("nombresEmpleado"));
        colApeEmp.setCellValueFactory(new PropertyValueFactory<Empleados, String>("apellidosEmpleado"));
        colSueldoEmp.setCellValueFactory(new PropertyValueFactory<Empleados, Double>("sueldo"));
        colDireEmp.setCellValueFactory(new PropertyValueFactory<Empleados, String>("direccion"));
        colTurnoEmp.setCellValueFactory(new PropertyValueFactory<Empleados, String>("turno"));
        colCodCargEmp.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoCargoEmpleado"));

    }

    public void seleccionarElemento() {
        try {
            txtCodigoEmp.setText(String.valueOf(((Empleados) tvEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
            txtnomEmp.setText((((Empleados) tvEmpleados.getSelectionModel().getSelectedItem()).getNombresEmpleado()));
            txtApellidosEmp.setText((((Empleados) tvEmpleados.getSelectionModel().getSelectedItem()).getApellidosEmpleado()));
            txtSueldo.setText(String.valueOf(((Empleados) tvEmpleados.getSelectionModel().getSelectedItem()).getSueldo()));
            txtDireccionEmp.setText((((Empleados) tvEmpleados.getSelectionModel().getSelectedItem()).getDireccion()));
            txtTurno.setText((((Empleados) tvEmpleados.getSelectionModel().getSelectedItem()).getTurno()));
            cmbCargoEmp.getSelectionModel().select(buscarCodigoEmp(((Empleados) tvEmpleados.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Por favor selecciona una fila válida", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public CargoEmpleados buscarCodigoEmp(int codigoEmpleado) {
        CargoEmpleados resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_buscarCargoEmpleado(?)}");
            procedimiento.setInt(1, codigoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new CargoEmpleados(registro.getInt("codigoCargoEmpleado"),
                        registro.getString("nombreCargo"),
                        registro.getString("descripcionCargo"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public ObservableList<Empleados> getEmpleados() {
        ArrayList<Empleados> listaEmp = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_mostrarEmpleados()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaEmp.add(new Empleados(resultado.getInt("codigoEmpleado"),
                        resultado.getString("nombresEmpleado"),
                        resultado.getString("apellidosEmpleado"),
                        resultado.getDouble("sueldo"),
                        resultado.getString("direccion"),
                        resultado.getNString("turno"),
                        resultado.getInt("codigoCargoEmpleado")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEmpleados = FXCollections.observableList(listaEmp);
    }

    public ObservableList<CargoEmpleados> getCargoE() {
        ArrayList<CargoEmpleados> listaCE = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_mostrarCargoEmpleado()}");

            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaCE.add(new CargoEmpleados(resultado.getInt("codigoCargoEmpleado"),
                        resultado.getString("nombreCargo"),
                        resultado.getString("descripcionCargo")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaCargo = FXCollections.observableList(listaCE);
    }

    public void Agregar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnAgregarE.setText("Guardar");
                btnEliminarE.setText("Cancelar");
                btnEditarE.setDisable(true);
                btnReportesE.setDisable(true);
                //imgAgregar.setImage(new Image("URL"));
                tipoDeOperador = operador.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                limpiarControles();
                cargarDatos();
                desactivarControles();
                btnAgregarE.setText("Agregar");
                btnEliminarE.setText("Eliminar");
                btnEditarE.setDisable(false);
                btnReportesE.setDisable(false);
                /*regresar de nuevo a sus imagenes originales
                imgAgregar.setImage(new Image("URL"));*/
                tipoDeOperador = operador.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void guardar() {
        Empleados registro = new Empleados();
        registro.setCodigoEmpleado(Integer.parseInt(txtCodigoEmp.getText()));
        registro.setNombresEmpleado(txtnomEmp.getText());
        registro.setApellidosEmpleado(txtApellidosEmp.getText());
        registro.setSueldo(Double.parseDouble(txtSueldo.getText()));
        registro.setDireccion(txtDireccionEmp.getText());
        registro.setTurno(txtTurno.getText());
        registro.setCodigoCargoEmpleado(((CargoEmpleados) cmbCargoEmp.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado());

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_crearEmpleado(?,?,?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoEmpleado());
            procedimiento.setString(2, registro.getNombresEmpleado());
            procedimiento.setString(3, registro.getApellidosEmpleado());
            procedimiento.setDouble(4, registro.getSueldo());
            procedimiento.setString(5, registro.getDireccion());
            procedimiento.setString(6, registro.getTurno());
            procedimiento.setInt(7, registro.getCodigoCargoEmpleado());
            procedimiento.execute();

            listaEmpleados.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                if (tvEmpleados.getSelectionModel().getSelectedItem() != null) {
                    btnEditarE.setText("Actualizar");
                    btnReportesE.setText("Cancelar");
                    btnEliminarE.setDisable(true);
                    btnAgregarE.setDisable(true);
                    activarControles();
                    txtCodigoEmp.setEditable(false);
                    tipoDeOperador = operador.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar una fila para editar");
                }
                break;
            case ACTUALIZAR:
                try {
                actualizar();
            } catch (Exception e) {
                e.printStackTrace();
            }
            btnEditarE.setText("Editar");
            btnReportesE.setText("Reportes");
            btnAgregarE.setDisable(false);
            btnEliminarE.setDisable(false);
            desactivarControles();
            limpiarControles();
            tipoDeOperador = operador.NINGUNO;
            cargarDatos();
            break;
        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_editarEmpleado(?,?,?,?,?,?,?)}");
            Empleados registro = (Empleados) tvEmpleados.getSelectionModel().getSelectedItem();
            registro.setCodigoEmpleado(Integer.parseInt(txtCodigoEmp.getText()));
            registro.setNombresEmpleado(txtnomEmp.getText());
            registro.setApellidosEmpleado(txtApellidosEmp.getText());
            registro.setSueldo(Double.parseDouble(txtSueldo.getText()));
            registro.setDireccion(txtDireccionEmp.getText());
            registro.setTurno(txtTurno.getText());
            registro.setCodigoCargoEmpleado(((CargoEmpleados) cmbCargoEmp.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado());
            procedimiento.setInt(1, registro.getCodigoEmpleado());
            procedimiento.setString(2, registro.getNombresEmpleado());
            procedimiento.setString(3, registro.getApellidosEmpleado());
            procedimiento.setDouble(4, registro.getSueldo());
            procedimiento.setString(5, registro.getDireccion());
            procedimiento.setString(6, registro.getTurno());
            procedimiento.setInt(7, registro.getCodigoCargoEmpleado());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar() throws SQLException {
        switch (tipoDeOperador) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregarE.setText("Agregar");
                btnEliminarE.setText("Eliminar");
                btnEditarE.setDisable(false);
                btnReportesE.setDisable(false);
                tipoDeOperador = operador.NINGUNO;
                break;
            default:
                if (tvEmpleados.getSelectionModel().getSelectedItem() != null) {
                    int ans = JOptionPane.showConfirmDialog(null, "Confirma esta Acción", "Verificación",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (ans == JOptionPane.YES_OPTION) {
                        try {
                            Empleados empleadoSeleccionado = (Empleados) tvEmpleados.getSelectionModel().getSelectedItem();
                            int codigoEmpleado = empleadoSeleccionado.getCodigoEmpleado();

                            // Eliminar las referencias en DetalleFactura a través de Factura
                            PreparedStatement eliminarDetalleFactura = Conexion.getInstance().getConexion()
                                    .prepareCall("{call sp_eliminarDetalleFacturaPorFactura(?)}");
                            eliminarDetalleFactura.setInt(1, codigoEmpleado);
                            eliminarDetalleFactura.execute();

                            // Eliminar las facturas del empleado
                            PreparedStatement eliminarFactura = Conexion.getInstance().getConexion()
                                    .prepareCall("{call sp_eliminarFacturaPorEmpleado(?)}");
                            eliminarFactura.setInt(1, codigoEmpleado);
                            eliminarFactura.execute();

                            // Finalmente, eliminar el empleado
                            PreparedStatement eliminarEmpleado = Conexion.getInstance().getConexion()
                                    .prepareCall("{call sp_eliminarEmpleado(?)}");
                            eliminarEmpleado.setInt(1, codigoEmpleado);
                            eliminarEmpleado.execute();

                            listaEmpleados.remove(empleadoSeleccionado);
                            limpiarControles();
                            cargarDatos();
                            JOptionPane.showMessageDialog(null, "Empleado eliminada correctamente");
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "Error al eliminar el empleado: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar una fila para eliminar");
                }
                break;
        }
    }

    public void reporte() {
        switch (tipoDeOperador) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditarE.setText("Editar");
                btnReportesE.setText("Reportes");
                btnAgregarE.setDisable(false);
                btnEliminarE.setDisable(false);
                tipoDeOperador = operador.NINGUNO;
        }
    }

    public void desactivarControles() {
        txtCodigoEmp.setEditable(false);
        txtnomEmp.setEditable(false);
        txtApellidosEmp.setEditable(false);
        txtSueldo.setEditable(false);
        txtDireccionEmp.setEditable(false);
        txtTurno.setEditable(false);
        cmbCargoEmp.setDisable(true);
    }

    public void activarControles() {
        txtCodigoEmp.setEditable(true);
        txtnomEmp.setEditable(true);
        txtApellidosEmp.setEditable(true);
        txtSueldo.setEditable(true);
        txtDireccionEmp.setEditable(true);
        txtTurno.setEditable(true);
        cmbCargoEmp.setDisable(false);
    }

    public void limpiarControles() {
        txtCodigoEmp.clear();
        txtnomEmp.clear();
        txtApellidosEmp.clear();
        txtSueldo.clear();
        txtDireccionEmp.clear();
        txtTurno.clear();
        cmbCargoEmp.getSelectionModel().clearSelection();
    }

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresarE) {
            escenarioPrincipal.menuPrincipalView();
        }
        if (event.getSource() == btnCargoE) {
            escenarioPrincipal.cargoEmpleadosView();
        }
    }

}
