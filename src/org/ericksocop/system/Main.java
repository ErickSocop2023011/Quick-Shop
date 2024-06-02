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
import org.ericksocop.controller.CargoEmpleadosViewController;
import org.ericksocop.controller.ClienteVistaController;
import org.ericksocop.controller.ComprasViewController;
import org.ericksocop.controller.DetalleCompraViewController;
import org.ericksocop.controller.DetalleFacturaViewController;
import org.ericksocop.controller.EmpleadosViewController;
import org.ericksocop.controller.FacturasViewController;
import org.ericksocop.controller.MenuPrincipalController;
import org.ericksocop.controller.ProductosViewController;
import org.ericksocop.controller.PrograViewController;
import org.ericksocop.controller.ProveedorViewController;
import org.ericksocop.controller.TipoProductoViewController;

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
            ClienteVistaController menuClienteView = (ClienteVistaController) cambiarEscena("ClienteVista.fxml", 1034, 582);
            menuClienteView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            //System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void programadorView() {
        try {
            PrograViewController prograView = (PrograViewController) cambiarEscena("PrograView.fxml", 600, 300);
            prograView.setEscenarioPrincipal(this);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void cargoEmpleadosView() {
        try {
            CargoEmpleadosViewController cargoView = (CargoEmpleadosViewController) cambiarEscena("CargoEmpleadosView.fxml", 1034, 582);
            cargoView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tipoProductoView() {
        try {
            TipoProductoViewController productoView = (TipoProductoViewController) cambiarEscena("tipoProductoView.fxml", 1034, 582);
            productoView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ProveedorView() {
        try {
            ProveedorViewController proveedorView = (ProveedorViewController) cambiarEscena("ProveedorView.fxml", 1181, 665);
            proveedorView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CompraView() {
        try {
            ComprasViewController compraView = (ComprasViewController) cambiarEscena("ComprasView.fxml", 1034, 582);
            compraView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ProductosView() {
        try {
            ProductosViewController producView = (ProductosViewController) cambiarEscena("ProductosView.fxml", 1181, 665);
            producView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DetalleCompra() {
        try {
            DetalleCompraViewController detalleComView = (DetalleCompraViewController) cambiarEscena("DetalleCompraView.fxml", 1181, 665);
            detalleComView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void EmpleadosView() {
        try {
            EmpleadosViewController empleadoView = (EmpleadosViewController) cambiarEscena("Empleados.fxml", 1359, 665);
            empleadoView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void FacturasView() {
        try {
            FacturasViewController facturasView = (FacturasViewController) cambiarEscena("FacturasView.fxml", 1181, 665);
            facturasView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DetalleFacturaView() {
        try {
            DetalleFacturaViewController detalleFView = (DetalleFacturaViewController) cambiarEscena("DetalleFacturaView.fxml", 1181, 665);
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
