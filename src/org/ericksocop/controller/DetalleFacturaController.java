/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.ericksocop.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.RotateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
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
public class DetalleFacturaController implements Initializable {
    
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
    private JFXButton btnAgregarDeF;
    @FXML
    private JFXButton btnEditarDeF;
    @FXML
    private JFXButton btnEliminarDeF;
    @FXML
    private JFXButton btnReportesDeF;
    @FXML
    private JFXTextField txtCodDetF;
    @FXML
    private JFXTextField txtPrecioU;
    @FXML
    private JFXTextField txtCantidad;
    @FXML
    private ComboBox cmbCodP;
    @FXML
    private ComboBox cmbNumF;
    @FXML
    private JFXButton btnMaximizar;
    @FXML
    private FontAwesomeIcon iconMaximizar;
    @FXML
    private JFXButton btnRegresarDetF;
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
    private FontAwesomeIcon agregarIcono;
    @FXML
    private FontAwesomeIcon actualizarIcono;
    @FXML
    private FontAwesomeIcon eliminarIcono;
    @FXML
    private FontAwesomeIcon reporteIcono;
    @FXML
    private JFXTextField txtbuscarDetalleFact;
    
    private enum operador {
        AGREGRAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    
    private operador tipoDeOperador = operador.NINGUNO;
    private Main escenarioPrincipal;
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
        txtPrecioU.setDisable(true);
        desactivarControles();
        colCodDetalleF.prefWidthProperty().bind(tvDetalleF.widthProperty().multiply(0.0476));
        colCant.prefWidthProperty().bind(tvDetalleF.widthProperty().multiply(0.23810));
        colCodP.prefWidthProperty().bind(tvDetalleF.widthProperty().multiply(0.23810));
        colNumF.prefWidthProperty().bind(tvDetalleF.widthProperty().multiply(0.23810));
        colPrecioU.prefWidthProperty().bind(tvDetalleF.widthProperty().multiply(0.23810));
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
                        resultado.getInt("codigoProducto")
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
                listaP.add(new Productos(resultado.getInt("codigoProducto"),
                        resultado.getString("descripcionProducto"),
                        resultado.getDouble("precioUnitario"),
                        resultado.getDouble("precioDocena"),
                        resultado.getDouble("precioMayor"),
                        resultado.getBlob("imagenProducto"),
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
    
    public Productos buscarProducto(int productoID) {
        Productos resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_buscarProducto(?)}");
            procedimiento.setInt(1, productoID);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Productos(
                        registro.getInt("codigoProducto"),
                        registro.getString("descripcionProducto"),
                        registro.getDouble("precioUnitario"),
                        registro.getDouble("precioDocena"),
                        registro.getDouble("precioMayor"),
                        registro.getBlob("imagenProducto"),
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
                tvDetalleF.setDisable(true);
                limpiarControles();
                activarControles();
                agregarIcono.setFill(Color.WHITE);
                agregarIcono.setIcon(FontAwesomeIcons.SAVE);
                btnAgregarDeF.setText("Guardar");
                eliminarIcono.setFill(Color.WHITE);
                eliminarIcono.setIcon(FontAwesomeIcons.CLOSE);
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
                agregarIcono.setFill(Color.WHITE);
                agregarIcono.setIcon(FontAwesomeIcons.PLUS);
                btnAgregarDeF.setText("Agregar");
                eliminarIcono.setFill(Color.WHITE);
                eliminarIcono.setIcon(FontAwesomeIcons.TRASH);
                btnEliminarDeF.setText("Eliminar");
                btnEditarDeF.setDisable(false);
                btnReportesDeF.setDisable(false);
                /*regresar de nuevo a sus imagenes originales
                imgAgregar.setImage(new Image("URL"));*/
                tipoDeOperador = operador.NINGUNO;
                tvDetalleF.setDisable(false);
                break;
        }
    }
    
    public void guardar() {
        DetalleFactura registro = new DetalleFactura();
        try {
            int detalleFacturaID = Integer.parseInt(txtCodDetF.getText());
            if (existeCodigoDetFact(detalleFacturaID)) {
                JOptionPane.showMessageDialog(null, "El ID del Detalle Factura ya existe. Por favor, ingrese uno nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            registro.setCodigoDetalleFactura(detalleFacturaID);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID de Detalle Factura no puede ser nulo/vacío", "Error", JOptionPane.ERROR_MESSAGE);
        }
        registro.setPrecioUnitario(Double.parseDouble("0.00"));
        //registro.setPrecioUnitario(Double.parseDouble(txtPrecioU.getText()));
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        registro.setNumeroDeFactura(((Facturas) cmbNumF.getSelectionModel().getSelectedItem()).getNumeroDeFactura());
        registro.setCodigoProducto(((Productos) cmbCodP.getSelectionModel().getSelectedItem()).getCodigoProducto());
        
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_crearDetalleFactura(?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoDetalleFactura());
            procedimiento.setDouble(2, registro.getPrecioUnitario());
            procedimiento.setInt(3, registro.getCantidad());
            procedimiento.setInt(4, registro.getNumeroDeFactura());
            procedimiento.setInt(5, registro.getCodigoProducto());
            procedimiento.execute();
            
            listaDetalleFactura.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private boolean existeCodigoDetFact(int codigoID) {
        for (DetalleFactura detalleFac : listaDetalleFactura) {
            if (detalleFac.getCodigoDetalleFactura() == codigoID) {
                return true;
            }
        }
        return false;
    }
    
    public void editar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                tvDetalleF.setDisable(true);
                if (tvDetalleF.getSelectionModel().getSelectedItem() != null) {
                    actualizarIcono.setFill(Color.WHITE);
                    actualizarIcono.setIcon(FontAwesomeIcons.SAVE);
                    btnEditarDeF.setText("Guardar");
                    reporteIcono.setFill(Color.WHITE);
                    reporteIcono.setIcon(FontAwesomeIcons.CLOSE);
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
                actualizarIcono.setFill(Color.WHITE);
                actualizarIcono.setIcon(FontAwesomeIcons.EDIT);
                btnEditarDeF.setText("Actualizar");
                reporteIcono.setFill(Color.WHITE);
                reporteIcono.setIcon(FontAwesomeIcons.FOLDER);
                btnReportesDeF.setText("Reportes");
                btnAgregarDeF.setDisable(false);
                btnEliminarDeF.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperador = operador.NINGUNO;
                cargarDatos();
                tvDetalleF.setDisable(false);
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
            procedimiento.setInt(5, registro.getCodigoProducto());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void eliminar() {
        
        switch (tipoDeOperador) {
            case ACTUALIZAR:
                tvDetalleF.setDisable(false);
                desactivarControles();
                limpiarControles();
                agregarIcono.setFill(Color.WHITE);
                agregarIcono.setIcon(FontAwesomeIcons.PLUS);
                btnAgregarDeF.setText("Agregar");
                eliminarIcono.setFill(Color.WHITE);
                eliminarIcono.setIcon(FontAwesomeIcons.TRASH);
                btnEliminarDeF.setText("Eliminar");
                btnEditarDeF.setDisable(false);
                btnReportesDeF.setDisable(false);
                /*regresar de nuevo a sus imagenes originales
                imgAgregar.setImage(new Image("URL"));*/
                tipoDeOperador = operador.NINGUNO;
                break;
            default:
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
                break;
        }
    }
    
    public void reportes() {
        switch (tipoDeOperador) {
            case NINGUNO:
                break;
            case ACTUALIZAR:
                tvDetalleF.setDisable(false);
                desactivarControles();
                limpiarControles();
                actualizarIcono.setFill(Color.WHITE);
                actualizarIcono.setIcon(FontAwesomeIcons.EDIT);
                btnEditarDeF.setText("Actualizar");
                reporteIcono.setFill(Color.WHITE);
                reporteIcono.setIcon(FontAwesomeIcons.FOLDER);
                btnReportesDeF.setText("Reportes");
                btnAgregarDeF.setDisable(false);
                btnEliminarDeF.setDisable(false);
                tipoDeOperador = operador.NINGUNO;
                break;
        }
    }
    
    public void buscarDetFact() {
        limpiarControles();
        FilteredList<DetalleFactura> filtro = new FilteredList<>(listaDetalleFactura, e -> true);
        txtbuscarDetalleFact.textProperty().addListener((Observable, oldValue, newValue) -> {
            filtro.setPredicate(predicateDetalleFactura -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String param = newValue.toLowerCase();
                String codigoDetalleFactura = String.valueOf(predicateDetalleFactura.getCodigoDetalleFactura());
                String precioUnitario = String.valueOf(predicateDetalleFactura.getPrecioUnitario());
                String cantidad = String.valueOf(predicateDetalleFactura.getCantidad());
                String numeroDeFactura = String.valueOf(predicateDetalleFactura.getNumeroDeFactura());
                String codigoProducto = String.valueOf(predicateDetalleFactura.getCodigoProducto());
                
                if (codigoDetalleFactura.contains(param)) {
                    return true;
                } else if (precioUnitario.contains(param)) {
                    return true;
                } else if (cantidad.contains(param)) {
                    return true;
                } else if (numeroDeFactura.contains(param)) {
                    return true;
                } else if (codigoProducto.contains(param)) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<DetalleFactura> sortList = new SortedList<>(filtro);
        sortList.comparatorProperty().bind(tvDetalleF.comparatorProperty());
        tvDetalleF.setItems(sortList);
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
    
    public void actualizarIconoMaximizar(boolean isMaximized) {
        if (isMaximized) {
            iconMaximizar.setRotate(180);
        } else {
            iconMaximizar.setRotate(0);
        }
    }
    
    public void ventana(ActionEvent event) {
        colCodDetalleF.prefWidthProperty().bind(tvDetalleF.widthProperty().multiply(0.0476));
        colCant.prefWidthProperty().bind(tvDetalleF.widthProperty().multiply(0.23810));
        colCodP.prefWidthProperty().bind(tvDetalleF.widthProperty().multiply(0.23810));
        colNumF.prefWidthProperty().bind(tvDetalleF.widthProperty().multiply(0.23810));
        colPrecioU.prefWidthProperty().bind(tvDetalleF.widthProperty().multiply(0.23810));
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
    
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresarDetF) {
            escenarioPrincipal.FacturasView();
        }
        if (event.getSource() == iconoCerrar || event.getSource() == btnCerrar) {
            System.exit(0);
        }
        if (event.getSource() == iconMinimizar || event.getSource() == btnMinimizar) {
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            stage.setIconified(true);
        }
    }
    
}
