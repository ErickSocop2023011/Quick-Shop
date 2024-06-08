/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ericksocop.report;

import java.io.InputStream;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.ericksocop.dao.Conexion;

/**
 *
 * @author mauco
 */
public class GenerarReportes {

    public static void mostrarReportes(String nombreReporte, String titulo, Map parametros) {
        InputStream reporte = GenerarReportes.class.getResourceAsStream(nombreReporte);
        try{
            JasperReport ReporteClientes2 = (JasperReport) JRLoader.loadObject(reporte);
            JasperPrint reporteImpreso = JasperFillManager.fillReport(ReporteClientes2, parametros,Conexion.getInstance().getConexion());
            JasperViewer visor = new JasperViewer(reporteImpreso,false);
            visor.setTitle(titulo);
            visor.setVisible(true);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
/*
    Interface MAP
    HashMap es uno de los objetos que implementa un conjunto de keyValue.
    Tiene un constructor sin parámetros: "New HashMap()" y sú finalidad suele referirse para agrupar
    información en un único objeto
    
    Tiene cierta similitud con la colección de objetos (ArrayList) pero con la diferencia
    que estos no tiene orden
    
    Hash hace referencia a una técnica de organización de archivos, hashing (abierto-cerrado) en la
    que se almacena registro en una dirección que es generada por una función se aplica a la llave del
    
    En Java el HashMap posee un espacio de memoria y cuando se guarda un objeto en dicho espacio
    se determina su dirección aplicando una función a la llave que le indiquemos. Cada objeto se identifica
    mediante algún identificador apropiado.
     */
