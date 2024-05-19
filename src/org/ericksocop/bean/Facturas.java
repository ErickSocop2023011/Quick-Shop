/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ericksocop.bean;

/**
 *
 * @author mauco
 */
public class Facturas {
    private int numeroDeFactura;
    private String estado;
    private double totalFactura;
    private String fechaFactura;
    private int clienteID;
    private int codigoEmpleado;

    public Facturas() {
    }

    public Facturas(int numeroDeFactura, String estado, double totalFactura, String fechaFactura, int clienteID, int codigoEmpleado) {
        this.numeroDeFactura = numeroDeFactura;
        this.estado = estado;
        this.totalFactura = totalFactura;
        this.fechaFactura = fechaFactura;
        this.clienteID = clienteID;
        this.codigoEmpleado = codigoEmpleado;
    }

    public int getNumeroDeFactura() {
        return numeroDeFactura;
    }

    public void setNumeroDeFactura(int numeroDeFactura) {
        this.numeroDeFactura = numeroDeFactura;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getTotalFactura() {
        return totalFactura;
    }

    public void setTotalFactura(double totalFactura) {
        this.totalFactura = totalFactura;
    }

    public String getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(String fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public int getClienteID() {
        return clienteID;
    }

    public void setClienteID(int clienteID) {
        this.clienteID = clienteID;
    }

    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(int codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    @Override
    public String toString() {
        return getNumeroDeFactura()+ "    |   " + getEstado() + "    |   " + getTotalFactura();
    }
    
    
}
