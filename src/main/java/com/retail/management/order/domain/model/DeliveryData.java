package com.retail.management.order.domain.model;

import java.util.Objects;

public class DeliveryData {

    private String id;
    private String direccionEnvio;

    public DeliveryData() {
    }

    public DeliveryData(String id, String direccionEnvio) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryData that = (DeliveryData) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
