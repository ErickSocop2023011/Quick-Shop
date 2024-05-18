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
import org.ericksocop.bean.Compras;
import org.ericksocop.bean.DetalleCompra;
import org.ericksocop.bean.DetalleFactura;
import org.ericksocop.bean.Productos;
import org.ericksocop.dao.Conexion;
import org.ericksocop.system.Main;

/**
 * FXML Controller class
 *
 * @author mauco
 */
public class DetalleCompraViewController implements Initializable {

    @FXML
    private TableView tvDetalleC;
    @FXML
    private TableColumn colCodDetCo;
    @FXML
    private TableColumn colCostU;
    @FXML
    private TableColumn colCant;
    @FXML
    private TableColumn colCodPro;
    @FXML
    private TableColumn colNumDoc;
    @FXML
    private Button btnRegresarDetC;
    @FXML
    private Button btnAgregarDeC;
    @FXML
    private Button btnEditarDeC;
    @FXML
    private Button btnEliminarDeC;
    @FXML
    private Button btnReportesDeC;
    @FXML
    private TextField txtCodDetC;
    @FXML
    private TextField txtCostoU;
    @FXML
    private TextField txtCantidad;
    @FXML
    private ComboBox cmbNumDoc;
    @FXML
    private ComboBox cmbCodPro;

    private Main escenarioPrincipal;
    private ObservableList<DetalleCompra> listaDetalleC;
    private ObservableList<Productos> listaProductos;
    private ObservableList<Compras> listaCompras;

    private enum operador {
        GREGRAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }

