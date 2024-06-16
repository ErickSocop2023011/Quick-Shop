/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.ericksocop.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.sql.rowset.serial.SerialBlob;
import javax.swing.JOptionPane;
import org.ericksocop.bean.Productos;
import org.ericksocop.bean.Proveedores;
import org.ericksocop.bean.TipoProducto;
import org.ericksocop.dao.Conexion;
import org.ericksocop.report.GenerarReportes;
import org.ericksocop.system.Main;

/**
 * FXML Controller class
 *
 * @author mauco
 */
public class ProductosController implements Initializable {

    @FXML
    private ImageView imageViewProducto;
    @FXML
    private JFXButton btnSeleccionarImagen;
    @FXML
    private TableView tvProductos;
    @FXML
    private JFXButton btnRegresarP;
    @FXML
    private JFXButton btnAgregarPro;
    @FXML
    private JFXButton btnEditarPro;
    @FXML
    private JFXButton btnTipoP;
    @FXML
    private JFXButton btnEliminarPro;
    @FXML
    private JFXButton btnReportesPro;
    @FXML
    private JFXTextField txtCodigoProd;
    @FXML
    private JFXTextField txtDescPro;
    @FXML
    private JFXTextField txtPrecioU;
    @FXML
    private JFXTextField txtPrecioD;
    @FXML
    private JFXTextField txtPrecioM;
    @FXML
    private JFXTextField txtExistencia;
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
    private JFXTextField txtbuscarProducto;
    private Main escenarioPrincipal;
    private ObservableList<Productos> listaProductos;
    private ObservableList<Proveedores> listaProveedores;
    private ObservableList<TipoProducto> listaTipoP;

    private enum operador {
        AGREGRAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    // Enumeración que define los diferentes tipos de operaciones que se pueden realizar
    private operador tipoDeOperador = operador.NINGUNO;

