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
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;
import org.ericksocop.bean.DetalleFactura;
import org.ericksocop.bean.Facturas;
import org.ericksocop.bean.Productos;
import org.ericksocop.dao.Conexion;
import org.ericksocop.system.Main;

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

    private Main escenarioPrincipal;

    private enum operador {
        AGREGRAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operador tipoDeOperador = operador.NINGUNO;

    private ObservableList<DetalleFactura> listaDetalleFactura;
    private ObservableList<Facturas> listaFacturas;
    private ObservableList<Productos> listaProductos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarDatos();
        cmbCodP.setItems(getProducto());
        cmbNumF.setItems(getFacturas());
        desactivarControles();

    }

    public void cargarDatos() {
        tvDetalleF.setItems(getDetalleF());
        colCodDetalleF.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("codigoDetalleFactura"));
        colPrecioU.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Double>("precioUnitario"));
        colCant.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("cantidad"));
        colNumF.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("numeroDeFactura"));
        colCodP.setCellValueFactory(new PropertyValueFactory<DetalleFactura, String>("codigoProducto"));
    }

    public void seleccionarElemento() {
        try {
            txtCodDetF.setText(String.valueOf(((DetalleFactura) tvDetalleF.getSelectionModel().getSelectedItem()).getCodigoDetalleFactura()));
            txtPrecioU.setText(String.valueOf(((DetalleFactura) tvDetalleF.getSelectionModel().getSelectedItem()).getPrecioUnitario()));
            txtCantidad.setText(String.valueOf(((DetalleFactura) tvDetalleF.getSelectionModel().getSelectedItem()).getCantidad()));
            cmbNumF.getSelectionModel().select(buscarFactura((((DetalleFactura) tvDetalleF.getSelectionModel().getSelectedItem()).getNumeroDeFactura())));
            cmbCodP.getSelectionModel().select(buscarProducto((((DetalleFactura) tvDetalleF.getSelectionModel().getSelectedItem()).getCodigoProducto())));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Por favor selecciona una fila válida", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public ObservableList<DetalleFactura> getDetalleF() {
        ArrayList<DetalleFactura> listaDetF = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_mostrarDetallesFactura()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaDetF.add(new DetalleFactura(resultado.getInt("codigoDetalleFactura"),
                        resultado.getDouble("precioUnitario"),
                        resultado.getInt("cantidad"),
                        resultado.getInt("numeroDeFactura"),
                        resultado.getString("codigoProducto")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaDetalleFactura = FXCollections.observableList(listaDetF);
    }

    public ObservableList<Productos> getProducto() {
        ArrayList<Productos> listaP = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_mostrarProductos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaP.add(new Productos(resultado.getString("codigoProducto"),
                        resultado.getString("descripcionProducto"),
                        resultado.getDouble("precioUnitario"),
                        resultado.getDouble("precioDocena"),
                        resultado.getDouble("precioMayor"),
                        resultado.getString("imagenProducto"),
                        resultado.getInt("existencia"),
                        resultado.getInt("CodigoTipoProducto"),
                        resultado.getInt("codigoProveedor")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProductos = FXCollections.observableList(listaP);
    }

    public ObservableList<Facturas> getFacturas() {
        ArrayList<Facturas> listaF = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_mostrarFacturas()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaF.add(new Facturas(resultado.getInt("numeroDeFactura"),
                        resultado.getString("estado"),
                        resultado.getDouble("totalFactura"),
                        resultado.getString("fechaFactura"),
                        resultado.getInt("clienteID"),
                        resultado.getInt("codigoEmpleado")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaFacturas = FXCollections.observableList(listaF);
    }

    public Productos buscarProducto(String productoID) {
        Productos resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_buscarProducto(?)}");
            procedimiento.setString(1, productoID);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Productos(
                        registro.getString("codigoProducto"),
                        registro.getString("descripcionProducto"),
                        registro.getDouble("precioUnitario"),
                        registro.getDouble("precioDocena"),
                        registro.getDouble("precioMayor"),
                        registro.getString("imagenProducto"),
                        registro.getInt("existencia"),
                        registro.getInt("CodigoTipoProducto"),
                        registro.getInt("codigoProveedor")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public Facturas buscarFactura(int idFactura) {
        Facturas resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_buscarFactura(?)}");
            procedimiento.setInt(1, idFactura);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Facturas(registro.getInt("numeroDeFactura"),
                        registro.getString("estado"),
                        registro.getDouble("totalFactura"),
                        registro.getString("fechaFactura"),
                        registro.getInt("clienteID"),
                        registro.getInt("codigoEmpleado"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public void Agregar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnAgregarDeF.setText("Guardar");
                btnEliminarDeF.setText("Cancelar");
                btnEditarDeF.setDisable(true);
                btnReportesDeF.setDisable(true);
                //imgAgregar.setImage(new Image("URL"));
                tipoDeOperador = operador.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                limpiarControles();
                cargarDatos();
                desactivarControles();
                btnAgregarDeF.setText("Agregar");
                btnEliminarDeF.setText("Eliminar");
                btnEditarDeF.setDisable(false);
                btnReportesDeF.setDisable(false);
                /*regresar de nuevo a sus imagenes originales
                imgAgregar.setImage(new Image("URL"));*/
                tipoDeOperador = operador.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void guardar() {
        DetalleFactura registro = new DetalleFactura();
        registro.setCodigoDetalleFactura(Integer.parseInt(txtCodDetF.getText()));
        registro.setPrecioUnitario(Double.parseDouble(txtPrecioU.getText()));
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        registro.setNumeroDeFactura(((Facturas) cmbNumF.getSelectionModel().getSelectedItem()).getNumeroDeFactura());
        registro.setCodigoProducto(((Productos) cmbCodP.getSelectionModel().getSelectedItem()).getCodigoProducto());

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_crearDetalleFactura(?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoDetalleFactura());
            procedimiento.setDouble(2, registro.getPrecioUnitario());
            procedimiento.setInt(3, registro.getCantidad());
            procedimiento.setInt(4, registro.getNumeroDeFactura());
            procedimiento.setString(5, registro.getCodigoProducto());
            procedimiento.execute();

            listaDetalleFactura.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                if (tvDetalleF.getSelectionModel().getSelectedItem() != null) {
                    btnEditarDeF.setText("Actualizar");
                    btnReportesDeF.setText("Cancelar");
                    btnEliminarDeF.setDisable(true);
                    btnAgregarDeF.setDisable(true);
                    activarControles();
                    txtCodDetF.setEditable(false);
                    tipoDeOperador = operador.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar una fila para editar");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditarDeF.setText("Editar");
                btnReportesDeF.setText("Reportes");
                btnAgregarDeF.setDisable(false);
                btnEliminarDeF.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperador = operador.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_editarDetalleFactura(?,?,?,?,?)}");
            DetalleFactura registro = (DetalleFactura) tvDetalleF.getSelectionModel().getSelectedItem();
            registro.setCodigoDetalleFactura(Integer.parseInt(txtCodDetF.getText()));
            registro.setPrecioUnitario(Double.parseDouble(txtPrecioU.getText()));
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            registro.setNumeroDeFactura(((Facturas) cmbNumF.getSelectionModel().getSelectedItem()).getNumeroDeFactura());
            registro.setCodigoProducto(((Productos) cmbCodP.getSelectionModel().getSelectedItem()).getCodigoProducto());
            procedimiento.setInt(1, registro.getCodigoDetalleFactura());
            procedimiento.setDouble(2, registro.getPrecioUnitario());
            procedimiento.setInt(3, registro.getCantidad());
            procedimiento.setInt(4, registro.getNumeroDeFactura());
            procedimiento.setString(5, registro.getCodigoProducto());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar() {
    // Verificar si se ha seleccionado un detalle de factura
    if (tvDetalleF.getSelectionModel().getSelectedItem() != null) {
        // Obtener el detalle de factura seleccionado
        DetalleFactura detalleSeleccionado = (DetalleFactura) tvDetalleF.getSelectionModel().getSelectedItem();
        int codigoDetalle = detalleSeleccionado.getCodigoDetalleFactura();

        // Confirmar la acción con el usuario
        int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas eliminar este detalle de factura?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                // Eliminar el detalle de factura
                PreparedStatement eliminarDetalle = Conexion.getInstance().getConexion()
                        .prepareCall("{call sp_eliminarDetalleFactura(?)}");
                eliminarDetalle.setInt(1, codigoDetalle);
                eliminarDetalle.execute();

                // Remover el detalle de factura de la lista observable
                listaDetalleFactura.remove(detalleSeleccionado);

                // Limpiar los controles
                limpiarControles();
                cargarDatos();
                JOptionPane.showMessageDialog(null, "Factura eliminada correctamente");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al eliminar el detalle de factura: " + e.getMessage());
                e.printStackTrace();
            }
        }
    } else {
        // Si no se ha seleccionado ningún detalle de factura, mostrar un mensaje de advertencia
        JOptionPane.showMessageDialog(null, "Debe seleccionar un detalle de factura para eliminarlo", "Advertencia",
                JOptionPane.WARNING_MESSAGE);
    }
}

    public void reportes() {
        switch (tipoDeOperador) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditarDeF.setText("Editar");
                btnReportesDeF.setText("Reportes");
                btnAgregarDeF.setDisable(false);
                btnEliminarDeF.setDisable(false);
                tipoDeOperador = operador.NINGUNO;

        }
    }

    public void desactivarControles() {
        txtCantidad.setEditable(false);
        txtCodDetF.setEditable(false);
        txtPrecioU.setEditable(false);
        cmbCodP.setDisable(true);
        cmbNumF.setDisable(true);
    }

    public void activarControles() {
        txtCantidad.setEditable(true);
        txtCodDetF.setEditable(true);
        txtPrecioU.setEditable(true);
        cmbCodP.setDisable(false);
        cmbNumF.setDisable(false);

    }

    public void limpiarControles() {
        txtCantidad.clear();
        txtCodDetF.clear();
        txtPrecioU.clear();
        cmbCodP.getSelectionModel().clearSelection();
        cmbNumF.getSelectionModel().clearSelection();
    }

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresarDetF) {
            escenarioPrincipal.FacturasView();
        }
    }

}
