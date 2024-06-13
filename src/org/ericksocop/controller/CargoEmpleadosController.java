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
import org.ericksocop.bean.CargoEmpleados;
import org.ericksocop.dao.Conexion;
import org.ericksocop.system.Main;

/**
 * FXML Controller class
 *
 * @author mauco
 */
public class CargoEmpleadosController implements Initializable {

    private enum operador {
        AGREGRAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operador tipoDeOperador = operador.NINGUNO;
    @FXML
    private TableView tvCargoE;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private FontAwesomeIcon iconMinimizar;
    @FXML
    private JFXButton btnMaximizar;
    @FXML
    private FontAwesomeIcon iconMaximizar;
    @FXML
    private FontAwesomeIcon reporteIcono;
    @FXML
    private JFXButton btnAgregarCE;
    @FXML
    private JFXButton btnEditarCE;
    @FXML
    private TableColumn colCodigoCargoE;
    @FXML
    private TableColumn colNombreC;
    @FXML
    private TableColumn colDescripcionC;
    @FXML
    private JFXButton btnEliminarCE;
    @FXML
    private JFXButton btnReportesCE;
    @FXML
    private JFXButton btnRegresarCE;
    @FXML
    private JFXTextField txtCodigoCargoE;
    @FXML
    private JFXTextField txtNombreCargo;
    @FXML
    private JFXTextField txtDescripcionC;
    @FXML
    private JFXTextField txtbuscarCargoEmp;
    @FXML
    private JFXButton btnCerrar;
    @FXML
    private FontAwesomeIcon iconoCerrar;
    @FXML
    private JFXButton btnMinimizar;
    @FXML
    private FontAwesomeIcon agregarIcono;
    @FXML
    private FontAwesomeIcon actualizarIcono;
    @FXML
    private FontAwesomeIcon eliminarIcono;
    private ObservableList<CargoEmpleados> listaCargo;
    private Main escenarioPrincipal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarDatos();
        desactivarControles();
        colCodigoCargoE.prefWidthProperty().bind(tvCargoE.widthProperty().multiply(0.0476));
        colDescripcionC.prefWidthProperty().bind(tvCargoE.widthProperty().multiply(0.47620));
        colNombreC.prefWidthProperty().bind(tvCargoE.widthProperty().multiply(0.47620));
    }

    public void cargarDatos() {
        tvCargoE.setItems(getCargoE());
        colCodigoCargoE.setCellValueFactory(new PropertyValueFactory<CargoEmpleados, Integer>("codigoCargoEmpleado"));
        colDescripcionC.setCellValueFactory(new PropertyValueFactory<CargoEmpleados, String>("descripcionCargo"));
        colNombreC.setCellValueFactory(new PropertyValueFactory<CargoEmpleados, String>("nombreCargo"));

    }

