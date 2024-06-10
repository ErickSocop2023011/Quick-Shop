/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.ericksocop.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import org.ericksocop.bean.Clientes;
import org.ericksocop.bean.Empleados;
import org.ericksocop.bean.Facturas;
import org.ericksocop.dao.Conexion;
import org.ericksocop.system.Main;

/**
 * FXML Controller class
 *
 * @author mauco
 */
public class FacturasController implements Initializable {

    @FXML
    private TableView tvFacturas;
    @FXML
    private TableColumn colNumF;
    @FXML
    private TableColumn colEstadoF;
    @FXML
    private TableColumn colTotalF;
    @FXML
    private TableColumn colFechaF;
    @FXML
    private TableColumn colClienteID;
    @FXML
    private TableColumn colCodEmp;
    @FXML
    private JFXButton btnAgregarF;
    @FXML
    private JFXButton btnEditarF;
    @FXML
    private JFXButton btnEliminarF;
    @FXML
    private JFXButton btnReportesF;
    @FXML
    private JFXTextField txtNumF;
    @FXML
    private JFXTextField txtEstadoF;
    @FXML
    private JFXTextField txtTotalF;
    @FXML
    private ComboBox cmbClienteID;
    @FXML
    private ComboBox cmbCodEmp;
    @FXML
    private JFXDatePicker jfxDatePicker;
    @FXML
    private Button btnRegresarF;
    @FXML
    private JFXButton btnDetalleFact;
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
    private JFXTextField txtbuscarFactura;
    @FXML
    private JFXButton btnMaximizar;
    @FXML
    private FontAwesomeIcon iconMaximizar;

