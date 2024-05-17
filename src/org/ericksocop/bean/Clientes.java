/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ericksocop.bean;

/**
 *
 * @author mauco
 */
public class Clientes {

    private int clienteID;
    private String nombreClientes;
    private String apellidoClientes;
    private String direccionClientes;
    private String NITClientes;
    private String telefonoClientes;
    private String correoClientes;

    public Clientes() {
    }

    public Clientes(int clienteID, String nombreClientes, String apellidoClientes, String direccionClientes, String NITClientes, String telefonoClientes, String correoClientes) {
        this.clienteID = clienteID;
        this.nombreClientes = nombreClientes;
        this.apellidoClientes = apellidoClientes;
        this.NITClientes = NITClientes;
        this.telefonoClientes = telefonoClientes;
        this.direccionClientes = direccionClientes;
        this.correoClientes = correoClientes;
    }

    public int getClienteID() {
        return clienteID;
    }

    public void setClienteID(int clienteID) {
        this.clienteID = clienteID;
    }

    public String getNombreClientes() {
        return nombreClientes;
    }

    public void setNombreClientes(String nombreClientes) {
        this.nombreClientes = nombreClientes;
    }

    public String getApellidoClientes() {
        return apellidoClientes;
    }

    public void setApellidoClientes(String apellidoClientes) {
        this.apellidoClientes = apellidoClientes;
    }

    public String getDireccionClientes() {
        return direccionClientes;
    }

    public void setDireccionClientes(String direccionClientes) {
        this.direccionClientes = direccionClientes;
    }

    public String getNITClientes() {
        return NITClientes;
    }

    public void setNITClientes(String NITClientes) {
        this.NITClientes = NITClientes;
    }

    public String getTelefonoClientes() {
        return telefonoClientes;
    }

    public void setTelefonoClientes(String telefonoClientes) {
        this.telefonoClientes = telefonoClientes;
    }

    public String getCorreoClientes() {
        return correoClientes;
    }

    public void setCorreoClientes(String correoClientes) {
        this.correoClientes = correoClientes;
    }

    @Override
    public String toString() {
        return getNombreClientes()+ "    |   " + getNITClientes() ;
    }

    
}
