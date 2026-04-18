package com.retail.management.order.infrastructure.persistence.mongodb.document;

public class DeliveryDataDocument {

    private String id;
    private String direccionEnvio;

    public DeliveryDataDocument() {
    }

    public DeliveryDataDocument(String id, String direccionEnvio) {
        this.id = id;
        this.direccionEnvio = direccionEnvio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }
}
