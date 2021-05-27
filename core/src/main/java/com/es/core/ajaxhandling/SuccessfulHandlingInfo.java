package com.es.core.ajaxhandling;

public class SuccessfulHandlingInfo implements HandlingInfo {

    private String totalQuantity;
    private String totalCost;

    public SuccessfulHandlingInfo(String totalQuantity, String totalCost) {
        this.totalQuantity = totalQuantity;
        this.totalCost = totalCost;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }
}