    private File archivoSeleccionado;
    // Variable para almacenar el archivo seleccionado (imagen)

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
        // Ajusta el ancho de las columnas de la tabla de productos
        colCodProd.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.0476));
        colCodProv.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
        colCodTipoProd.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
        colDescProd.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
        colExistencia.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
        colImagenP.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
        colPrecioD.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
        colPrecioM.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
        colPrecioU.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
    }

    public void cargarDatos() {
        // Configura las columnas de la tabla con los datos de los productos
        tvProductos.setItems(getProducto());
        colCodProd.setCellValueFactory(new PropertyValueFactory<Productos, String>("codigoProducto"));
        colDescProd.setCellValueFactory(new PropertyValueFactory<Productos, String>("descripcionProducto"));
        colPrecioU.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioUnitario"));
        colPrecioD.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioDocena"));
        colPrecioM.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioMayor"));
        //colImagenP.setCellValueFactory(new PropertyValueFactory<Productos, Blob>("imagenProducto"));
        colExistencia.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("existencia"));
        colCodTipoProd.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("CodigoTipoProducto"));
        colCodProv.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("codigoProveedor"));
    }

    public void seleccionarElemento() {
        // Método para manejar la selección de un producto en la tabla
        imageViewProducto.setImage(null);
        try {
            Productos productoSeleccionado = (Productos) tvProductos.getSelectionModel().getSelectedItem();
            txtCodigoProd.setText(String.valueOf(productoSeleccionado.getCodigoProducto()));
            txtDescPro.setText(productoSeleccionado.getDescripcionProducto());
            txtPrecioU.setText(String.valueOf(productoSeleccionado.getPrecioUnitario()));
            txtPrecioD.setText(String.valueOf(productoSeleccionado.getPrecioDocena()));
            txtPrecioM.setText(String.valueOf(productoSeleccionado.getPrecioMayor()));
            txtExistencia.setText(String.valueOf(productoSeleccionado.getExistencia()));
            cmbCodigoTipoP.getSelectionModel().select(buscarTipoProducto(productoSeleccionado.getCodigoTipoProducto()));
            cmbCodigoP.getSelectionModel().select(buscarProveedor(productoSeleccionado.getCodigoProveedor()));
            // Carga la imagen del producto si existe
            if (productoSeleccionado.getImagenProducto() != null) {
                Blob imagenBlob = productoSeleccionado.getImagenProducto();
                byte[] imagenBytes = imagenBlob.getBytes(1, (int) imagenBlob.length());
                Image image = byteArrayToImage(imagenBytes);
                imageViewProducto.setImage(image);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Por favor selecciona una fila válida", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public TipoProducto buscarTipoProducto(int codigoTipoProducto) {
        // Método para buscar un tipo de producto por su código
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
        // Método para buscar un proveedor por su código
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
        // Método para obtener la lista de productos desde la base de datos
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

    public ObservableList<Proveedores> getProveedores() {
        // Método para obtener la lista de proveedores desde la base de datos
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

    public ObservableList<TipoProducto> getTipoP() {
        // Método para obtener la lista de tipos de producto desde la base de datos
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

    public void Agregar() throws SQLException {
        switch (tipoDeOperador) {
            case NINGUNO:
                tvProductos.setDisable(true);
                imageViewProducto.setImage(null);
                limpiarControles();
                activarControles();
                txtPrecioD.setDisable(true);
                txtPrecioM.setDisable(true);
                txtPrecioU.setDisable(true);
                agregarIcono.setFill(Color.WHITE);
                agregarIcono.setIcon(FontAwesomeIcons.SAVE);
                btnAgregarPro.setText("Guardar");
                eliminarIcono.setFill(Color.WHITE);
                eliminarIcono.setIcon(FontAwesomeIcons.CLOSE);
                btnEliminarPro.setText("Cancelar");
                btnEditarPro.setDisable(true);
                btnReportesPro.setDisable(true);
                //imgAgregar.setImage(new Image("URL"));
                tipoDeOperador = operador.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                imageViewProducto.setImage(null);
                limpiarControles();
                cargarDatos();
                desactivarControles();
                agregarIcono.setFill(Color.WHITE);
                agregarIcono.setIcon(FontAwesomeIcons.PLUS);
                btnAgregarPro.setText("Agregar");
                eliminarIcono.setFill(Color.WHITE);
                eliminarIcono.setIcon(FontAwesomeIcons.TRASH);
                btnEliminarPro.setText("Eliminar");
                btnEditarPro.setDisable(false);
                btnReportesPro.setDisable(false);
                /*regresar de nuevo a sus imagenes originales
                imgAgregar.setImage(new Image("URL"));*/
                tipoDeOperador = operador.NINGUNO;
                tvProductos.setDisable(false);
                break;
        }
    }

    public void guardar() throws SQLException {
        imageViewProducto.setImage(null);
        Productos registro = new Productos();

        // Verificación del ID del Producto
        try {
            int productoID = Integer.parseInt(txtCodigoProd.getText());
            if (existeCodigoProducto(productoID)) {
                JOptionPane.showMessageDialog(null, "El ID del Producto ya existe. Por favor, ingrese uno nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Detener el proceso de guardado
            }
            registro.setCodigoProducto(productoID);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID del Producto no puede ser nulo/caracter no numérico", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificación del Proveedor ID
        try {
            registro.setCodigoProveedor(((Proveedores) cmbCodigoP.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "El Proveedor ID no puede ser nulo", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificación de la Existencia
        try {
            registro.setExistencia(Integer.parseInt(txtExistencia.getText()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Existencias no puede ser nulo/caracter no númerico", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Verificación del Tipo Producto ID
        try {
            registro.setCodigoTipoProducto(((TipoProducto) cmbCodigoTipoP.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "El Tipo Producto ID no puede ser nulo", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Establecer descripción del producto
        registro.setDescripcionProducto(txtDescPro.getText());

        // Manejo de la imagen del producto
        if (archivoSeleccionado != null) {
            try {
                byte[] imageBytes = leerArchivo(archivoSeleccionado);
                registro.setImagenProducto(new SerialBlob(imageBytes));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            registro.setImagenProducto(null);
        }

        // Establecer precios predeterminados
        registro.setPrecioDocena(0.00);
        registro.setPrecioMayor(0.00);
        registro.setPrecioUnitario(0.00);

        // Ejecutar el procedimiento almacenado para guardar el producto
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_agregarProducto(?,?,?,?,?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getDescripcionProducto());
            procedimiento.setDouble(3, registro.getPrecioUnitario());
            procedimiento.setDouble(4, registro.getPrecioDocena());
            procedimiento.setDouble(5, registro.getPrecioMayor());
            procedimiento.setBlob(6, registro.getImagenProducto());
            procedimiento.setInt(7, registro.getExistencia());
            procedimiento.setInt(8, registro.getCodigoTipoProducto());
            procedimiento.setInt(9, registro.getCodigoProveedor());

            // Guardar el producto solo si el ID del producto no es 0
            if (registro.getCodigoProducto() != 0) {
                procedimiento.execute();
                listaProductos.add(registro);
            } else {
                limpiarControles();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean existeCodigoProducto(int productoID) {
        for (Productos producto : listaProductos) {
            if (producto.getCodigoProducto() == productoID) {
                return true;
            }
        }
        return false;
    }

    public void editar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                tvProductos.setDisable(true);
                if (tvProductos.getSelectionModel().getSelectedItem() != null) {
                    actualizarIcono.setFill(Color.WHITE);
                    actualizarIcono.setIcon(FontAwesomeIcons.SAVE);
                    btnEditarPro.setText("Actualizar");
                    reporteIcono.setFill(Color.WHITE);
                    reporteIcono.setIcon(FontAwesomeIcons.CLOSE);
                    btnReportesPro.setText("Cancelar");
                    btnEliminarPro.setDisable(true);
                    btnAgregarPro.setDisable(true);
                    txtPrecioD.setDisable(true);
                    txtPrecioM.setDisable(true);
                    txtPrecioU.setDisable(true);
                    activarControles();
                    txtCodigoProd.setEditable(false);
                    tipoDeOperador = operador.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar una fila para editar");
                    tvProductos.setDisable(false);
                }
                break;
            case ACTUALIZAR:
                try {
                actualizar();
            } catch (Exception e) {
                e.printStackTrace();
            }
            actualizarIcono.setFill(Color.WHITE);
            actualizarIcono.setIcon(FontAwesomeIcons.EDIT);
            btnEditarPro.setText("Actualizar");
            reporteIcono.setFill(Color.WHITE);
            reporteIcono.setIcon(FontAwesomeIcons.FOLDER);
            btnReportesPro.setText("Reportes");
            btnAgregarPro.setDisable(false);
            btnEliminarPro.setDisable(false);
            desactivarControles();
            limpiarControles();
            tipoDeOperador = operador.NINGUNO;
            tvProductos.setDisable(false);
            break;
        }
    }

    public void actualizar() {
        try {
            Productos registro = (Productos) tvProductos.getSelectionModel().getSelectedItem();
            if (registro == null) {
                return;
            }
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_editarProducto(?,?,?,?,?,?,?,?,?)}");

            registro.setCodigoProveedor(((Proveedores) cmbCodigoP.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            registro.setCodigoTipoProducto((((TipoProducto) cmbCodigoTipoP.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
            registro.setDescripcionProducto(txtDescPro.getText());

            // Validar y actualizar los campos numéricos
            registro.setPrecioDocena(Double.parseDouble(txtPrecioD.getText()));
            registro.setPrecioMayor(Double.parseDouble(txtPrecioM.getText()));
            registro.setPrecioUnitario(Double.parseDouble(txtPrecioU.getText()));

            try {
                registro.setExistencia(Integer.parseInt(txtExistencia.getText()));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Existencias no puede ser nulo/vacío" + "\n"
                        + "Valor Predefinido 1", "Error", JOptionPane.INFORMATION_MESSAGE);
                registro.setExistencia(1);
            }

            // Leer la imagen seleccionada y convertirla a Blob
            if (archivoSeleccionado != null) {
                byte[] imageBytes = leerArchivo(archivoSeleccionado);
                Blob imagenBlob = new SerialBlob(imageBytes);
                registro.setImagenProducto(imagenBlob);
            }

            // Configurar los parámetros del procedimiento almacenado
            procedimiento.setInt(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getDescripcionProducto());
            procedimiento.setDouble(3, registro.getPrecioUnitario());
            procedimiento.setDouble(4, registro.getPrecioDocena());
            procedimiento.setDouble(5, registro.getPrecioMayor());

            if (registro.getImagenProducto() != null) {
                procedimiento.setBlob(6, registro.getImagenProducto());
            } else {
                procedimiento.setNull(6, java.sql.Types.BLOB);
            }

            procedimiento.setInt(7, registro.getExistencia());
            procedimiento.setInt(8, registro.getCodigoTipoProducto());
            procedimiento.setInt(9, registro.getCodigoProveedor());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar() throws SQLException {
        switch (tipoDeOperador) {
            case ACTUALIZAR:
                tvProductos.setDisable(false);
                desactivarControles();
                limpiarControles();
                agregarIcono.setFill(Color.WHITE);
                agregarIcono.setIcon(FontAwesomeIcons.PLUS);
                btnAgregarPro.setText("Agregar");
                eliminarIcono.setFill(Color.WHITE);
                eliminarIcono.setIcon(FontAwesomeIcons.TRASH);
                btnEliminarPro.setText("Eliminar");
                btnEditarPro.setDisable(false);
                btnReportesPro.setDisable(false);
                tipoDeOperador = operador.NINGUNO;
                break;
            default:
                if (tvProductos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirma esta Acción", "Verificación",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            Productos productoSeleccionado = (Productos) tvProductos.getSelectionModel().getSelectedItem();
                            int codigoProducto = productoSeleccionado.getCodigoProducto();

                            // Eliminar las referencias en DetalleCompra
                            PreparedStatement eliminarDetalleCompraStmt = Conexion.getInstance().getConexion()
                                    .prepareCall("{call sp_eliminarDetalleCompraPorProducto(?)}");
                            eliminarDetalleCompraStmt.setInt(1, codigoProducto);
                            eliminarDetalleCompraStmt.execute();

                            // Eliminar el producto
                            PreparedStatement eliminarProductoStmt = Conexion.getInstance().getConexion()
                                    .prepareCall("{call sp_eliminarProducto(?)}");
                            eliminarProductoStmt.setInt(1, codigoProducto);
                            eliminarProductoStmt.execute();

                            listaProductos.remove(productoSeleccionado);
                            limpiarControles();
                            cargarDatos();
                            JOptionPane.showMessageDialog(null, "Producto eliminado correctamente");
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "Error al eliminar el producto: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar una fila para eliminar");
                }
                break;
        }
    }

    public void reportes() {
        switch (tipoDeOperador) {
            case NINGUNO:
                imprimirReporte();
                break;
            case ACTUALIZAR:
                tvProductos.setDisable(false);
                desactivarControles();
                limpiarControles();
                actualizarIcono.setFill(Color.WHITE);
                actualizarIcono.setIcon(FontAwesomeIcons.EDIT);
                btnEditarPro.setText("Actualizar");
                reporteIcono.setFill(Color.WHITE);
                reporteIcono.setIcon(FontAwesomeIcons.FOLDER);
                btnReportesPro.setText("Reportes");
                btnAgregarPro.setDisable(false);
                btnEliminarPro.setDisable(false);
                tipoDeOperador = operador.NINGUNO;
                break;
        }
    }

    public void imprimirReporte() {
        Map parametros = new HashMap();
        parametros.put("codigoProducto", null);
        GenerarReportes.mostrarReportes("reportProducto.jasper", "Reporte de Cliente", parametros);
    }

    public void buscarProducto() {
        limpiarControles();
        // Crea una lista filtrada basada en la lista original de productos
        FilteredList<Productos> filtro = new FilteredList<>(listaProductos, e -> true);
        // Agrega un listener al campo de texto de búsqueda
        txtbuscarProducto.textProperty().addListener((Observable, oldValue, newValue) -> {
            // Configura el predicado para filtrar los productos basado en la nueva entrada de búsqueda
            filtro.setPredicate(predicateProducto -> {
                // Si el nuevo valor es nulo o está vacío, muestra todos los productos
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Convierte la entrada de búsqueda a minúsculas para hacer la búsqueda case-insensitive
                String param = newValue.toLowerCase();
                // Obtiene las propiedades del producto como cadenas
                String productoID = String.valueOf(predicateProducto.getCodigoProducto());
                String precioU = String.valueOf(predicateProducto.getPrecioUnitario());
                String precioD = String.valueOf(predicateProducto.getPrecioDocena());
                String precioM = String.valueOf(predicateProducto.getPrecioMayor());
                String existencia = String.valueOf(predicateProducto.getExistencia());
                String tipoProdID = String.valueOf(predicateProducto.getCodigoTipoProducto());
                String proveedorID = String.valueOf(predicateProducto.getCodigoProveedor());
                // Verifica si alguna de las propiedades del producto contiene el valor de búsqueda
                if (productoID.contains(param)) {
                    return true;
                } else if (predicateProducto.getDescripcionProducto().toLowerCase().contains(param)) {
                    return true;
                } else if (precioU.contains(param)) {
                    return true;
                } else if (precioD.contains(param)) {
                    return true;
                } else if (precioM.contains(param)) {
                    return true;
                } else if (existencia.contains(param)) {
                    return true;
                } else if (tipoProdID.contains(param)) {
                    return true;
                } else if (proveedorID.contains(param)) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        // Crea una lista ordenada basada en la lista filtrada
        SortedList<Productos> sortList = new SortedList<>(filtro);
        // Vincula el comparador de la lista ordenada al comparador de la tabla de vista de productos
        sortList.comparatorProperty().bind(tvProductos.comparatorProperty());
        // Establece la lista ordenada como los elementos de la tabla de vista de productos
        tvProductos.setItems(sortList);
    }

    public void desactivarControles() {
        txtCodigoProd.setEditable(false);
        txtDescPro.setEditable(false);
        txtExistencia.setEditable(false);
        btnSeleccionarImagen.setDisable(true);
        txtPrecioD.setEditable(false);
        txtPrecioM.setEditable(false);
        txtPrecioU.setEditable(false);
        cmbCodigoTipoP.setDisable(true);
        cmbCodigoP.setDisable(true);
        // imageViewProducto.setVisible(false);
    }

    public void activarControles() {
        txtCodigoProd.setEditable(true);
        txtDescPro.setEditable(true);
        txtExistencia.setEditable(true);
        btnSeleccionarImagen.setDisable(false);
        txtPrecioD.setEditable(true);
        txtPrecioM.setEditable(true);
        txtPrecioU.setEditable(true);
        cmbCodigoTipoP.setDisable(false);
        cmbCodigoP.setDisable(false);
        // imageViewProducto.setVisible(true);
    }

    public void limpiarControles() {
        txtCodigoProd.clear();
        txtDescPro.clear();
        txtExistencia.clear();
        //  txtImagenPro.clear();
        txtPrecioD.clear();
        txtPrecioM.clear();
        txtPrecioU.clear();
        cmbCodigoTipoP.setValue(null);
        cmbCodigoP.setValue(null);
        imageViewProducto.setImage(null);
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
            try {
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                escenarioPrincipal.tipoProductoView(stage);
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
        colCodProd.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.0476));
        colCodProv.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
        colCodTipoProd.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
        colDescProd.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
        colExistencia.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
        colImagenP.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
        colPrecioD.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
        colPrecioM.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
        colPrecioU.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));

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

    public void verificacionRotacion() {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        if (stage.isMaximized()) {
            RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.5), iconMaximizar);
            rotateTransition.setByAngle(180);
            rotateTransition.setCycleCount(1);
            rotateTransition.setAutoReverse(false);
            stage.setMaximized(false);
        }
    }

    public void abrirImagen(Stage stage) {
        // Crea un selector de archivos
        FileChooser fileChooser = new FileChooser();
        // Agrega filtros para limitar los tipos de archivos que se pueden seleccionar
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        // Abre el cuadro de diálogo para seleccionar un archivo
        File file = fileChooser.showOpenDialog(stage);
        // Si se seleccionó un archivo, se ejecuta este bloque (en este caso no hace nada)
        if (file != null) {
        }
    }

    public void seleccionarImagen() {
        // Crea un selector de archivos
        FileChooser fileChooser = new FileChooser();
        // Agrega filtros para limitar los tipos de archivos que se pueden seleccionar
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        // Abre el cuadro de diálogo para seleccionar un archivo y guarda la referencia del archivo seleccionado
        archivoSeleccionado = fileChooser.showOpenDialog(new Stage());
        // Si se seleccionó un archivo, se ejecuta este bloque
        if (archivoSeleccionado != null) {
            // Crea una imagen a partir del archivo seleccionado
            Image image = new Image(archivoSeleccionado.toURI().toString());
            // Establece la imagen en el ImageView del producto
            imageViewProducto.setImage(image);
        }
    }

    private byte[] leerArchivo(File file) throws IOException {
        // Abre un FileInputStream para leer el archivo y un ByteArrayOutputStream para almacenar los bytes leídos
        try (FileInputStream fis = new FileInputStream(file); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // Buffer de 1 KB
            byte[] buffer = new byte[1024];
            // Variable para almacenar el número de bytes leídos en cada iteración
            int bytesRead;
            // Lee el archivo en bloques de 1 KB y escribe en el ByteArrayOutputStream
            while ((bytesRead = fis.read(buffer)) != -1) {
                // Retorna el contenido del archivo como un arreglo de bytes
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        }
    }

    private Image byteArrayToImage(byte[] byteArray) {
        // Convierte un arreglo de bytes en una imagen
        ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
        return new Image(bais);
    }
}
