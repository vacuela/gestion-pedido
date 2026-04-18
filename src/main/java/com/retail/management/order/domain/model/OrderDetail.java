package com.retail.management.order.domain.model;

public class OrderDetail {

    private String itemId;
    private int quantity;
    private String displayName;
    private String canal;
    private String orderStatus;

    public OrderDetail() {
    }

    public OrderDetail(String itemId, int quantity, String displayName, String canal, String orderStatus) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.displayName = displayName;
        this.canal = canal;
        this.orderStatus = orderStatus;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
