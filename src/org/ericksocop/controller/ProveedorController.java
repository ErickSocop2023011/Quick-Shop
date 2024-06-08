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
import java.util.ResourceBundle;
import javafx.animation.RotateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;
import org.ericksocop.bean.Proveedores;
import org.ericksocop.dao.Conexion;
import org.ericksocop.system.Main;

/**
 * FXML Controller class
 *
 * @author mauco
 */
public class ProveedorController implements Initializable {

    private ObservableList<Proveedores> listaProveedores;
    private Main escenarioPrincipal;
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
    private JFXButton btnMaximizar;
    @FXML
    private FontAwesomeIcon iconMaximizar;
    @FXML
    private FontAwesomeIcon reporteIcono;
    @FXML
    private JFXTextField txtbuscarProv;

    private enum operador {
        AGREGRAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operador tipoDeOperador = operador.NINGUNO;
    @FXML
    private TableView tvPoveedores;
    @FXML
    private TableColumn colCodP;
    @FXML
    private TableColumn colNITP;
    @FXML
    private TableColumn colNomP;
    @FXML
    private TableColumn colApeP;
    @FXML
    private TableColumn colDireP;
    @FXML
    private TableColumn colRazonS;
    @FXML
    private TableColumn colContactoP;
    @FXML
    private TableColumn colSitioWeb;
    @FXML
    private TableColumn colTelP;
    @FXML
    private TableColumn colEmailP;
    @FXML
    private JFXButton btnAgregarP;
    @FXML
    private JFXButton btnEditarP;
    @FXML
    private JFXButton btnEliminarP;
    @FXML
    private JFXButton btnReportesP;
    @FXML
    private JFXTextField txtCodigoP;
    @FXML
    private JFXTextField txtNITP;
    @FXML
    private JFXTextField txtNombresP;
    @FXML
    private JFXTextField txtApellidosP;
    @FXML
    private JFXTextField txtDireccionP;
    @FXML
    private JFXTextField txtRazonSocial;
    @FXML
    private JFXTextField txtContactoP;
    @FXML
    private JFXTextField txtSitioWeb;
    @FXML
    private JFXTextField txtTelefono;
    @FXML
    private JFXTextField txtEmailP;
    @FXML
    private JFXButton btnRegresarP;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarDatos();
        desactivarControles();
        colCodP.prefWidthProperty().bind(tvPoveedores.widthProperty().multiply(0.0476));
        colApeP.prefWidthProperty().bind(tvPoveedores.widthProperty().multiply(0.10582));
        colContactoP.prefWidthProperty().bind(tvPoveedores.widthProperty().multiply(0.10582));
        colDireP.prefWidthProperty().bind(tvPoveedores.widthProperty().multiply(0.10582));
        colEmailP.prefWidthProperty().bind(tvPoveedores.widthProperty().multiply(0.10582));
        colNITP.prefWidthProperty().bind(tvPoveedores.widthProperty().multiply(0.10582));
        colNomP.prefWidthProperty().bind(tvPoveedores.widthProperty().multiply(0.10582));
        colRazonS.prefWidthProperty().bind(tvPoveedores.widthProperty().multiply(0.10582));
        colSitioWeb.prefWidthProperty().bind(tvPoveedores.widthProperty().multiply(0.10582));
        colTelP.prefWidthProperty().bind(tvPoveedores.widthProperty().multiply(0.10582));
    }

