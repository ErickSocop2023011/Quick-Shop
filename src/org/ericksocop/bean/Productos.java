/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ericksocop.bean;

import java.sql.Blob;

/**
 *
 * @author mauco
 */
public class Productos {
    private int codigoProducto;
    private String descripcionProducto;
    private double precioUnitario;
    private double precioDocena;
    private double precioMayor;
    private Blob imagenProducto;
    private int existencia;
    private int codigoTipoProducto;
    private int codigoProveedor;

    public Productos() {
    }

    public Productos(int codigoProducto, String descripcionProducto, double precioUnitario, double precioDocena, double precioMayor, Blob imagenProducto, int existencia, int codigoTipoProducto, int codigoProveedor) {
        this.codigoProducto = codigoProducto;
        this.descripcionProducto = descripcionProducto;
        this.precioUnitario = precioUnitario;
        this.precioDocena = precioDocena;
        this.precioMayor = precioMayor;
        this.imagenProducto = imagenProducto;
        this.existencia = existencia;
        this.codigoTipoProducto = codigoTipoProducto;
        this.codigoProveedor = codigoProveedor;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getPrecioDocena() {
        return precioDocena;
    }

    public void setPrecioDocena(double precioDocena) {
        this.precioDocena = precioDocena;
    }

    public double getPrecioMayor() {
        return precioMayor;
    }

    public void setPrecioMayor(double precioMayor) {
        this.precioMayor = precioMayor;
    }

    public Blob getImagenProducto() {
        return imagenProducto;
    }

    public void setImagenProducto(Blob imagenProducto) {
        this.imagenProducto = imagenProducto;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    public int getCodigoTipoProducto() {
        return codigoTipoProducto;
    }

    public void setCodigoTipoProducto(int codigoTipoProducto) {
        this.codigoTipoProducto = codigoTipoProducto;
    }

    public int getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(int codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }

    @Override
    public String toString() {
        return getCodigoProducto() + "    |   "  + getDescripcionProducto() + "    |   "  + getPrecioUnitario()+ "    |   "  + getExistencia();
    }
    
    

}