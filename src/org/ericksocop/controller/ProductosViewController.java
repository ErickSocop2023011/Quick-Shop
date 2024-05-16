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
import org.ericksocop.bean.DetalleCompra;
import org.ericksocop.bean.Productos;
import org.ericksocop.bean.Proveedores;
import org.ericksocop.bean.TipoProducto;
import org.ericksocop.dao.Conexion;
import org.ericksocop.system.Main;

/**
 * FXML Controller class
 *
 * @author mauco
 */
public class ProductosViewController implements Initializable {

    @FXML
    private TableView tvProductos;
    @FXML
    private Button btnRegresarP;
    @FXML
    private Button btnAgregarPro;
    @FXML
    private Button btnEditarPro;
    @FXML
    private Button btnTipoP;
    @FXML
    private Button btnEliminarPro;
    @FXML
    private Button btnReportesPro;
    @FXML
    private TextField txtCodigoProd;
    @FXML
    private TextField txtDescPro;
    @FXML
    private TextField txtPrecioU;
    @FXML
    private TextField txtPrecioD;
    @FXML
    private TextField txtPrecioM;
    @FXML
    private TextField txtImagenPro;
    @FXML
    private TextField txtExistencia;
    @FXML
    private ComboBox cmbCodigoTipoP;
    @FXML
    private ComboBox cmbCodigoP;
    @FXML
    private TableColumn colCodProd;
    @FXML
    private TableColumn colDescProd;
    @FXML
    private TableColumn colPrecioU;
    @FXML
    private TableColumn colPrecioD;
    @FXML
    private TableColumn colPrecioM;
    @FXML
    private TableColumn colImagenP;
    @FXML
    private TableColumn colExistencia;
    @FXML
    private TableColumn colCodTipoProd;
    @FXML
    private TableColumn colCodProv;

