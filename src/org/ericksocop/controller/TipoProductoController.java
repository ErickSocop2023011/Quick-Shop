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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;
import org.ericksocop.bean.TipoProducto;
import org.ericksocop.dao.Conexion;
import org.ericksocop.system.Main;

/**
 * FXML Controller class
 *
 * @author mauco
 */
public class TipoProductoController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private JFXButton btnCerrar;
    @FXML
    private FontAwesomeIcon iconoCerrar;
    @FXML
    private JFXButton btnMinimizar;
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
    private JFXTextField txtbuscarTipoProd;
    @FXML
    private TableView tvDescripcionP;
    @FXML
    private TableColumn colCodigoP;
    @FXML
    private TableColumn colDescripcionP;
    @FXML
    private JFXTextField txtCodigoP;
    @FXML
    private JFXTextField txtDescripcionP;
    @FXML
    private JFXButton btnAgregarP;
    @FXML
    private JFXButton btnEditarP;
    @FXML
    private JFXButton btnEliminarP;
    @FXML
    private JFXButton btnReportesP;
    @FXML
    private JFXButton btnRegresarTipoP;
    @FXML
    private JFXButton btnMaximizar;
    @FXML
    private FontAwesomeIcon iconMaximizar;

    private enum operador {
        AGREGRAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operador tipoDeOperador = operador.NINGUNO;
    private ObservableList<TipoProducto> listaTipoP;

    private Main escenarioPrincipal;

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
        tvDescripcionP.setItems(getTipoP());
        colCodigoP.setCellValueFactory(new PropertyValueFactory<TipoProducto, Integer>("codigoTipoProducto"));
        colDescripcionP.setCellValueFactory(new PropertyValueFactory<TipoProducto, String>("descripcionProducto"));
    }

    @FXML
    public void seleccionarElemento() {
        try {
            txtCodigoP.setText(String.valueOf(((TipoProducto) tvDescripcionP.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
            txtDescripcionP.setText((((TipoProducto) tvDescripcionP.getSelectionModel().getSelectedItem()).getDescripcionProducto()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Por favor selecciona una fila válida", "Error", JOptionPane.ERROR_MESSAGE);
        }
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

    @FXML
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
                cargarDatos();
                desactivarControles();
                agregarIcono.setFill(Color.WHITE);
                agregarIcono.setIcon(FontAwesomeIcons.PLUS);
                btnAgregarP.setText("Agregar");
                eliminarIcono.setFill(Color.WHITE);
                eliminarIcono.setIcon(FontAwesomeIcons.TRASH);
                btnEliminarP.setText("Eliminar");
                btnEditarP.setDisable(false);
                btnReportesP.setDisable(false);
                /*regresar de nuevo a sus imagenes originales
                imgAgregar.setImage(new Image("URL"));*/
                tipoDeOperador = operador.NINGUNO;
                break;
        }

    }

    public void guardar() {
        TipoProducto registro = new TipoProducto();
        registro.setCodigoTipoProducto(Integer.parseInt(txtCodigoP.getText()));
        registro.setDesrcipcionProducto(txtDescripcionP.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_agregarTipoProducto(?,?)}");
            procedimiento.setInt(1, registro.getCodigoTipoProducto());
            procedimiento.setString(2, registro.getDescripcionProducto());
            procedimiento.execute();
            listaTipoP.add(registro);
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
                agregarIcono.setIcon(FontAwesomeIcons.PLUS);
                btnAgregarP.setText("Agregar");
                eliminarIcono.setFill(Color.WHITE);
                eliminarIcono.setIcon(FontAwesomeIcons.TRASH);
                btnEliminarP.setText("Eliminar");
                btnEditarP.setDisable(false);
                btnReportesP.setDisable(false);
                /*regresar de nuevo a sus imagenes originales
                imgAgregar.setImage(new Image("URL"));*/
                tipoDeOperador = operador.NINGUNO;
                break;
            default:
                if (tvDescripcionP.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmas la eliminacion del registro?", "Eliminar Tipo de Productos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarTipoProducto(?)}");
                            procedimiento.setInt(1, ((TipoProducto) tvDescripcionP.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
                            procedimiento.execute();
                            listaTipoP.remove(tvDescripcionP.getSelectionModel().getSelectedItem());
                            limpiarControles();
                            cargarDatos();
                            JOptionPane.showMessageDialog(null, "Tipo Producto eliminada correctamente");
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

    @FXML
    public void editar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                if (tvDescripcionP.getSelectionModel().getSelectedItem() != null) {
                    actualizarIcono.setFill(Color.WHITE);
                    actualizarIcono.setIcon(FontAwesomeIcons.SAVE);
                    btnEditarP.setText("Actualizar");
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
                btnEditarP.setText("Editar");
                btnReportesP.setText("Reportes");
                btnEliminarP.setDisable(false);
                btnAgregarP.setDisable(false);
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_editarTipoProducto(?,?)}");
            TipoProducto registro = (TipoProducto) tvDescripcionP.getSelectionModel().getSelectedItem();
            registro.setDesrcipcionProducto(txtDescripcionP.getText());
            procedimiento.setInt(1, registro.getCodigoTipoProducto());
            procedimiento.setString(2, registro.getDescripcionProducto());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buscarTipoProd() {
        limpiarControles();
        FilteredList<TipoProducto> filtro = new FilteredList<>(listaTipoP, e -> true);
        txtbuscarTipoProd.textProperty().addListener((Observable, oldValue, newValue) -> {
            filtro.setPredicate(predicateTipoProducto -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String param = newValue.toLowerCase();
                String tipoPID = String.valueOf(predicateTipoProducto.getCodigoTipoProducto());

                if (tipoPID.contains(param)) {
                    return true;
                } else if (predicateTipoProducto.getDescripcionProducto().toLowerCase().contains(param)) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<TipoProducto> sortList = new SortedList<>(filtro);
        sortList.comparatorProperty().bind(tvDescripcionP.comparatorProperty());
        tvDescripcionP.setItems(sortList);
    }

    public void activarControles() {
        txtCodigoP.setEditable(true);
        txtDescripcionP.setEditable(true);
    }

    public void limpiarControles() {
        txtCodigoP.clear();
        txtDescripcionP.clear();
    }

    public void desactivarControles() {
        txtCodigoP.setEditable(false);
        txtDescripcionP.setEditable(false);
    }

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresarTipoP) {
            escenarioPrincipal.ProductosView();
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
        if (event.getSource() == iconMaximizar || event.getSource() == btnMaximizar) {
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            // Configurar la rotación del icono
            RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.5), iconMaximizar);
            rotateTransition.setByAngle(180); // Rotación de 180 grados
            rotateTransition.setCycleCount(1); // Número de ciclos de la animación
            rotateTransition.setAutoReverse(false); // No revertir la animación

            if (stage.isMaximized()) {
                rotateTransition.play(); // Reproducir la animación de rotación
                stage.setMaximized(false);
            } else {
                rotateTransition.play(); // Reproducir la animación de rotación
                stage.setMaximized(true);
            }
        }
    }

}
