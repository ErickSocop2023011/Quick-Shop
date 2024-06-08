/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package org.ericksocop.system;

import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.ericksocop.controller.CargoEmpleadosController;
import org.ericksocop.controller.ClienteController;
import org.ericksocop.controller.ComprasController;
import org.ericksocop.controller.DetalleCompraController;
import org.ericksocop.controller.DetalleFacturaController;
import org.ericksocop.controller.EmpleadosController;
import org.ericksocop.controller.FacturasController;
import org.ericksocop.controller.MenuPrincipalController;
import org.ericksocop.controller.ProductosController;
import org.ericksocop.controller.ProgramadorController;
import org.ericksocop.controller.ProveedorController;
import org.ericksocop.controller.TipoProductoController;

/**
 *
 * @author mauco
 */
public class Main extends Application {

    private Stage escenarioPrincipal;
    private Scene escena;

    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("QuickShop");
        menuPrincipalView();
        escenarioPrincipal.getIcons().add(new Image("/org/ericksocop/resource/Isotipo.png"));
        // Parent root = FXMLLoader.load(getClass().getResource("/org/ericksocop/view/MenuPrincipalView.fxml"));
        //Scene escena = new Scene(root);
        //escenarioPrincipal.setScene(escena);
        escenarioPrincipal.initStyle(StageStyle.UNDECORATED);
        escenarioPrincipal.show();
    }

    public Initializable cambiarEscena(String fxmlname, int width, int height) throws Exception {
        Initializable resultado;
        FXMLLoader loader = new FXMLLoader();
        InputStream file = Main.class.getResourceAsStream("/org/ericksocop/view/" + fxmlname);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource("/org/ericksocop/view/" + fxmlname));
        escena = new Scene((StackPane) loader.load(file), width, height);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();

        resultado = (Initializable) loader.getController();

        return resultado;
    }

    public void menuPrincipalView() {
        try {
            MenuPrincipalController menuPrincipalView = (MenuPrincipalController) cambiarEscena("MenuPrincipal.fxml", 1359, 665);
            menuPrincipalView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void menuClienteView() {
        try {
            ClienteController menuClienteView = (ClienteController) cambiarEscena("Cliente.fxml", 1359, 665);
            menuClienteView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            //System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void programadorView() {
        try {
            ProgramadorController prograView = (ProgramadorController) cambiarEscena("Programador.fxml", 1359, 300);
            prograView.setEscenarioPrincipal(this);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void cargoEmpleadosView() {
        try {
            CargoEmpleadosController cargoView = (CargoEmpleadosController) cambiarEscena("CargoEmpleados.fxml", 1359, 665);
            cargoView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tipoProductoView() {
        try {
            TipoProductoController productoView = (TipoProductoController) cambiarEscena("tipoProducto.fxml", 1359, 665);
            productoView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ProveedorView() {
        try {
            ProveedorController proveedorView = (ProveedorController) cambiarEscena("Proveedor.fxml", 1359, 665);
            proveedorView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CompraView() {
        try {
            ComprasController compraView = (ComprasController) cambiarEscena("Compras.fxml", 1359, 665);
            compraView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ProductosView() {
        try {
            ProductosController producView = (ProductosController) cambiarEscena("Productos.fxml", 1359, 665);
            producView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DetalleCompra() {
        try {
            DetalleCompraController detalleComView = (DetalleCompraController) cambiarEscena("DetalleCompra.fxml", 1359, 665);
            detalleComView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void EmpleadosView() {
        try {
            EmpleadosController empleadoView = (EmpleadosController) cambiarEscena("Empleados.fxml", 1359, 665);
            empleadoView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void FacturasView() {
        try {
            FacturasController facturasView = (FacturasController) cambiarEscena("Facturas.fxml", 1359, 665);
            facturasView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DetalleFacturaView() {
        try {
            DetalleFacturaController detalleFView = (DetalleFacturaController) cambiarEscena("DetalleFactura.fxml", 1359, 665);
            detalleFView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