    private Main escenarioPrincipal;
    private ObservableList<Productos> listaProductos;
    private ObservableList<Proveedores> listaProveedores;
    private ObservableList<TipoProducto> listaTipoP;

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
        cmbCodigoTipoP.setItems(getTipoP());
        cmbCodigoP.setItems(getProveedores());
        desactivarControles();
    }

    public void cargarDatos() {
        tvProductos.setItems(getProducto());
        colCodProd.setCellValueFactory(new PropertyValueFactory<Productos, String>("codigoProducto"));
        colDescProd.setCellValueFactory(new PropertyValueFactory<Productos, String>("descripcionProducto"));
        colPrecioU.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioUnitario"));
        colPrecioD.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioDocena"));
        colPrecioM.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioMayor"));
        colImagenP.setCellValueFactory(new PropertyValueFactory<Productos, String>("imagenProducto"));
        colExistencia.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("existencia"));
        colCodTipoProd.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("CodigoTipoProducto"));
        colCodProv.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("codigoProveedor"));
    }

    public void seleccionarElemento() {
        txtCodigoProd.setText((((Productos) tvProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        txtDescPro.setText((((Productos) tvProductos.getSelectionModel().getSelectedItem()).getDescripcionProducto()));
        txtPrecioU.setText(String.valueOf(((Productos) tvProductos.getSelectionModel().getSelectedItem()).getPrecioUnitario()));
        txtPrecioD.setText(String.valueOf(((Productos) tvProductos.getSelectionModel().getSelectedItem()).getPrecioDocena()));
        txtPrecioM.setText(String.valueOf(((Productos) tvProductos.getSelectionModel().getSelectedItem()).getPrecioMayor()));
        txtImagenPro.setText((((Productos) tvProductos.getSelectionModel().getSelectedItem()).getImagenProducto()));
        txtExistencia.setText(String.valueOf(((Productos) tvProductos.getSelectionModel().getSelectedItem()).getExistencia()));
        cmbCodigoTipoP.getSelectionModel().select(buscarTipoProducto(((Productos) tvProductos.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
        cmbCodigoP.getSelectionModel().select(buscarProveedor(((Productos) tvProductos.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
        //cmbCodgioTipoP.set(String.valueOf(((Productos)tvProductos.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
        //  cmbCodigoP.setText(String.valueOf(((Productos)tvProductos.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
    }

    public TipoProducto buscarTipoProducto(int codigoTipoProducto) {
        TipoProducto resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_buscarTipoProducto(?)}");
            procedimiento.setInt(1, codigoTipoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new TipoProducto(registro.getInt("CodigoTipoProducto"),
                        registro.getString("descripcionProducto")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }

    public Proveedores buscarProveedor(int codigoProveedor) {
        Proveedores resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_buscarproveedor(?)}");
            procedimiento.setInt(1, codigoProveedor);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Proveedores(registro.getInt("codigoProveedor"),
                        registro.getString("NITProveedor"),
                        registro.getString("nombresProveedor"),
                        registro.getString("apellidosProveedor"),
                        registro.getString("direccionProveedor"),
                        registro.getString("razonSocial"),
                        registro.getString("contactoPrincipal"),
                        registro.getString("paginaWeb"),
                        registro.getString("telefonoProveedor"),
                        registro.getString("emailProveedor")
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

    public ObservableList<Proveedores> getProveedores() {
        ArrayList<Proveedores> listaPro = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_mostrarproveedor()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaPro.add(new Proveedores(resultado.getInt("codigoProveedor"),
                        resultado.getString("NITProveedor"),
                        resultado.getString("nombresProveedor"),
                        resultado.getString("apellidosProveedor"),
                        resultado.getString("direccionProveedor"),
                        resultado.getString("razonSocial"),
                        resultado.getString("contactoPrincipal"),
                        resultado.getString("paginaWeb"),
                        resultado.getString("telefonoProveedor"),
                        resultado.getString("emailProveedor")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProveedores = FXCollections.observableList(listaPro);
    }

    public ObservableList<TipoProducto> getTipoP() {
        ArrayList<TipoProducto> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_mostrarTipoProducto()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoProducto(resultado.getInt("CodigoTipoProducto"),
                        resultado.getString("descripcionProducto")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaTipoP = FXCollections.observableList(lista);
    }

    public void Agregar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnAgregarPro.setText("Guardar");
                btnEliminarPro.setText("Cancelar");
                btnEditarPro.setDisable(true);
                btnReportesPro.setDisable(true);
                //imgAgregar.setImage(new Image("URL"));
                tipoDeOperador = operador.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                limpiarControles();
                cargarDatos();
                desactivarControles();
                btnAgregarPro.setText("Agregar");
                btnEliminarPro.setText("Eliminar");
                btnEditarPro.setDisable(false);
                btnReportesPro.setDisable(false);
                /*regresar de nuevo a sus imagenes originales
                imgAgregar.setImage(new Image("URL"));*/
                tipoDeOperador = operador.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void guardar() {
        Productos registro = new Productos();
        registro.setCodigoProducto(txtCodigoProd.getText());
        registro.setCodigoProveedor(((Proveedores) cmbCodigoP.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        registro.setCodigoTipoProducto((((TipoProducto) cmbCodigoTipoP.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
        registro.setDescripcionProducto(txtDescPro.getText());
        registro.setPrecioDocena(Double.parseDouble(txtPrecioD.getText()));
        registro.setPrecioMayor(Double.parseDouble(txtPrecioM.getText()));
        registro.setExistencia(Integer.parseInt(txtExistencia.getText()));
        registro.setImagenProducto(txtImagenPro.getText());
        registro.setPrecioUnitario(Double.parseDouble(txtPrecioU.getText()));

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_agregarProducto(?,?,?,?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getDescripcionProducto());
            procedimiento.setDouble(3, registro.getPrecioUnitario());
            procedimiento.setDouble(4, registro.getPrecioDocena());
            procedimiento.setDouble(5, registro.getPrecioMayor());
            procedimiento.setString(6, registro.getImagenProducto());
            procedimiento.setInt(7, registro.getExistencia());
            procedimiento.setInt(8, registro.getCodigoTipoProducto());
            procedimiento.setInt(9, registro.getCodigoProveedor());
            procedimiento.execute();

            listaProductos.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                if (tvProductos.getSelectionModel().getSelectedItem() != null) {
                    btnEditarPro.setText("Actualizar");
                    btnReportesPro.setText("Cancelar");
                    btnEliminarPro.setDisable(true);
                    btnAgregarPro.setDisable(true);
                    activarControles();
                    txtCodigoProd.setEditable(false);
                    tipoDeOperador = operador.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un Detalle Compra para editar");
                }
                break;
            case ACTUALIZAR:
                try {
                actualizar();
            } catch (Exception e) {
                e.printStackTrace();
            }
            btnEditarPro.setText("Editar");
            btnReportesPro.setText("Reportes");
            btnAgregarPro.setDisable(false);
            btnEliminarPro.setDisable(false);
            desactivarControles();
            limpiarControles();
            tipoDeOperador = operador.NINGUNO;
            cargarDatos();
            break;
        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_editarProducto(?,?,?,?,?,?,?,?,?)}");
            Productos registro = (Productos) tvProductos.getSelectionModel().getSelectedItem();
            registro.setCodigoProveedor(((Proveedores) cmbCodigoP.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            registro.setCodigoTipoProducto((((TipoProducto) cmbCodigoTipoP.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
            registro.setDescripcionProducto(txtDescPro.getText());
            registro.setPrecioDocena(Double.parseDouble(txtPrecioD.getText()));
            registro.setPrecioMayor(Double.parseDouble(txtPrecioM.getText()));
            registro.setExistencia(Integer.parseInt(txtExistencia.getText()));
            registro.setImagenProducto(txtImagenPro.getText());
            registro.setPrecioUnitario(Double.parseDouble(txtPrecioU.getText()));
            procedimiento.setString(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getDescripcionProducto());
            procedimiento.setDouble(3, registro.getPrecioUnitario());
            procedimiento.setDouble(4, registro.getPrecioDocena());
            procedimiento.setDouble(5, registro.getPrecioMayor());
            procedimiento.setString(6, registro.getImagenProducto());
            procedimiento.setInt(7, registro.getExistencia());
            procedimiento.setInt(8, registro.getCodigoTipoProducto());
            procedimiento.setInt(9, registro.getCodigoProveedor());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* public void eliminar() {
        switch (tipoDeOperador) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregarPro.setText("Agregar");
                btnEliminarPro.setText("Eliminar");
                btnEditarPro.setDisable(false);
                btnReportesPro.setDisable(false);
                /*regresar de nuevo a sus imagenes originales
                imgAgregar.setImage(new Image("URL"));
                tipoDeOperador = operador.NINGUNO;
                break;
            default:
                if (tvProductos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmas la eliminacion del registro?", "Eliminar Producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarProducto(?)}");
                            procedimiento.setString(1, ((Productos) tvProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                            listaProductos.remove(tvProductos.getSelectionModel().getSelectedItem());
                            
                            
                           /* PreparedStatement procedimiento1 = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarproveedor(?)}");
                            procedimiento.setInt(1, ((Proveedores) tvProductos.getSelectionModel().getSelectedItem()).getCodigoProveedor());
                            PreparedStatement procedimiento2 = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarTipoProducto(?)}");
                            procedimiento.setInt(1, ((TipoProducto) tvProductos.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
                            
                            procedimiento.execute();
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un Producto para eliminar");
                }

                break;
        }
    }*/
    public void eliminar() throws SQLException {
        switch (tipoDeOperador) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregarPro.setText("Agregar");
                btnEliminarPro.setText("Eliminar");
                btnEditarPro.setDisable(false);
                btnReportesPro.setDisable(false);
                tipoDeOperador = operador.NINGUNO;
                break;
            default:
                if (tvProductos.getSelectionModel().getSelectedItem() != null) {
                    int ans = JOptionPane.showConfirmDialog(null, "Confirma esta Accion", "Verificacion",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (ans == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion()
                                    .prepareCall("{call sp_eliminarProducto(?)}");
                            procedimiento.setInt(1,
                                    ((Productos) tvProductos.getSelectionModel().getSelectedItem()).getCodigoProveedor());
                            procedimiento.execute();
                            listaProductos.remove(tvProductos.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar Cliente para eliminar");
                }
        }
    }

    public void eliminarDetalleCompra() throws SQLException {
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarDetalleCompra(?)}");
        procedimiento.setInt(1, ((DetalleCompra) tvProductos.getSelectionModel().getSelectedItem()).getCodigoDetalleCompra());
        procedimiento.execute();
        listaProductos.remove(tvProductos.getSelectionModel().getSelectedItem());
        limpiarControles();
    }

    public void desactivarControles() {
        txtCodigoProd.setEditable(false);
        txtDescPro.setEditable(false);
        txtExistencia.setEditable(false);
        txtImagenPro.setEditable(false);
        txtPrecioD.setEditable(false);
        txtPrecioM.setEditable(false);
        txtPrecioU.setEditable(false);
        cmbCodigoTipoP.setDisable(true);
        cmbCodigoP.setDisable(true);
    }

    public void activarControles() {
        txtCodigoProd.setEditable(true);
        txtDescPro.setEditable(true);
        txtExistencia.setEditable(true);
        txtImagenPro.setEditable(true);
        txtPrecioD.setEditable(true);
        txtPrecioM.setEditable(true);
        txtPrecioU.setEditable(true);
        cmbCodigoTipoP.setDisable(false);
        cmbCodigoP.setDisable(false);
    }

    public void limpiarControles() {
        txtCodigoProd.clear();
        txtDescPro.clear();
        txtExistencia.clear();
        txtImagenPro.clear();
        txtPrecioD.clear();
        txtPrecioM.clear();
        txtPrecioU.clear();
        tvProductos.getSelectionModel().getSelectedItem();
        cmbCodigoTipoP.getSelectionModel().getSelectedItem();
        cmbCodigoP.getSelectionModel().getSelectedItem();

    }

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void handleButtonAction(ActionEvent event) {

        if (event.getSource() == btnRegresarP) {
            escenarioPrincipal.menuPrincipalView();
        }
        if (event.getSource() == btnTipoP) {
            escenarioPrincipal.tipoProductoView();
        }
    }

}
