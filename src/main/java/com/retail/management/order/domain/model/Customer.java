package com.retail.management.order.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Customer {

    private String userId;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String email;
    private List<DeliveryData> datosEntrega = new ArrayList<>();

    public Customer() {
    }

    public Customer(String userId, String nombre, String apellidoPaterno, String apellidoMaterno, String email) {
        this.userId = userId;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<DeliveryData> getDatosEntrega() {
        return datosEntrega;
    }

    public void setDatosEntrega(List<DeliveryData> datosEntrega) {
        this.datosEntrega = datosEntrega;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(userId, customer.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId);
    }
}
