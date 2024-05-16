/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.ericksocop.controller;

import com.jfoenix.controls.JFXDatePicker;
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
import javafx.scene.input.MouseEvent;
import org.ericksocop.bean.Compras;
import org.ericksocop.dao.Conexion;
import org.ericksocop.system.Main;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author mauco
 */
public class ComprasViewController implements Initializable {

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
    private Button btnDetalleCompra;
    @FXML
    private Button btnAgregarC;
    @FXML
    private Button btnEditarC;
    @FXML
    private Button btnEliminarC;
    @FXML
    private Button btnReportesC;
    @FXML
    private Button btnRegresarC;
    @FXML
    private TextField txtNumDoc;
    @FXML
    private JFXDatePicker jfxDatePicker;
    @FXML
    private TextField txtDescDoc;
    @FXML
    private TextField txtTotalDoc;

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
        tvCompras.setItems(getCompra());
        colNumDoc.setCellValueFactory(new PropertyValueFactory<Compras, Integer>("numeroDocumento"));
        colFechaDoc.setCellValueFactory(new PropertyValueFactory<Compras, String>("fechaDocumento"));
        colDescCom.setCellValueFactory(new PropertyValueFactory<Compras, String>("descripcionCompra"));
        colTotalDoc.setCellValueFactory(new PropertyValueFactory<Compras, String>("totalDocumento"));
    }

    public void seleccionarElemento() {
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
                btnAgregarC.setText("Guardar");
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
                btnAgregarC.setText("Agregar");
                btnEliminarC.setText("Eliminar");
                btnEditarC.setDisable(false);
                btnReportesC.setDisable(false);
                /*regresar de nuevo a sus imagenes originales
                imgAgregar.setImage(new Image("URL"));*/
                tipoDeOperador = operador.NINGUNO;
                break;
        }

    }

    public void guardar() {
        Compras registro = new Compras();
        registro.setNumeroDocumento(Integer.parseInt(txtNumDoc.getText()));
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar() {
        switch (tipoDeOperador) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregarC.setText("Agregar");
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un cliente para eliminar");
                }

                break;
        }
    }

    public void editar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                activarControles();
                if (tvCompras.getSelectionModel().getSelectedItem() != null) {
                    btnEditarC.setText("Actualizar");
                    btnReportesC.setText("Cancelar");
                    btnEliminarC.setDisable(true);
                    btnAgregarC.setDisable(true);
                    txtNumDoc.setEditable(false);
                    tipoDeOperador = operador.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un cliente para editar");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditarC.setText("Editar");
                btnReportesC.setText("Reportes");
                btnAgregarC.setDisable(false);
                btnEliminarC.setDisable(false);
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
                btnEditarC.setText("Editar");
                btnReportesC.setText("Reportes");
                btnAgregarC.setDisable(false);
                btnEliminarC.setDisable(false);
                tipoDeOperador = operador.NINGUNO;

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

    public void desactivarControles() {
        txtNumDoc.setEditable(false);
        jfxDatePicker.setEditable(false);
        txtDescDoc.setEditable(false);
        txtTotalDoc.setEditable(false);
    }

    public void activarControles() {
        txtNumDoc.setEditable(true);
        jfxDatePicker.setEditable(true);
        txtDescDoc.setEditable(true);
        txtTotalDoc.setEditable(true);
    }

    public void limpiarControles() {
        txtNumDoc.clear();
        jfxDatePicker.setValue(null);
        txtDescDoc.clear();
        txtTotalDoc.clear();
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
            escenarioPrincipal.DetalleCompra();
        }
    }

}