    public void cargarDatos() {
        tvPoveedores.setItems(getProveedores());
        colCodP.setCellValueFactory(new PropertyValueFactory<Proveedores, Integer>("codigoProveedor"));
        colNITP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("NITProveedor"));
        colNomP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("nombresProveedor"));
        colApeP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("apellidosProveedor"));
        colDireP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("direccionProveedor"));
        colRazonS.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("razonSocial"));
        colContactoP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("contactoPrincipal"));
        colSitioWeb.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("paginaWeb"));
        colTelP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("telefonoProveedor"));
        colEmailP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("emailProveedor"));
    }

    public void seleccionarElemento() {
        try {
            txtCodigoP.setText(String.valueOf(((Proveedores) tvPoveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
            txtNITP.setText((((Proveedores) tvPoveedores.getSelectionModel().getSelectedItem()).getNITProveedor()));
            txtNombresP.setText((((Proveedores) tvPoveedores.getSelectionModel().getSelectedItem()).getNombresProveedor()));
            txtApellidosP.setText((((Proveedores) tvPoveedores.getSelectionModel().getSelectedItem()).getApellidosProveedor()));
            txtDireccionP.setText((((Proveedores) tvPoveedores.getSelectionModel().getSelectedItem()).getDireccionProveedor()));
            txtRazonSocial.setText((((Proveedores) tvPoveedores.getSelectionModel().getSelectedItem()).getRazonSocial()));
            txtContactoP.setText((((Proveedores) tvPoveedores.getSelectionModel().getSelectedItem()).getContactoPrincipal()));
            txtSitioWeb.setText((((Proveedores) tvPoveedores.getSelectionModel().getSelectedItem()).getPaginaWeb()));
            txtTelefono.setText((((Proveedores) tvPoveedores.getSelectionModel().getSelectedItem()).getTelefonoProveedor()));
            txtEmailP.setText((((Proveedores) tvPoveedores.getSelectionModel().getSelectedItem()).getEmailProveedor()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Por favor selecciona una fila válida", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public ObservableList<Proveedores> getProveedores() {
        ArrayList<Proveedores> listaPro = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_mostrarproveedores()}");
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

    public void Agregar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                limpiarControles();
                activarControles();
                agregarIcono.setFill(Color.WHITE);
                agregarIcono.setIcon(FontAwesomeIcons.SAVE);
                btnAgregarP.setText("Guardar");
                eliminarIcono.setFill(Color.WHITE);
                eliminarIcono.setIcon(FontAwesomeIcons.CLOSE);
                btnEliminarP.setText("Cancelar");
                btnEditarP.setDisable(true);
                btnReportesP.setDisable(true);
                //imgAgregar.setImage(new Image("URL"));
                tipoDeOperador = operador.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                limpiarControles();
                desactivarControles();
                agregarIcono.setFill(Color.WHITE);
                agregarIcono.setIcon(FontAwesomeIcons.USER_PLUS);
                btnAgregarP.setText("Agregar");
                eliminarIcono.setFill(Color.WHITE);
                eliminarIcono.setIcon(FontAwesomeIcons.USER_TIMES);
                btnEliminarP.setText("Eliminar");
                btnEditarP.setDisable(false);
                btnReportesP.setDisable(false);
                /*regresar de nuevo a sus imagenes originales
                imgAgregar.setImage(new Image("URL"));*/
                tipoDeOperador = operador.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void guardar() {
        Proveedores registro = new Proveedores();
        registro.setCodigoProveedor(Integer.parseInt(txtCodigoP.getText()));
        registro.setNITProveedor(txtNITP.getText());
        registro.setNombresProveedor(txtNombresP.getText());
        registro.setApellidosProveedor(txtApellidosP.getText());
        registro.setDireccionProveedor(txtDireccionP.getText());
        registro.setRazonSocial(txtRazonSocial.getText());
        registro.setContactoPrincipal(txtContactoP.getText());
        registro.setPaginaWeb(txtSitioWeb.getText());
        registro.setTelefonoProveedor(txtTelefono.getText());
        registro.setEmailProveedor(txtEmailP.getText());

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_agregarproveedor(?,?,?,?,?,?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoProveedor());
            procedimiento.setString(2, registro.getNITProveedor());
            procedimiento.setString(3, registro.getNombresProveedor());
            procedimiento.setString(4, registro.getApellidosProveedor());
            procedimiento.setString(5, registro.getDireccionProveedor());
            procedimiento.setString(6, registro.getRazonSocial());
            procedimiento.setString(7, registro.getContactoPrincipal());
            procedimiento.setString(8, registro.getPaginaWeb());
            procedimiento.setString(9, registro.getTelefonoProveedor());
            procedimiento.setString(10, registro.getEmailProveedor());
            procedimiento.execute();
            listaProveedores.add(registro);
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
                btnAgregarP.setText("Agregar");
                eliminarIcono.setFill(Color.WHITE);
                eliminarIcono.setIcon(FontAwesomeIcons.USER_TIMES);
                btnEliminarP.setText("Eliminar");
                btnEditarP.setDisable(false);
                btnReportesP.setDisable(false);
                /*regresar de nuevo a sus imagenes originales
                imgAgregar.setImage(new Image("URL"));*/
                tipoDeOperador = operador.NINGUNO;
                break;
            default:
                if (tvPoveedores.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmas la eliminacion del registro?", "Eliminar Proveedores", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            Proveedores productoSeleccionado = (Proveedores) tvPoveedores.getSelectionModel().getSelectedItem();
                            int codigoProducto = productoSeleccionado.getCodigoProveedor();

                            PreparedStatement eliminarDetalleCompraStmt = Conexion.getInstance().getConexion()
                                    .prepareCall("{call sp_eliminarDetalleCompraPorProducto(?)}");
                            eliminarDetalleCompraStmt.setInt(1, codigoProducto);
                            eliminarDetalleCompraStmt.execute();

                            PreparedStatement eliminarDetalleFacturaStmt = Conexion.getInstance().getConexion()
                                    .prepareCall("{call sp_eliminarDetalleFacturaPorProducto(?)}");
                            eliminarDetalleFacturaStmt.setInt(1, codigoProducto);
                            eliminarDetalleFacturaStmt.execute();

                            PreparedStatement eliminarProductoPorProveedorStmt = Conexion.getInstance().getConexion()
                                    .prepareCall("{call sp_eliminarProductoPorProveedor(?)}");
                            eliminarProductoPorProveedorStmt.setInt(1, codigoProducto);
                            eliminarProductoPorProveedorStmt.execute();

                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarproveedor(?)}");
                            procedimiento.setInt(1, codigoProducto);
                            procedimiento.execute();
                            limpiarControles();
                            cargarDatos();
                            JOptionPane.showMessageDialog(null, "Proveedor eliminado correctamente");
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

    public void editar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                if (tvPoveedores.getSelectionModel().getSelectedItem() != null) {
                    actualizarIcono.setFill(Color.WHITE);
                    actualizarIcono.setIcon(FontAwesomeIcons.SAVE);
                    btnEditarP.setText("Guardar");
                    reporteIcono.setFill(Color.WHITE);
                    reporteIcono.setIcon(FontAwesomeIcons.CLOSE);
                    btnReportesP.setText("Cancelar");
                    btnEliminarP.setDisable(true);
                    btnAgregarP.setDisable(true);
                    activarControles();
                    txtCodigoP.setEditable(false);
                    tipoDeOperador = operador.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar una fila para editar");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditarP.setText("Actualizar");
                actualizarIcono.setFill(Color.WHITE);
                actualizarIcono.setIcon(FontAwesomeIcons.EDIT);
                reporteIcono.setFill(Color.WHITE);
                reporteIcono.setIcon(FontAwesomeIcons.FOLDER);
                btnReportesP.setText("Reportes");
                btnAgregarP.setDisable(false);
                btnEliminarP.setDisable(false);
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
                actualizarIcono.setFill(Color.WHITE);
                actualizarIcono.setIcon(FontAwesomeIcons.EDIT);
                btnEditarP.setText("Actualizar");
                reporteIcono.setFill(Color.WHITE);
                reporteIcono.setIcon(FontAwesomeIcons.FOLDER);
                btnReportesP.setText("Reportes");
                btnAgregarP.setDisable(false);
                btnEliminarP.setDisable(false);
                tipoDeOperador = operador.NINGUNO;

        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_editarproveedor(?,?,?,?,?,?,?,?,?,?)}");
            Proveedores registro = (Proveedores) tvPoveedores.getSelectionModel().getSelectedItem();
            registro.setNombresProveedor(txtNombresP.getText());
            registro.setApellidosProveedor(txtApellidosP.getText());
            registro.setDireccionProveedor(txtDireccionP.getText());
            registro.setRazonSocial(txtRazonSocial.getText());
            registro.setContactoPrincipal(txtContactoP.getText());
            registro.setPaginaWeb(txtSitioWeb.getText());
            registro.setTelefonoProveedor(txtTelefono.getText());
            registro.setEmailProveedor(txtEmailP.getText());
            procedimiento.setInt(1, registro.getCodigoProveedor());
            procedimiento.setString(2, registro.getNITProveedor());
            procedimiento.setString(3, registro.getNombresProveedor());
            procedimiento.setString(4, registro.getApellidosProveedor());
            procedimiento.setString(5, registro.getDireccionProveedor());
            procedimiento.setString(6, registro.getRazonSocial());
            procedimiento.setString(7, registro.getContactoPrincipal());
            procedimiento.setString(8, registro.getPaginaWeb());
            procedimiento.setString(9, registro.getTelefonoProveedor());
            procedimiento.setString(10, registro.getEmailProveedor());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buscarProv() {
        limpiarControles();
        FilteredList<Proveedores> filtro = new FilteredList<>(listaProveedores, e -> true);
        txtbuscarProv.textProperty().addListener((Observable, oldValue, newValue) -> {
            filtro.setPredicate(predicateProveedor -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String param = newValue.toLowerCase();
                String provID = String.valueOf(predicateProveedor.getCodigoProveedor());

                if (provID.contains(param)) {
                    return true;
                } else if (predicateProveedor.getNombresProveedor().toLowerCase().contains(param)) {
                    return true;
                } else if (predicateProveedor.getApellidosProveedor().toLowerCase().contains(param)) {
                    return true;
                } else if (predicateProveedor.getNITProveedor().toLowerCase().contains(param)) {
                    return true;
                } else if (predicateProveedor.getDireccionProveedor().toLowerCase().contains(param)) {
                    return true;
                } else if (predicateProveedor.getRazonSocial().toLowerCase().contains(param)) {
                    return true;
                } else if (predicateProveedor.getContactoPrincipal().toLowerCase().contains(param)) {
                    return true;
                } else if (predicateProveedor.getPaginaWeb().toLowerCase().contains(param)) {
                    return true;
                } else if (predicateProveedor.getTelefonoProveedor().toLowerCase().contains(param)) {
                    return true;
                } else if (predicateProveedor.getEmailProveedor().toLowerCase().contains(param)) {
                    return true;
                } else {

                    return false;
                }
            });
        });
        SortedList<Proveedores> sortList = new SortedList<>(filtro);
        sortList.comparatorProperty().bind(tvPoveedores.comparatorProperty());
        tvPoveedores.setItems(sortList);

    }

    public void desactivarControles() {
        txtCodigoP.setEditable(false);
        txtNITP.setEditable(false);
        txtNombresP.setEditable(false);
        txtApellidosP.setEditable(false);
        txtDireccionP.setEditable(false);
        txtRazonSocial.setEditable(false);
        txtContactoP.setEditable(false);
        txtSitioWeb.setEditable(false);
        txtTelefono.setEditable(false);
        txtEmailP.setEditable(false);
    }

    public void activarControles() {
        txtCodigoP.setEditable(true);
        txtNITP.setEditable(true);
        txtNombresP.setEditable(true);
        txtApellidosP.setEditable(true);
        txtDireccionP.setEditable(true);
        txtRazonSocial.setEditable(true);
        txtContactoP.setEditable(true);
        txtSitioWeb.setEditable(true);
        txtTelefono.setEditable(true);
        txtEmailP.setEditable(true);
    }

    public void limpiarControles() {
        txtCodigoP.clear();
        txtNITP.clear();
        txtNombresP.clear();
        txtApellidosP.clear();
        txtDireccionP.clear();
        txtRazonSocial.clear();
        txtContactoP.clear();
        txtSitioWeb.clear();
        txtTelefono.clear();
        txtEmailP.clear();
    }

    public void MenuPrincipalView() {
        escenarioPrincipal.menuPrincipalView();
    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresarP) {
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

    public void ventana(ActionEvent event) {
        colCodP.prefWidthProperty().bind(tvPoveedores.widthProperty().multiply(0.0476));
        colApeP.prefWidthProperty().bind(tvPoveedores.widthProperty().multiply(0.10582));
        colContactoP.prefWidthProperty().bind(tvPoveedores.widthProperty().multiply(0.10582));
        colDireP.prefWidthProperty().bind(tvPoveedores.widthProperty().multiply(0.10582));
        colEmailP.prefWidthProperty().bind(tvPoveedores.widthProperty().multiply(0.10582));
        colNITP.prefWidthProperty().bind(tvPoveedores.widthProperty().multiply(0.10582));
        colNomP.prefWidthProperty().bind(tvPoveedores.widthProperty().multiply(0.10582));
        colRazonS.prefWidthProperty().bind(tvPoveedores.widthProperty().multiply(0.10582));
        colSitioWeb.prefWidthProperty().bind(tvPoveedores.widthProperty().multiply(0.10582));
        colTelP.prefWidthProperty().bind(tvPoveedores.widthProperty().multiply(0.10582));
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

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

}
