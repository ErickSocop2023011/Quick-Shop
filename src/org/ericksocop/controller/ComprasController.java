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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.ericksocop.bean.Compras;
import org.ericksocop.dao.Conexion;
import org.ericksocop.system.Main;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import javafx.animation.RotateTransition;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author mauco
 */
public class ComprasController implements Initializable {

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
    private JFXButton btnMaximizar;
    @FXML
    private FontAwesomeIcon iconMaximizar;
    @FXML
    private FontAwesomeIcon agregarIcono;
    @FXML
    private FontAwesomeIcon actualizarIcono;
    @FXML
    private FontAwesomeIcon eliminarIcono;
    @FXML
    private FontAwesomeIcon reporteIcono;
    @FXML
    private JFXTextField txtbuscarCompra;

    private enum operador {
        AGREGRAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operador tipoDeOperador = operador.NINGUNO;
    private ObservableList<Compras> listaCompras;
    private Main escenarioPrincipal;

    @FXML
    private TableView tvCompras;
    @FXML
    private TableColumn colNumDoc;
    @FXML
    private TableColumn colFechaDoc;
    @FXML
    private TableColumn colDescCom;
    @FXML
    private TableColumn colTotalDoc;
    @FXML
    private JFXButton btnDetalleCompra;
    @FXML
    private JFXButton btnAgregarC;
    @FXML
    private JFXButton btnEditarC;
    @FXML
    private JFXButton btnEliminarC;
    @FXML
    private JFXButton btnReportesC;
    @FXML
    private JFXButton btnRegresarC;
    @FXML
    private JFXTextField txtNumDoc;
    @FXML
    private JFXDatePicker jfxDatePicker;
    @FXML
    private JFXTextField txtDescDoc;
    @FXML
    private JFXTextField txtTotalDoc;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarDatos();
        desactivarControles();
        colNumDoc.prefWidthProperty().bind(tvCompras.widthProperty().multiply(0.0476));
        colDescCom.prefWidthProperty().bind(tvCompras.widthProperty().multiply(0.31746));
        colFechaDoc.prefWidthProperty().bind(tvCompras.widthProperty().multiply(0.31746));
        colTotalDoc.prefWidthProperty().bind(tvCompras.widthProperty().multiply(0.31746));

    }

    public void cargarDatos() {
        tvCompras.setItems(getCompra());
        colNumDoc.setCellValueFactory(new PropertyValueFactory<Compras, Integer>("numeroDocumento"));
        colFechaDoc.setCellValueFactory(new PropertyValueFactory<Compras, String>("fechaDocumento"));
        colDescCom.setCellValueFactory(new PropertyValueFactory<Compras, String>("descripcionCompra"));
        colTotalDoc.setCellValueFactory(new PropertyValueFactory<Compras, String>("totalDocumento"));
    }

