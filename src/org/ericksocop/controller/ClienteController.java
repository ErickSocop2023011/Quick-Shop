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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;
import org.ericksocop.bean.Clientes;
import org.ericksocop.dao.Conexion;
import org.ericksocop.report.GenerarReportes;
import org.ericksocop.system.Main;

/**
 * FXML Controller class
 *
 * @author mauco
 */
public class ClienteController implements Initializable {

    private enum operador {
        AGREGRAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operador tipoDeOperador = operador.NINGUNO;

    private ObservableList<Clientes> listaClientes;
    private Main escenarioPrincipal;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private JFXButton btnCerrar;
    @FXML
    private FontAwesomeIcon iconoCerrar;
    @FXML
    private JFXButton btnMaximizar;
    @FXML
    private FontAwesomeIcon iconMaximizar;
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
    private TableView tvCliente;
    @FXML
    private JFXTextField txtbuscarCliente;
    @FXML
    private TableColumn colClienteID;
    @FXML
    private TableColumn colNombreCliente;
    @FXML
    private TableColumn colApellidosClientes;
    @FXML
    private TableColumn colDireccionClientes;
    @FXML
    private TableColumn colNit;
    @FXML
    private TableColumn colTelefonoClientes;
    @FXML
    private TableColumn colCorreoClientes;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReportes;
    @FXML
    private Button btnAgregar;
    @FXML
    private TextField txtClienteID;
    @FXML
    private TextField txtNombreCliente;
    @FXML
    private TextField txtApellidoCliente;
    @FXML
    private TextField txtDireccionCliente;
    @FXML
    private TextField txtNIT;
    @FXML
    private TextField txtTelefonoCli;
    @FXML
    private TextField txtCorreoCliente;
    @FXML
    private Button btnRegresar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarDatos();
        desactivarControles();
        colClienteID.prefWidthProperty().bind(tvCliente.widthProperty().multiply(0.0476));
        colApellidosClientes.prefWidthProperty().bind(tvCliente.widthProperty().multiply(0.15873));
        colCorreoClientes.prefWidthProperty().bind(tvCliente.widthProperty().multiply(0.15873));
        colDireccionClientes.prefWidthProperty().bind(tvCliente.widthProperty().multiply(0.15873));
        colNit.prefWidthProperty().bind(tvCliente.widthProperty().multiply(0.15873));
        colNombreCliente.prefWidthProperty().bind(tvCliente.widthProperty().multiply(0.15873));
        colTelefonoClientes.prefWidthProperty().bind(tvCliente.widthProperty().multiply(0.15873));
    }

