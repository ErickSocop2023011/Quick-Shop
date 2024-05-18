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
import org.ericksocop.bean.Proveedores;
import org.ericksocop.dao.Conexion;
import org.ericksocop.system.Main;

/**
 * FXML Controller class
 *
 * @author mauco
 */
public class ProveedorViewController implements Initializable {

    private ObservableList<Proveedores> listaProveedores;
    private Main escenarioPrincipal;

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
    private Button btnAgregarP;
    @FXML
    private Button btnEditarP;
    @FXML
    private Button btnEliminarP;
    @FXML
    private Button btnReportesP;
    @FXML
    private TextField txtCodigoP;
    @FXML
    private TextField txtNITP;
    @FXML
    private TextField txtNombresP;
    @FXML
    private TextField txtApellidosP;
    @FXML
    private TextField txtDireccionP;
    @FXML
    private TextField txtRazonSocial;
    @FXML
    private TextField txtContactoP;
    @FXML
    private TextField txtSitioWeb;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtEmailP;
    @FXML
    private Button btnRegresarP;

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

    public void Agregar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnAgregarP.setText("Guardar");
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
                btnAgregarP.setText("Agregar");
                btnEliminarP.setText("Eliminar");
                btnEditarP.setDisable(false);
                btnReportesP.setDisable(false);
                /*regresar de nuevo a sus imagenes originales
                imgAgregar.setImage(new Image("URL"));*/
                tipoDeOperador = operador.NINGUNO;
                // cargarDatos();
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
                btnAgregarP.setText("Agregar");
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
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarproveedor(?)}");
                            procedimiento.setInt(1, ((Proveedores) tvPoveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor());
                            procedimiento.execute();
                            listaProveedores.remove(tvPoveedores.getSelectionModel().getSelectedItem());
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
                    btnEditarP.setText("Actualizar");
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
                btnEditarP.setText("Editar");
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
                btnEditarP.setText("Editar");
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
            registro.setNITProveedor(txtNITP.getText());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresarP) {
            escenarioPrincipal.menuPrincipalView();
        }
    }

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

}