    @FXML
    public void seleccionarElemento() {
        try {
            txtNumDoc.setText(String.valueOf(((Compras) tvCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento()));
            Compras compraSeleccionada = (Compras) tvCompras.getSelectionModel().getSelectedItem();

// Verificar si se seleccionó alguna fila
            if (compraSeleccionada != null) {
                // Obtener la fecha de la compra seleccionada
                String fechaDocumento = compraSeleccionada.getFechaDocumento();

                // Convertir la fecha de String a LocalDate
                LocalDate localDate = LocalDate.parse(fechaDocumento);

                // Establecer la fecha en el JFXDatePicker
                jfxDatePicker.setValue(localDate);
            }

            txtDescDoc.setText((((Compras) tvCompras.getSelectionModel().getSelectedItem()).getDescripcionCompra()));
            txtTotalDoc.setText(String.valueOf((((Compras) tvCompras.getSelectionModel().getSelectedItem()).getTotalDocumento())));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Por favor selecciona una fila válida", "Error", JOptionPane.ERROR_MESSAGE);
        }

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

    public void Agregar() throws SQLException {
        switch (tipoDeOperador) {
            case NINGUNO:
                tvCompras.setDisable(true);
                limpiarControles();
                activarControles();
                agregarIcono.setFill(Color.WHITE);
                agregarIcono.setIcon(FontAwesomeIcons.SAVE);
                btnAgregarC.setText("Guardar");
                eliminarIcono.setFill(Color.WHITE);
                eliminarIcono.setIcon(FontAwesomeIcons.CLOSE);
                btnEliminarC.setText("Cancelar");
                btnEditarC.setDisable(true);
                btnReportesC.setDisable(true);
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
                btnAgregarC.setText("Agregar");
                eliminarIcono.setFill(Color.WHITE);
                eliminarIcono.setIcon(FontAwesomeIcons.TRASH);
                btnEliminarC.setText("Eliminar");
                btnEditarC.setDisable(false);
                btnReportesC.setDisable(false);
                /*regresar de nuevo a sus imagenes originales
                imgAgregar.setImage(new Image("URL"));*/
                tipoDeOperador = operador.NINGUNO;
                tvCompras.setDisable(false);
                break;
        }

    }

    public void guardar() throws SQLException {
        Compras registro = new Compras();
        try {
            int compraID = Integer.parseInt(txtNumDoc.getText());
            if (existeCodigoCompra(compraID)) {
                JOptionPane.showMessageDialog(null, "El ID de la Compra ya existe. Por favor, ingrese uno nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Detener el proceso de guardado
            }
            registro.setNumeroDocumento(compraID);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID de la Compra no puede ser nulo/caracter no numérico", "Error", JOptionPane.ERROR_MESSAGE);
            if (txtNumDoc.getText().equals(0)) {
                int compraID = Integer.parseInt(txtNumDoc.getText());
                PreparedStatement eliminarCompra = Conexion.getInstance().getConexion()
                        .prepareCall("{call sp_eliminarcompra(?)}");
                eliminarCompra.setInt(1, compraID);
                eliminarCompra.execute();
            }
            return;
        }
        if (jfxDatePicker.getValue() != null) {
            // Convertir LocalDate a Date
            Date fechaDocumento = Date.valueOf(jfxDatePicker.getValue());
            registro.setFechaDocumento(fechaDocumento.toString());
        } else {
            // Si no se selecciona ninguna fecha, puedes manejarlo según tus requerimientos
            registro.setFechaDocumento(null);
        }
        registro.setDescripcionCompra(txtDescDoc.getText());
        registro.setTotalDocumento(Double.parseDouble(txtTotalDoc.getText()));

        /*registro.setFechaDocumento(DatePicker(txtFechaDoc.getConverter().fromString("YYYY-MM-DD")));
            registro.set*/
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_agregarcompra(?,?,?,?)}");
            procedimiento.setInt(1, registro.getNumeroDocumento());
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = date.parse(registro.getFechaDocumento());
            Date fechaDocumento = new Date(parsedDate.getTime());
            procedimiento.setDate(2, fechaDocumento);
            procedimiento.setString(3, registro.getDescripcionCompra());
            procedimiento.setDouble(4, registro.getTotalDocumento());
            procedimiento.execute();
            listaCompras.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean existeCodigoCompra(int compraID) {
        for (Compras compras : listaCompras) {
            if (compras.getNumeroDocumento()== compraID) {
                return true;
            }
        }
        return false;
    }

    public void eliminar() {
        switch (tipoDeOperador) {
            case ACTUALIZAR:
                tvCompras.setDisable(false);
                desactivarControles();
                limpiarControles();
                agregarIcono.setFill(Color.WHITE);
                agregarIcono.setIcon(FontAwesomeIcons.PLUS);
                btnAgregarC.setText("Agregar");
                eliminarIcono.setFill(Color.WHITE);
                eliminarIcono.setIcon(FontAwesomeIcons.TRASH);
                btnEliminarC.setText("Eliminar");
                btnEditarC.setDisable(false);
                btnReportesC.setDisable(false);
                /*regresar de nuevo a sus imagenes originales
                imgAgregar.setImage(new Image("URL"));*/
                tipoDeOperador = operador.NINGUNO;
                break;
            default:
                if (tvCompras.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmas la eliminacion del registro?", "Eliminar Compras", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarcompra(?)}");
                            procedimiento.setInt(1, ((Compras) tvCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento());
                            procedimiento.execute();
                            listaCompras.remove(tvCompras.getSelectionModel().getSelectedItem());
                            limpiarControles();
                            cargarDatos();
                            JOptionPane.showMessageDialog(null, "Compra eliminada correctamente");
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
                activarControles();
                tvCompras.setDisable(true);
                if (tvCompras.getSelectionModel().getSelectedItem() != null) {
                    actualizarIcono.setFill(Color.WHITE);
                    actualizarIcono.setIcon(FontAwesomeIcons.SAVE);
                    btnEditarC.setText("Guardar");
                    reporteIcono.setFill(Color.WHITE);
                    reporteIcono.setIcon(FontAwesomeIcons.CLOSE);
                    btnReportesC.setText("Cancelar");
                    btnEliminarC.setDisable(true);
                    btnAgregarC.setDisable(true);
                    txtNumDoc.setEditable(false);
                    tipoDeOperador = operador.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar una fila para editar");
                    tvCompras.setDisable(false);
                }
                break;
            case ACTUALIZAR:
                actualizar();
                actualizarIcono.setFill(Color.WHITE);
                actualizarIcono.setIcon(FontAwesomeIcons.EDIT);
                btnEditarC.setText("Actualizar");
                reporteIcono.setFill(Color.WHITE);
                reporteIcono.setIcon(FontAwesomeIcons.FOLDER);
                btnReportesC.setText("Reportes");
                btnAgregarC.setDisable(false);
                btnEliminarC.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperador = operador.NINGUNO;
                cargarDatos();
                tvCompras.setDisable(false);
                break;
        }
    }

    public void reportes() {
        switch (tipoDeOperador) {
            case NINGUNO:
                break;
            case ACTUALIZAR:
                tvCompras.setDisable(false);
                desactivarControles();
                limpiarControles();
                actualizarIcono.setFill(Color.WHITE);
                actualizarIcono.setIcon(FontAwesomeIcons.EDIT);
                btnEditarC.setText("Actualizar");
                reporteIcono.setFill(Color.WHITE);
                reporteIcono.setIcon(FontAwesomeIcons.FOLDER);
                btnReportesC.setText("Reportes");
                btnAgregarC.setDisable(false);
                btnEliminarC.setDisable(false);
                tipoDeOperador = operador.NINGUNO;
                break;
        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_editarcompra(?,?,?,?)}");
            //Compras registro = new Compras();
            Compras registro = (Compras) tvCompras.getSelectionModel().getSelectedItem();
            if (jfxDatePicker.getValue() != null) {
                // Convertir LocalDate a Date
                Date fechaDocumento = Date.valueOf(jfxDatePicker.getValue());
                registro.setFechaDocumento(fechaDocumento.toString());
            } else {
                // Si no se selecciona ninguna fecha, puedes manejarlo según tus requerimientos
                registro.setFechaDocumento(null);
            }
            registro.setDescripcionCompra(txtDescDoc.getText());
            registro.setTotalDocumento(Double.parseDouble(txtTotalDoc.getText()));
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = date.parse(registro.getFechaDocumento());
            Date fechaDocumento = new Date(parsedDate.getTime());
            procedimiento.setInt(1, registro.getNumeroDocumento());
            procedimiento.setDate(2, fechaDocumento);
            procedimiento.setString(3, registro.getDescripcionCompra());
            procedimiento.setDouble(4, registro.getTotalDocumento());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buscarCompra() {
        limpiarControles();
        FilteredList<Compras> filtro = new FilteredList<>(listaCompras, e -> true);
        txtbuscarCompra.textProperty().addListener((observable, oldValue, newValue) -> {
            filtro.setPredicate(predicateCompra -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String param = newValue.toLowerCase();
                String codigoCompra = String.valueOf(predicateCompra.getNumeroDocumento());
                String fechaDocumento = predicateCompra.getFechaDocumento().toString();
                String totalDocumento = String.valueOf(predicateCompra.getTotalDocumento());

                if (codigoCompra.contains(param)) {
                    return true;
                } else if (fechaDocumento.contains(param)) {
                    return true;
                } else if (predicateCompra.getDescripcionCompra().toLowerCase().contains(param)) {
                    return true;
                } else if (totalDocumento.contains(param)) {
                    return true;
                } else {

                    return false;
                }
            });
        });

        SortedList<Compras> sortList = new SortedList<>(filtro);
        sortList.comparatorProperty().bind(tvCompras.comparatorProperty());
        tvCompras.setItems(sortList);
    }

    public void actualizarIconoMaximizar(boolean isMaximized) {
        if (isMaximized) {
            iconMaximizar.setRotate(180);
        } else {
            iconMaximizar.setRotate(0);
        }
    }

    public void ventana(ActionEvent event) {
        colNumDoc.prefWidthProperty().bind(tvCompras.widthProperty().multiply(0.0476));
        colDescCom.prefWidthProperty().bind(tvCompras.widthProperty().multiply(0.31746));
        colFechaDoc.prefWidthProperty().bind(tvCompras.widthProperty().multiply(0.31746));
        colTotalDoc.prefWidthProperty().bind(tvCompras.widthProperty().multiply(0.31746));
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

    public void desactivarControles() {
        txtNumDoc.setEditable(false);
        jfxDatePicker.setEditable(false);
        txtDescDoc.setEditable(false);
        txtTotalDoc.setEditable(false);
        jfxDatePicker.setDisable(true);
    }

    public void activarControles() {
        txtNumDoc.setEditable(true);
        jfxDatePicker.setEditable(true);
        txtDescDoc.setEditable(true);
        txtTotalDoc.setEditable(true);
        jfxDatePicker.setDisable(false);
    }

    public void limpiarControles() {
        txtNumDoc.clear();
        jfxDatePicker.setValue(null);
        txtDescDoc.clear();
        txtTotalDoc.clear();
        jfxDatePicker.setValue(null);
    }

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresarC) {
            escenarioPrincipal.menuPrincipalView();
        }
        if (event.getSource() == btnDetalleCompra) {
            try {
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                escenarioPrincipal.DetalleCompraView(stage);
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

}