    public void cargarDatos() {
        tvCliente.setItems(getClientes());
        colClienteID.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("clienteID"));
        colNombreCliente.setCellValueFactory(new PropertyValueFactory<Clientes, String>("nombreClientes"));
        colApellidosClientes.setCellValueFactory(new PropertyValueFactory<Clientes, String>("apellidoClientes"));
        colDireccionClientes.setCellValueFactory(new PropertyValueFactory<Clientes, String>("direccionClientes"));
        colNit.setCellValueFactory(new PropertyValueFactory<Clientes, String>("NITClientes"));
        colTelefonoClientes.setCellValueFactory(new PropertyValueFactory<Clientes, String>("telefonoClientes"));
        colCorreoClientes.setCellValueFactory(new PropertyValueFactory<Clientes, String>("correoClientes"));

    }

    @FXML
    public void seleccionarElemento() {
        try {
            txtClienteID.setText(String.valueOf(((Clientes) tvCliente.getSelectionModel().getSelectedItem()).getClienteID()));
            txtNombreCliente.setText((((Clientes) tvCliente.getSelectionModel().getSelectedItem()).getNombreClientes()));
            txtApellidoCliente.setText((((Clientes) tvCliente.getSelectionModel().getSelectedItem()).getApellidoClientes()));
            txtDireccionCliente.setText((((Clientes) tvCliente.getSelectionModel().getSelectedItem()).getDireccionClientes()));
            txtNIT.setText((((Clientes) tvCliente.getSelectionModel().getSelectedItem()).getNITClientes()));
            txtTelefonoCli.setText((((Clientes) tvCliente.getSelectionModel().getSelectedItem()).getTelefonoClientes()));
            txtCorreoCliente.setText((((Clientes) tvCliente.getSelectionModel().getSelectedItem()).getCorreoClientes()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Por favor selecciona una fila válida", "Error", JOptionPane.ERROR_MESSAGE);
        }
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

    @FXML
    public void Agregar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                limpiarControles();
                activarControles();
                agregarIcono.setFill(Color.WHITE);
                agregarIcono.setIcon(FontAwesomeIcons.SAVE);
                btnAgregar.setText("Guardar");
                eliminarIcono.setFill(Color.WHITE);
                eliminarIcono.setIcon(FontAwesomeIcons.CLOSE);
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReportes.setDisable(true);
                //imgAgregar.setImage(new Image("URL"));
                tipoDeOperador = operador.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                limpiarControles();
                desactivarControles();
                agregarIcono.setFill(Color.WHITE);
                agregarIcono.setIcon(FontAwesomeIcons.USER_PLUS);
                btnAgregar.setText("Agregar");
                eliminarIcono.setFill(Color.WHITE);
                eliminarIcono.setIcon(FontAwesomeIcons.USER_TIMES);
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportes.setDisable(false);
                /*regresar de nuevo a sus imagenes originales
                imgAgregar.setImage(new Image("URL"));*/
                tipoDeOperador = operador.NINGUNO;
                // cargarDatos();
                break;
        }

    }

    public void guardar() {
        Clientes registro = new Clientes();
        registro.setClienteID(Integer.parseInt(txtClienteID.getText()));
        registro.setNombreClientes(txtNombreCliente.getText());
        registro.setApellidoClientes(txtApellidoCliente.getText());
        registro.setDireccionClientes(txtDireccionCliente.getText());
        registro.setNITClientes(txtNIT.getText());
        registro.setTelefonoClientes(txtTelefonoCli.getText());
        registro.setCorreoClientes(txtCorreoCliente.getText());
        try {
            /*
                se utiliza la misma variable "Procedimiento" porque es un servidor local
                de lo contrario debe ser diferente el nombre de la variable
             */
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCliente(?,?,?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getClienteID());
            procedimiento.setString(2, registro.getNombreClientes());
            procedimiento.setString(3, registro.getApellidoClientes());
            procedimiento.setString(4, registro.getDireccionClientes());
            procedimiento.setString(5, registro.getNITClientes());
            procedimiento.setString(6, registro.getTelefonoClientes());
            procedimiento.setString(7, registro.getCorreoClientes());
            procedimiento.execute();
            listaClientes.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void eliminar() {
        switch (tipoDeOperador) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                agregarIcono.setFill(Color.WHITE);
                agregarIcono.setIcon(FontAwesomeIcons.USER_PLUS);
                btnAgregar.setText("Agregar");
                eliminarIcono.setFill(Color.WHITE);
                eliminarIcono.setIcon(FontAwesomeIcons.USER_TIMES);
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportes.setDisable(false);
                /*regresar de nuevo a sus imagenes originales
                imgAgregar.setImage(new Image("URL"));*/
                tipoDeOperador = operador.NINGUNO;
                break;
            default:
                if (tvCliente.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmas la eliminacion del registro?", "Eliminar Clientes", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarClientes(?)}");
                            procedimiento.setInt(1, ((Clientes) tvCliente.getSelectionModel().getSelectedItem()).getClienteID());
                            procedimiento.execute();
                            listaClientes.remove(tvCliente.getSelectionModel().getSelectedItem());
                            limpiarControles();
                            cargarDatos();
                            JOptionPane.showMessageDialog(null, "Cliente eliminada correctamente");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar una fila para eliminar");
                }

                break;
        }
    }

    //Editar lleva el mismo concepto que agregar y eliminar
    @FXML
    public void editar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                if (tvCliente.getSelectionModel().getSelectedItem() != null) {
                    actualizarIcono.setFill(Color.WHITE);
                    actualizarIcono.setIcon(FontAwesomeIcons.SAVE);
                    btnEditar.setText("Guardar");
                    reporteIcono.setFill(Color.WHITE);
                    reporteIcono.setIcon(FontAwesomeIcons.CLOSE);
                    btnReportes.setText("Cancelar");
                    btnEliminar.setDisable(true);
                    btnAgregar.setDisable(true);
                    activarControles();
                    txtClienteID.setEditable(false);
                    tipoDeOperador = operador.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar una fila para editar");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                actualizarIcono.setFill(Color.WHITE);
                actualizarIcono.setIcon(FontAwesomeIcons.EDIT);
                btnEditar.setText("Actualizar");
                reporteIcono.setFill(Color.WHITE);
                reporteIcono.setIcon(FontAwesomeIcons.FOLDER);
                btnReportes.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperador = operador.NINGUNO;
                cargarDatos();
                break;
        }
    }

    @FXML
    public void reportes() {
        switch (tipoDeOperador) {
            case NINGUNO:
                imprimirReporte();
                break;
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                actualizarIcono.setFill(Color.WHITE);
                actualizarIcono.setIcon(FontAwesomeIcons.EDIT);
                btnEditar.setText("Actualizar");
                reporteIcono.setFill(Color.WHITE);
                reporteIcono.setIcon(FontAwesomeIcons.FOLDER);
                btnReportes.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperador = operador.NINGUNO;
                break;

        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_editarClientes(?,?,?,?,?,?,?)}");
            Clientes registro = (Clientes) tvCliente.getSelectionModel().getSelectedItem();
            registro.setNombreClientes(txtNombreCliente.getText());
            registro.setApellidoClientes(txtApellidoCliente.getText());
            registro.setDireccionClientes(txtDireccionCliente.getText());
            registro.setNITClientes(txtNIT.getText());
            registro.setTelefonoClientes(txtTelefonoCli.getText());
            registro.setCorreoClientes(txtCorreoCliente.getText());
            procedimiento.setInt(1, registro.getClienteID());
            procedimiento.setString(2, registro.getNombreClientes());
            procedimiento.setString(3, registro.getApellidoClientes());
            procedimiento.setString(4, registro.getDireccionClientes());
            procedimiento.setString(5, registro.getNITClientes());
            procedimiento.setString(6, registro.getTelefonoClientes());
            procedimiento.setString(7, registro.getCorreoClientes());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void buscarCliente() {
        limpiarControles();
        FilteredList<Clientes> filtro = new FilteredList<>(listaClientes, e -> true);
        txtbuscarCliente.textProperty().addListener((Observable, oldValue, newValue) -> {
            filtro.setPredicate(predicateCliente -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String param = newValue.toLowerCase();
                String codigoCliente = String.valueOf(predicateCliente.getClienteID());

                if (codigoCliente.contains(param)) {
                    return true;
                } else if (predicateCliente.getNombreClientes().toLowerCase().contains(param)) {
                    return true;
                } else if (predicateCliente.getApellidoClientes().toLowerCase().contains(param)) {
                    return true;
                } else if (predicateCliente.getDireccionClientes().toLowerCase().contains(param)) {
                    return true;
                } else if (predicateCliente.getNITClientes().toLowerCase().contains(param)) {
                    return true;
                } else if (predicateCliente.getTelefonoClientes().toLowerCase().contains(param)) {
                    return true;
                } else if (predicateCliente.getCorreoClientes().toLowerCase().contains(param)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Clientes> sortList = new SortedList<>(filtro);
        sortList.comparatorProperty().bind(tvCliente.comparatorProperty());
        tvCliente.setItems(sortList);
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("clienteID", null);
        GenerarReportes.mostrarReportes("ReporteCliente.jasper", "Reporte de Cliente", parametros);
        
    }

    public void desactivarControles() {
        txtClienteID.setEditable(false);
        txtNombreCliente.setEditable(false);
        txtApellidoCliente.setEditable(false);
        txtDireccionCliente.setEditable(false);
        txtNIT.setEditable(false);
        txtTelefonoCli.setEditable(false);
        txtCorreoCliente.setEditable(false);
    }

    public void activarControles() {
        txtClienteID.setEditable(true);
        txtNombreCliente.setEditable(true);
        txtApellidoCliente.setEditable(true);
        txtDireccionCliente.setEditable(true);
        txtNIT.setEditable(true);
        txtTelefonoCli.setEditable(true);
        txtCorreoCliente.setEditable(true);
    }

    public void limpiarControles() {
        txtClienteID.clear();
        txtNombreCliente.clear();
        txtApellidoCliente.clear();
        txtDireccionCliente.clear();
        txtNIT.clear();
        txtTelefonoCli.clear();
        txtCorreoCliente.clear();
    }
    
    public void ventana(ActionEvent event) {
        colClienteID.prefWidthProperty().bind(tvCliente.widthProperty().multiply(0.0476));
        colApellidosClientes.prefWidthProperty().bind(tvCliente.widthProperty().multiply(0.15873));
        colCorreoClientes.prefWidthProperty().bind(tvCliente.widthProperty().multiply(0.15873));
        colDireccionClientes.prefWidthProperty().bind(tvCliente.widthProperty().multiply(0.15873));
        colNit.prefWidthProperty().bind(tvCliente.widthProperty().multiply(0.15873));
        colNombreCliente.prefWidthProperty().bind(tvCliente.widthProperty().multiply(0.15873));
        colTelefonoClientes.prefWidthProperty().bind(tvCliente.widthProperty().multiply(0.15873));
        
        if (event.getSource() == iconMaximizar || event.getSource() == btnMaximizar) {
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.5), iconMaximizar);
            rotateTransition.setByAngle(180);
            rotateTransition.setCycleCount(1);
            rotateTransition.setAutoReverse(false);

            if (stage.isMaximized()) {
                stage.setMaximized(false);
                
            } else {
                stage.setMaximized(true);
            }
            rotateTransition.play();
        }

    }

    
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresar) {
            escenarioPrincipal.menuPrincipalView();
        }
        if (event.getSource() == iconoCerrar || event.getSource() == btnCerrar) {
            System.exit(0);
        }
        if (event.getSource() == iconMinimizar || event.getSource() == btnMinimizar) {
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            stage.setIconified(true);
        }
    }
    

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

}
