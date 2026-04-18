package com.retail.management.order.infrastructure.persistence.mongodb.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "customers")
public class CustomerDocument {

    @Id
    private String userId;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;

    @Indexed(unique = true)
    private String email;

    private List<DeliveryDataDocument> datosEntrega = new ArrayList<>();

    public CustomerDocument() {
    }

    public CustomerDocument(String userId, String nombre, String apellidoPaterno, String apellidoMaterno, String email) {
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

    public List<DeliveryDataDocument> getDatosEntrega() {
        return datosEntrega;
    }

    public void setDatosEntrega(List<DeliveryDataDocument> datosEntrega) {
        this.datosEntrega = datosEntrega;
    }
}
