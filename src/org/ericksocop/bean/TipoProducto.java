/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ericksocop.bean;

/**
 *
 * @author mauco
 */
public class TipoProducto {
    private int codigoTipoProducto;
    private String descripcionProducto;

    public TipoProducto() {
    }

    public TipoProducto(int codigoTipoProducto, String descripcionProducto) {
        this.codigoTipoProducto = codigoTipoProducto;
        this.descripcionProducto = descripcionProducto;
    }

    public int getCodigoTipoProducto() {
        return codigoTipoProducto;
    }

    public void setCodigoTipoProducto(int codigoTipoProducto) {
        this.codigoTipoProducto = codigoTipoProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDesrcipcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    @Override
    public String toString() {
        return getCodigoTipoProducto() + "    |   " + getDescripcionProducto();
    }
    
    
    
    
}