    private Main escenarioPrincipal;
    private ObservableList<Facturas> listaFacturas;
    private ObservableList<Clientes> listaClientes;
    private ObservableList<Empleados> listaEmpleados;

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
        cmbClienteID.setItems(getClientes());
        cmbCodEmp.setItems(getEmpleados());
        desactivarControles();
        txtTotalF.setDisable(true);
        colClienteID.prefWidthProperty().bind(tvFacturas.widthProperty().multiply(0.19048));
        colCodEmp.prefWidthProperty().bind(tvFacturas.widthProperty().multiply(0.19048));
        colEstadoF.prefWidthProperty().bind(tvFacturas.widthProperty().multiply(0.19048));
        colFechaF.prefWidthProperty().bind(tvFacturas.widthProperty().multiply(0.19048));
        colNumF.prefWidthProperty().bind(tvFacturas.widthProperty().multiply(0.0476));
        colTotalF.prefWidthProperty().bind(tvFacturas.widthProperty().multiply(0.19048));
    }

    public void cargarDatos() {
        tvFacturas.setItems(getFacturas());
        colNumF.setCellValueFactory(new PropertyValueFactory<Facturas, Integer>("numeroDeFactura"));
        colEstadoF.setCellValueFactory(new PropertyValueFactory<Facturas, String>("estado"));
        colTotalF.setCellValueFactory(new PropertyValueFactory<Facturas, Double>("totalFactura"));
        colFechaF.setCellValueFactory(new PropertyValueFactory<Facturas, String>("fechaFactura"));
        colClienteID.setCellValueFactory(new PropertyValueFactory<Facturas, Integer>("clienteID"));
        colCodEmp.setCellValueFactory(new PropertyValueFactory<Facturas, Integer>("codigoEmpleado"));
    }

    public void seleccionarElemento() {
        try {
            txtNumF.setText(String.valueOf(((Facturas) tvFacturas.getSelectionModel().getSelectedItem()).getNumeroDeFactura()));
            txtEstadoF.setText(((Facturas) tvFacturas.getSelectionModel().getSelectedItem()).getEstado());
            txtTotalF.setText(String.valueOf(((Facturas) tvFacturas.getSelectionModel().getSelectedItem()).getTotalFactura()));
            Facturas facturaSeleccionada = (Facturas) tvFacturas.getSelectionModel().getSelectedItem();
            if (facturaSeleccionada != null) {
                String fechaFactura = facturaSeleccionada.getFechaFactura();
                LocalDate localDate = LocalDate.parse(fechaFactura);

                jfxDatePicker.setValue(localDate);
            }

            cmbClienteID.getSelectionModel().select(buscarCliente((((Facturas) tvFacturas.getSelectionModel().getSelectedItem()).getClienteID())));
            cmbCodEmp.getSelectionModel().select(buscarEmpleado((((Facturas) tvFacturas.getSelectionModel().getSelectedItem()).getCodigoEmpleado())));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Por favor selecciona una fila válida", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Clientes buscarCliente(int ClienteID) {
        Clientes resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_buscarClientes(?)}");
            procedimiento.setInt(1, ClienteID);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Clientes(registro.getInt("clienteID"),
                        registro.getString("nombreClientes"),
                        registro.getString("apellidosClientes"),
                        registro.getString("direccionClientes"),
                        registro.getString("NIT"),
                        registro.getString("telefonoClientes"),
                        registro.getString("correoClientes"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public Empleados buscarEmpleado(int codigoEmpleado) {
        Empleados resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_buscarEmpleado(?)}");
            procedimiento.setInt(1, codigoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Empleados(registro.getInt("codigoEmpleado"),
                        registro.getString("nombresEmpleado"),
                        registro.getString("apellidosEmpleado"),
                        registro.getDouble("sueldo"),
                        registro.getString("direccion"),
                        registro.getNString("turno"),
                        registro.getInt("codigoCargoEmpleado"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
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

    public ObservableList<Clientes> getClientes() {
        ArrayList<Clientes> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_MostrarClientes()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Clientes(resultado.getInt("clienteID"),
                        resultado.getString("nombreClientes"),
                        resultado.getString("apellidosClientes"),
                        resultado.getString("direccionClientes"),
                        resultado.getString("NIT"),
                        resultado.getString("telefonoClientes"),
                        resultado.getString("correoClientes")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaClientes = FXCollections.observableList(lista);
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

    public void Agregar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                limpiarControles();
                activarControles();
                agregarIcono.setFill(Color.WHITE);
                eliminarIcono.setFill(Color.WHITE);
                btnAgregarF.setText("Guardar");
                eliminarIcono.setIcon(FontAwesomeIcons.CLOSE);
                agregarIcono.setIcon(FontAwesomeIcons.SAVE);
                btnEliminarF.setText("Cancelar");
                btnEditarF.setDisable(true);
                btnReportesF.setDisable(true);
                //imgAgregar.setImage(new Image("URL"));
                tipoDeOperador = operador.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                limpiarControles();
                cargarDatos();
                desactivarControles();
                agregarIcono.setFill(Color.WHITE);
                agregarIcono.setIcon(FontAwesomeIcons.USER_PLUS);
                btnAgregarF.setText("Agregar");
                eliminarIcono.setFill(Color.WHITE);
                eliminarIcono.setIcon(FontAwesomeIcons.USER_TIMES);
                btnEliminarF.setText("Eliminar");
                btnEditarF.setDisable(false);
                btnReportesF.setDisable(false);
                /*regresar de nuevo a sus imagenes originales
                imgAgregar.setImage(new Image("URL"));*/
                tipoDeOperador = operador.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void guardar() {
        Facturas registro = new Facturas();
        try {
            registro.setNumeroDeFactura(Integer.parseInt(txtNumF.getText()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID de Producto no puede ser nulo/vacío", "Error", JOptionPane.ERROR_MESSAGE);
        }
        registro.setEstado(txtEstadoF.getText());
        registro.setTotalFactura(Double.parseDouble("0.00"));
        if (jfxDatePicker.getValue() != null) {
            Date fechaFactura = Date.valueOf(jfxDatePicker.getValue());
            registro.setFechaFactura(fechaFactura.toString());
        } else {
            registro.setFechaFactura(null);
        }
        registro.setClienteID(((Clientes) cmbClienteID.getSelectionModel().getSelectedItem()).getClienteID());
        registro.setCodigoEmpleado(((Empleados) cmbCodEmp.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_crearFactura(?,?,?,?,?,?) }");
            procedimiento.setInt(1, registro.getNumeroDeFactura());
            procedimiento.setString(2, registro.getEstado());
            procedimiento.setDouble(3, registro.getTotalFactura());
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = date.parse(registro.getFechaFactura());
            Date fechaFactura = new Date(parsedDate.getTime());
            procedimiento.setDate(4, fechaFactura);
            procedimiento.setInt(5, registro.getClienteID());
            procedimiento.setInt(6, registro.getCodigoEmpleado());
            procedimiento.execute();

            listaFacturas.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                if (tvFacturas.getSelectionModel().getSelectedItem() != null) {
                    actualizarIcono.setFill(Color.WHITE);
                    actualizarIcono.setIcon(FontAwesomeIcons.SAVE);
                    btnEditarF.setText("Guardar");
                    reporteIcono.setFill(Color.WHITE);
                    reporteIcono.setIcon(FontAwesomeIcons.CLOSE);
                    btnReportesF.setText("Cancelar");
                    btnEliminarF.setDisable(true);
                    btnAgregarF.setDisable(true);
                    activarControles();
                    txtNumF.setEditable(false);
                    tipoDeOperador = operador.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar una fila para editar");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                actualizarIcono.setFill(Color.WHITE);
                actualizarIcono.setIcon(FontAwesomeIcons.EDIT);
                btnEditarF.setText("Actualizar");
                reporteIcono.setFill(Color.WHITE);
                reporteIcono.setIcon(FontAwesomeIcons.FOLDER);
                btnReportesF.setText("Reportes");
                btnAgregarF.setDisable(false);
                btnEliminarF.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperador = operador.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_editarFactura(?,?,?,?,?,?)}");
            Facturas registro = (Facturas) tvFacturas.getSelectionModel().getSelectedItem();
            registro.setNumeroDeFactura(Integer.parseInt(txtNumF.getText()));
            registro.setEstado(txtEstadoF.getText());
            registro.setTotalFactura(Double.parseDouble(txtTotalF.getText()));
            if (jfxDatePicker.getValue() != null) {
                Date fechaFactura = Date.valueOf(jfxDatePicker.getValue());
                registro.setFechaFactura(fechaFactura.toString());
            } else {
                registro.setFechaFactura(null);
            }
            registro.setClienteID(((Clientes) cmbClienteID.getSelectionModel().getSelectedItem()).getClienteID());
            registro.setCodigoEmpleado(((Empleados) cmbCodEmp.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
            procedimiento.setInt(1, registro.getNumeroDeFactura());
            procedimiento.setString(2, registro.getEstado());
            procedimiento.setDouble(3, registro.getTotalFactura());
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = date.parse(registro.getFechaFactura());
            Date fechaFactura = new Date(parsedDate.getTime());
            procedimiento.setDate(4, fechaFactura);
            procedimiento.setInt(5, registro.getClienteID());
            procedimiento.setInt(6, registro.getCodigoEmpleado());
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
                agregarIcono.setFill(Color.WHITE);
                agregarIcono.setIcon(FontAwesomeIcons.USER_PLUS);
                btnAgregarF.setText("Agregar");
                eliminarIcono.setFill(Color.WHITE);
                eliminarIcono.setIcon(FontAwesomeIcons.USER_TIMES);
                btnEliminarF.setText("Eliminar");
                btnEditarF.setDisable(false);
                btnReportesF.setDisable(false);
                tipoDeOperador = operador.NINGUNO;
                break;
            default:
                if (tvFacturas.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Factura", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            Facturas facturaSeleccionada = (Facturas) tvFacturas.getSelectionModel().getSelectedItem();
                            int numeroDeFactura = facturaSeleccionada.getNumeroDeFactura();

                            // Primero, eliminar los detalles de la factura
                            PreparedStatement eliminarDetalleFactura = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarDetalleFacturaPorFactura(?)}");
                            eliminarDetalleFactura.setInt(1, numeroDeFactura);
                            eliminarDetalleFactura.execute();

                            // Luego, eliminar la factura
                            PreparedStatement eliminarFactura = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarFactura(?)}");
                            eliminarFactura.setInt(1, numeroDeFactura);
                            eliminarFactura.execute();

                            listaFacturas.remove(facturaSeleccionada);
                            limpiarControles();
                            cargarDatos();
                            JOptionPane.showMessageDialog(null, "Factura eliminada correctamente");
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "Error al intentar eliminar la factura: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar una factura para eliminar");
                }
                break;
        }
    }

    public void reportes() {
        switch (tipoDeOperador) {
            case NINGUNO:
                try {
                generarPDF();
            } catch (Exception e) {
                e.printStackTrace();
            }
            tipoDeOperador = operador.ACTUALIZAR;
            break;
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                actualizarIcono.setFill(Color.WHITE);
                actualizarIcono.setIcon(FontAwesomeIcons.EDIT);
                btnEditarF.setText("Actualizar");
                reporteIcono.setFill(Color.WHITE);
                reporteIcono.setIcon(FontAwesomeIcons.FOLDER);
                btnReportesF.setText("Reportes");
                btnAgregarF.setDisable(false);
                btnEliminarF.setDisable(false);
                tipoDeOperador = operador.NINGUNO;
                break;
        }
    }

    public void generarPDF() {
        try {
            Facturas factura = (Facturas) tvFacturas.getSelectionModel().getSelectedItem();
            if (factura != null) {
                FacturaReportePDFController pdf = new FacturaReportePDFController();
                pdf.FacturaPDF(factura, "src/org/ericksocop/resource/firmaE.PNG", "src/org/ericksocop/resource/membrete.PNG", "src/org/ericksocop/report/");
            } else {
                JOptionPane.showMessageDialog(null, "Debe seleccionar una fila para realizar la factura", "ERROR", ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buscarFactura() {
        limpiarControles();
        FilteredList<Facturas> filtro = new FilteredList<>(listaFacturas, e -> true);
        txtbuscarFactura.textProperty().addListener((Observable, oldValue, newValue) -> {
            filtro.setPredicate(predicateFactura -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String param = newValue.toLowerCase();
                String numeroFactura = String.valueOf(predicateFactura.getNumeroDeFactura());
                String totalFactura = String.valueOf(predicateFactura.getTotalFactura());
                String fechaFactura = predicateFactura.getFechaFactura().toString();
                String clienteID = String.valueOf(predicateFactura.getClienteID());
                String empleadoID = String.valueOf(predicateFactura.getCodigoEmpleado());

                if (numeroFactura.contains(param)) {
                    return true;
                } else if (predicateFactura.getEstado().toLowerCase().contains(param)) {
                    return true;
                } else if (totalFactura.contains(param)) {
                    return true;
                } else if (fechaFactura.contains(param)) {
                    return true;
                } else if (clienteID.contains(param)) {
                    return true;
                } else if (empleadoID.contains(param)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Facturas> sortList = new SortedList<>(filtro);
        sortList.comparatorProperty().bind(tvFacturas.comparatorProperty());
        tvFacturas.setItems(sortList);
    }

    public void limpiarControles() {
        txtEstadoF.clear();
        txtNumF.clear();
        txtTotalF.clear();
        cmbClienteID.setValue(null);
        cmbCodEmp.setValue(null);
        jfxDatePicker.setValue(null);
    }

    public void activarControles() {
        txtEstadoF.setEditable(true);
        txtNumF.setEditable(true);
        txtTotalF.setEditable(true);
        cmbClienteID.setDisable(false);
        cmbCodEmp.setDisable(false);
        jfxDatePicker.setDisable(false);
    }

    public void desactivarControles() {
        txtEstadoF.setEditable(false);
        txtNumF.setEditable(false);
        txtTotalF.setEditable(false);
        cmbClienteID.setDisable(true);
        cmbCodEmp.setDisable(true);
        jfxDatePicker.setDisable(true);
    }

    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresarF) {
            escenarioPrincipal.menuPrincipalView();
        }
        if (event.getSource() == btnDetalleFact) {
            try {
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                escenarioPrincipal.DetalleFacturaView(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (event.getSource() == iconoCerrar || event.getSource() == btnCerrar) {
            System.exit(0);
        }
        if (event.getSource() == iconMinimizar || event.getSource() == btnMinimizar) {
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            stage.setIconified(true);
        }
    }
    
    public void actualizarIconoMaximizar(boolean isMaximized) {
        if (isMaximized) {
            iconMaximizar.setRotate(180);
        } else {
            iconMaximizar.setRotate(0);
        }
    }

    public void ventana(ActionEvent event) {
        colClienteID.prefWidthProperty().bind(tvFacturas.widthProperty().multiply(0.19048));
        colCodEmp.prefWidthProperty().bind(tvFacturas.widthProperty().multiply(0.19048));
        colEstadoF.prefWidthProperty().bind(tvFacturas.widthProperty().multiply(0.19048));
        colFechaF.prefWidthProperty().bind(tvFacturas.widthProperty().multiply(0.19048));
        colNumF.prefWidthProperty().bind(tvFacturas.widthProperty().multiply(0.0476));
        colTotalF.prefWidthProperty().bind(tvFacturas.widthProperty().multiply(0.19048));
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

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

}
