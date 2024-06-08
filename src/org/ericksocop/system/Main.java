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
import javafx.scene.Parent;
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

    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("QuickShop");
        menuPrincipalView();
        escenarioPrincipal.getIcons().add(new Image("/org/ericksocop/resource/Isotipo.png"));
        escenarioPrincipal.initStyle(StageStyle.UNDECORATED);
        escenarioPrincipal.show();
    }

    public Initializable cambiarEscena(String fxmlname, Stage parentStage) throws Exception {
        Initializable resultado;
        FXMLLoader loader = new FXMLLoader();
        InputStream file = Main.class.getResourceAsStream("/org/ericksocop/view/" + fxmlname);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource("/org/ericksocop/view/" + fxmlname));
        Parent root = loader.load(file);

        // Verificar si la ventana padre está maximizada
        boolean isMaximized = parentStage.isMaximized();

        // Configurar la nueva escena
        if (isMaximized) {
            parentStage.setScene(new Scene(root, parentStage.getWidth(), parentStage.getHeight()));
            parentStage.setMaximized(true);
        } else {
            // Valores por defecto
            parentStage.setScene(new Scene(root, 1359, 665)); 
        }
        parentStage.sizeToScene();

        resultado = (Initializable) loader.getController();
        return resultado;
    }

    public Initializable cambiarEscena(String fxmlname) throws Exception {
        return cambiarEscena(fxmlname, escenarioPrincipal);
    }

    public void menuPrincipalView() {
        try {
            MenuPrincipalController menuPrincipalView = (MenuPrincipalController) cambiarEscena("MenuPrincipal.fxml");
            menuPrincipalView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void menuClienteView() {
        try {
            ClienteController menuClienteView = (ClienteController) cambiarEscena("Cliente.fxml");
            menuClienteView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            // System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void programadorView() {
        try {
            ProgramadorController prograView = (ProgramadorController) cambiarEscena("Programador.fxml");
            prograView.setEscenarioPrincipal(this);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void cargoEmpleadosView(Stage parentStage) {
        try {
            CargoEmpleadosController cargoView = (CargoEmpleadosController) cambiarEscena("CargoEmpleados.fxml", parentStage);
            cargoView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tipoProductoView(Stage parentStage) {
        try {
            TipoProductoController productoView = (TipoProductoController) cambiarEscena("tipoProducto.fxml", parentStage);
            productoView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ProveedorView() {
        try {
            ProveedorController proveedorView = (ProveedorController) cambiarEscena("Proveedor.fxml");
            proveedorView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CompraView() {
        try {
            ComprasController compraView = (ComprasController) cambiarEscena("Compras.fxml");
            compraView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ProductosView() {
        try {
            ProductosController producView = (ProductosController) cambiarEscena("Productos.fxml");
            producView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DetalleCompraView(Stage parentStage) {
        try {
            DetalleCompraController detalleComView = (DetalleCompraController) cambiarEscena("DetalleCompra.fxml", parentStage);
            detalleComView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void EmpleadosView() {
        try {
            EmpleadosController empleadoView = (EmpleadosController) cambiarEscena("Empleados.fxml");
            empleadoView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void FacturasView() {
        try {
            FacturasController facturasView = (FacturasController) cambiarEscena("Facturas.fxml");
            facturasView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DetalleFacturaView(Stage parentStage) {
        try {
            DetalleFacturaController detalleFView = (DetalleFacturaController) cambiarEscena("DetalleFactura.fxml", parentStage);
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