    private operador tipoDeOperador = operador.NINGUNO;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarDatos();
        cmbCodPro.setItems(getProducto());
        cmbNumDoc.setItems(getCompra());
        desactivarControles();

    }

    public void cargarDatos() {
        tvDetalleC.setItems(getDetalleC());
        colCodDetCo.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("codigoDetalleCompra"));
        colCostU.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Double>("costoUnitario"));
        colCant.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("cantidad"));
        colCodPro.setCellValueFactory(new PropertyValueFactory<DetalleCompra, String>("codigoProducto"));
        colNumDoc.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("numeroDocumento"));
    }

    public void seleccionarElemento() {
        try {
            txtCodDetC.setText(String.valueOf(((DetalleCompra) tvDetalleC.getSelectionModel().getSelectedItem()).getCodigoDetalleCompra()));
            txtCostoU.setText(String.valueOf(((DetalleCompra) tvDetalleC.getSelectionModel().getSelectedItem()).getCostoUnitario()));
            txtCantidad.setText(String.valueOf(((DetalleCompra) tvDetalleC.getSelectionModel().getSelectedItem()).getCantidad()));
            cmbCodPro.getSelectionModel().select(buscarCodigoProducto((((DetalleCompra) tvDetalleC.getSelectionModel().getSelectedItem()).getCodigoProducto())));
            cmbNumDoc.getSelectionModel().select((((DetalleCompra) tvDetalleC.getSelectionModel().getSelectedItem()).getNumeroDocumento()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Por favor selecciona una fila válida", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public ObservableList<DetalleCompra> getDetalleC() {
        ArrayList<DetalleCompra> listaDeC = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_mostrarDetallesCompra()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaDeC.add(new DetalleCompra(resultado.getInt("codigoDetalleCompra"),
                        resultado.getDouble("costoUnitario"),
                        resultado.getInt("cantidad"),
                        resultado.getString("codigoProducto"),
                        resultado.getInt("numeroDocumento")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaDetalleC = FXCollections.observableList(listaDeC);
    }

    public Productos buscarCodigoProducto(String codigoProducto) {
        Productos resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_buscarProducto(?)}");
            procedimiento.setString(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Productos(registro.getString("codigoProducto"),
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

    public Compras buscarNumDoc(int numDoc) {
        Compras resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_buscarcompra(?)}");
            procedimiento.setInt(1, numDoc);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Compras(registro.getInt("numeroDocumento"),
                        registro.getString("fechaDocumento"),
                        registro.getString("descripcionCompra"),
                        registro.getDouble("totalDocumento")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
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

    public ObservableList<Compras> getCompra() {
        ArrayList<Compras> listaC = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_mostrarcompras()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaC.add(new Compras(resultado.getInt("numeroDocumento"),
                        resultado.getString("fechaDocumento"),
                        resultado.getString("descripcionCompra"),
                        resultado.getDouble("totalDocumento")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaCompras = FXCollections.observableList(listaC);
    }

    public void Agregar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnAgregarDeC.setText("Guardar");
                btnEliminarDeC.setText("Cancelar");
                btnEditarDeC.setDisable(true);
                btnReportesDeC.setDisable(true);
                //imgAgregar.setImage(new Image("URL"));
                tipoDeOperador = operador.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                limpiarControles();
                cargarDatos();
                desactivarControles();
                btnAgregarDeC.setText("Agregar");
                btnEliminarDeC.setText("Eliminar");
                btnEditarDeC.setDisable(false);
                btnReportesDeC.setDisable(false);
                /*regresar de nuevo a sus imagenes originales
                imgAgregar.setImage(new Image("URL"));*/
                tipoDeOperador = operador.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void guardar() {
    DetalleCompra registro = new DetalleCompra();
    registro.setCodigoDetalleCompra(Integer.parseInt(txtCodDetC.getText()));
    registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
    registro.setCostoUnitario(Double.parseDouble(txtCostoU.getText()));
    registro.setCodigoProducto((((Productos) cmbCodPro.getSelectionModel().getSelectedItem()).getCodigoProducto()));
    registro.setNumeroDocumento((((Compras) cmbNumDoc.getSelectionModel().getSelectedItem()).getNumeroDocumento()));

    try {
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_crearDetalleCompra(?,?,?, ?,?)}");
        procedimiento.setInt(1, registro.getCodigoDetalleCompra());
        procedimiento.setDouble(2, registro.getCostoUnitario());
        procedimiento.setInt(3, registro.getCantidad());
        procedimiento.setString(4, registro.getCodigoProducto());
        procedimiento.setInt(5, registro.getNumeroDocumento());
        procedimiento.execute();
        actualizarStockInsertarDetalle(registro.getCodigoProducto(), registro.getCantidad());
        listaDetalleC.add(registro);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    public void actualizarStockInsertarDetalle(String codigoProducto, int cantidad) {
        DetalleFactura detalleFactura = new DetalleFactura(); 
    detalleFactura.setCodigoProducto(codigoProducto); 
    detalleFactura.setCantidad(cantidad); 
    try {
        PreparedStatement procedimiento = Conexion.getInstance().getConexion()
            .prepareCall("{call ActualizarStockInsertarDetalle(?, ?)}");
        procedimiento.setString(1, codigoProducto);
        procedimiento.setInt(2, cantidad);
        procedimiento.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();

    }
}

    public void editar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                if (tvDetalleC.getSelectionModel().getSelectedItem() != null) {
                    btnEditarDeC.setText("Actualizar");
                    btnReportesDeC.setText("Cancelar");
                    btnEliminarDeC.setDisable(true);
                    btnAgregarDeC.setDisable(true);
                    activarControles();
                    txtCodDetC.setEditable(false);
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
            btnEditarDeC.setText("Editar");
            btnReportesDeC.setText("Reportes");
            btnAgregarDeC.setDisable(false);
            btnEliminarDeC.setDisable(false);
            limpiarControles();
            desactivarControles();

            tipoDeOperador = operador.NINGUNO;
            cargarDatos();
            break;
        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_editarDetalleCompra(?,?,?,?,?)}");
            DetalleCompra registro = (DetalleCompra) tvDetalleC.getSelectionModel().getSelectedItem();
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            registro.setCostoUnitario(Double.parseDouble(txtCostoU.getText()));
            registro.setCodigoProducto((((Productos) cmbCodPro.getSelectionModel().getSelectedItem()).getCodigoProducto()));
            registro.setNumeroDocumento((((Compras) cmbNumDoc.getSelectionModel().getSelectedItem()).getNumeroDocumento()));
            procedimiento.setInt(1, registro.getCodigoDetalleCompra());
            procedimiento.setDouble(2, registro.getCostoUnitario());
            procedimiento.setInt(3, registro.getCantidad());
            procedimiento.setString(4, registro.getCodigoProducto());
            procedimiento.setInt(5, registro.getNumeroDocumento());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void eliminar() {
    switch (tipoDeOperador) {
        case ACTUALIZAR:
            desactivarControles();
            limpiarControles();
            btnAgregarDeC.setText("Agregar");
            btnEliminarDeC.setText("Eliminar");
            btnEditarDeC.setDisable(false);
            btnReportesDeC.setDisable(false);
            /*regresar de nuevo a sus imagenes originales
            imgAgregar.setImage(new Image("URL"));*/
            tipoDeOperador = operador.NINGUNO;
            break;
        default:
            if (tvDetalleC.getSelectionModel().getSelectedItem() != null) {
                int respuesta = JOptionPane.showConfirmDialog(null, "Confirmas la eliminacion del registro?", "Eliminar Detalle Compra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (respuesta == JOptionPane.YES_NO_OPTION) {
                    DetalleCompra detalleSeleccionado = (DetalleCompra) tvDetalleC.getSelectionModel().getSelectedItem();
                    try {
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarDetalleCompra(?)}");
                        procedimiento.setInt(1, detalleSeleccionado.getCodigoDetalleCompra());
                        procedimiento.execute();
                        listaDetalleC.remove(detalleSeleccionado);
                        limpiarControles();
                        cargarDatos();
                        JOptionPane.showMessageDialog(null, "Detalle Compra eliminado correctamente");
                        actualizarStockEliminarDetalle(detalleSeleccionado.getCodigoProducto(), detalleSeleccionado.getCantidad());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Debe de seleccionar una fila Compra para eliminar");
            }

            break;
    }
}
    
    public void actualizarStockEliminarDetalle(String codigoProducto, int cantidad) {
        DetalleFactura detalleFactura = new DetalleFactura(); // Instancia de DetalleFactura
detalleFactura.setCodigoProducto(codigoProducto); // Establecer el código del producto
detalleFactura.setCantidad(cantidad); // Establecer la cantidad
    try {
        PreparedStatement procedimiento = Conexion.getInstance().getConexion()
            .prepareCall("{call ActualizarStockEliminarDetalle(?, ?)}");
        procedimiento.setString(1, codigoProducto);
        procedimiento.setInt(2, cantidad);
        procedimiento.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        // Manejar cualquier excepción
    }
}

    public void reportes() {
        switch (tipoDeOperador) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditarDeC.setText("Editar");
                btnReportesDeC.setText("Reportes");
                btnAgregarDeC.setDisable(false);
                btnEliminarDeC.setDisable(false);
                tipoDeOperador = operador.NINGUNO;
        }
    }

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void desactivarControles() {
        txtCodDetC.setEditable(false);
        txtCostoU.setEditable(false);
        txtCantidad.setEditable(false);
        cmbCodPro.setDisable(true);
        cmbNumDoc.setDisable(true);
    }

    public void activarControles() {
        txtCodDetC.setEditable(true);
        txtCostoU.setEditable(true);
        txtCantidad.setEditable(true);
        cmbCodPro.setDisable(false);
        cmbNumDoc.setDisable(false);
    }

    public void limpiarControles() {
        txtCodDetC.clear();
        txtCostoU.clear();
        txtCantidad.clear();
        cmbCodPro.getSelectionModel().clearSelection();
        cmbNumDoc.getSelectionModel().clearSelection();
    }

    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresarDetC) {
            escenarioPrincipal.CompraView();

        }
    }
}