    public void seleccionarElemento() {
        try {
            txtCodigoCargoE.setText(String.valueOf(((CargoEmpleados) tvCargoE.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado()));
            txtNombreCargo.setText((((CargoEmpleados) tvCargoE.getSelectionModel().getSelectedItem()).getNombreCargo()));
            txtDescripcionC.setText((((CargoEmpleados) tvCargoE.getSelectionModel().getSelectedItem()).getDescripcionCargo()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Por favor selecciona una fila válida", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public ObservableList<CargoEmpleados> getCargoE() {
        ArrayList<CargoEmpleados> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_mostrarCargoEmpleado()}");

            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new CargoEmpleados(resultado.getInt("codigoCargoEmpleado"),
                        resultado.getString("nombreCargo"),
                        resultado.getString("descripcionCargo")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaCargo = FXCollections.observableList(lista);
    }

    public void Agregar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                limpiarControles();
                activarControles();
                agregarIcono.setFill(Color.WHITE);
                agregarIcono.setIcon(FontAwesomeIcons.SAVE);
                btnAgregarCE.setText("Guardar");
                eliminarIcono.setFill(Color.WHITE);
                eliminarIcono.setIcon(FontAwesomeIcons.CLOSE);
                btnEliminarCE.setText("Cancelar");
                btnEditarCE.setDisable(true);
                btnReportesCE.setDisable(true);
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
                btnAgregarCE.setText("Agregar");
                eliminarIcono.setFill(Color.WHITE);
                eliminarIcono.setIcon(FontAwesomeIcons.TRASH);
                btnEliminarCE.setText("Eliminar");
                btnEditarCE.setDisable(false);
                btnReportesCE.setDisable(false);
                /*regresar de nuevo a sus imagenes originales
                imgAgregar.setImage(new Image("URL"));*/
                tipoDeOperador = operador.NINGUNO;
                break;
        }

    }

    public void guardar() {
        CargoEmpleados registro = new CargoEmpleados();
        try {
            int cargoID = Integer.parseInt(txtCodigoCargoE.getText());
            if(existeCodigoCargo(cargoID)){
                JOptionPane.showMessageDialog(null, "El ID del Cargo ya existe. Por favor, ingrese uno nuevo.","Error", JOptionPane.ERROR_MESSAGE);
                return; // Detener el proceso de guardado
            }
            registro.setCodigoCargoEmpleado(cargoID);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID del Cargo no puede ser nulo/vacio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        registro.setNombreCargo(txtNombreCargo.getText());
        registro.setDescripcionCargo(txtDescripcionC.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_agregarCargoEmpleado(?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoCargoEmpleado());
            procedimiento.setString(2, registro.getNombreCargo());
            procedimiento.setString(3, registro.getDescripcionCargo());
            procedimiento.execute();
            listaCargo.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private boolean existeCodigoCargo(int codigoCargo) {
    for (CargoEmpleados cliente : listaCargo) {
        if (cliente.getCodigoCargoEmpleado()== codigoCargo) {
            return true;
        }
    }
    return false;
}

    public void eliminar() {
        switch (tipoDeOperador) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                agregarIcono.setFill(Color.WHITE);
                agregarIcono.setIcon(FontAwesomeIcons.PLUS);
                btnAgregarCE.setText("Agregar");
                eliminarIcono.setFill(Color.WHITE);
                eliminarIcono.setIcon(FontAwesomeIcons.TRASH);
                btnEliminarCE.setText("Eliminar");
                btnEditarCE.setDisable(false);
                btnReportesCE.setDisable(false);
                /*regresar de nuevo a sus imagenes originales
                imgAgregar.setImage(new Image("URL"));*/
                tipoDeOperador = operador.NINGUNO;
                break;
            default:
                if (tvCargoE.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmas la eliminacion del registro?", "Eliminar Cargo Empleados", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarCargoEmpleado(?)}");
                            procedimiento.setInt(1, ((CargoEmpleados) tvCargoE.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado());
                            procedimiento.execute();
                            listaCargo.remove(tvCargoE.getSelectionModel().getSelectedItem());
                            limpiarControles();
                            cargarDatos();
                            JOptionPane.showMessageDialog(null, "Cargo eliminada correctamente");
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
                if (tvCargoE.getSelectionModel().getSelectedItem() != null) {
                    actualizarIcono.setFill(Color.WHITE);
                    actualizarIcono.setIcon(FontAwesomeIcons.SAVE);
                    btnEditarCE.setText("Guardar");
                    reporteIcono.setFill(Color.WHITE);
                    reporteIcono.setIcon(FontAwesomeIcons.CLOSE);
                    btnReportesCE.setText("Cancelar");
                    btnEliminarCE.setDisable(true);
                    btnAgregarCE.setDisable(true);
                    activarControles();
                    txtCodigoCargoE.setEditable(false);
                    tipoDeOperador = operador.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar una fila para editar");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                actualizarIcono.setFill(Color.WHITE);
                actualizarIcono.setIcon(FontAwesomeIcons.EDIT);
                btnEditarCE.setText("Actualizar");
                reporteIcono.setFill(Color.WHITE);
                reporteIcono.setIcon(FontAwesomeIcons.FOLDER);
                btnReportesCE.setText("Reportes");
                btnEliminarCE.setDisable(false);
                btnAgregarCE.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperador = operador.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void reportes() {
        switch (tipoDeOperador) {
            case NINGUNO:
                break;
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                actualizarIcono.setFill(Color.WHITE);
                actualizarIcono.setIcon(FontAwesomeIcons.EDIT);
                btnEditarCE.setText("Actualizar");
                reporteIcono.setFill(Color.WHITE);
                reporteIcono.setIcon(FontAwesomeIcons.FOLDER);
                btnReportesCE.setText("Reportes");
                btnAgregarCE.setDisable(false);
                btnEliminarCE.setDisable(false);
                tipoDeOperador = operador.NINGUNO;
                break;
        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_editarCargoEmpleado(?,?,?)}");
            CargoEmpleados registro = (CargoEmpleados) tvCargoE.getSelectionModel().getSelectedItem();
            registro.setNombreCargo(txtNombreCargo.getText());
            registro.setDescripcionCargo(txtDescripcionC.getText());
            procedimiento.setInt(1, registro.getCodigoCargoEmpleado());
            procedimiento.setString(2, registro.getNombreCargo());
            procedimiento.setString(3, registro.getDescripcionCargo());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buscarCargoEmp() {
        limpiarControles();
        FilteredList<CargoEmpleados> filtro = new FilteredList<>(listaCargo, e -> true);
        txtbuscarCargoEmp.textProperty().addListener((Observable, oldValue, newValue) -> {
            filtro.setPredicate(predicateCargoEmpleado -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String param = newValue.toLowerCase();
                String codigoCargoEmpleado = String.valueOf(predicateCargoEmpleado.getCodigoCargoEmpleado());

                if (codigoCargoEmpleado.contains(param)) {
                    return true;
                } else if (predicateCargoEmpleado.getNombreCargo().toLowerCase().contains(param)) {
                    return true;
                } else if (predicateCargoEmpleado.getDescripcionCargo().toLowerCase().contains(param)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<CargoEmpleados> sortList = new SortedList<>(filtro);
        sortList.comparatorProperty().bind(tvCargoE.comparatorProperty());
        tvCargoE.setItems(sortList);
    }

    public void activarControles() {
        txtCodigoCargoE.setEditable(true);
        txtDescripcionC.setEditable(true);
        txtNombreCargo.setEditable(true);
    }

    public void limpiarControles() {
        txtCodigoCargoE.clear();
        txtDescripcionC.clear();
        txtNombreCargo.clear();
    }

    public void desactivarControles() {
        txtCodigoCargoE.setEditable(false);
        txtDescripcionC.setEditable(false);
        txtNombreCargo.setEditable(false);
    }

    public void actualizarIconoMaximizar(boolean isMaximized) {
        if (isMaximized) {
            iconMaximizar.setRotate(180);
        } else {
            iconMaximizar.setRotate(0);
        }
    }

    public void ventana(ActionEvent event) {
        colCodigoCargoE.prefWidthProperty().bind(tvCargoE.widthProperty().multiply(0.0476));
        colDescripcionC.prefWidthProperty().bind(tvCargoE.widthProperty().multiply(0.47620));
        colNombreC.prefWidthProperty().bind(tvCargoE.widthProperty().multiply(0.47620));
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
                //iconMaximizar.setRotate(180);
            }
            rotateTransition.play();
        }
    }

    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresarCE) {
            escenarioPrincipal.EmpleadosView();
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
