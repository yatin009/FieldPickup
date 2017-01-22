package io.webguru.fieldpickup.POJO;

import java.io.Serializable;

/**
 * Created by yatin on 21/01/17.
 */

public class FieldData implements Serializable{
    private boolean sameProduct;
    private int quantity;
    private boolean accessory;
    private boolean issueCateogary;
    private boolean productUsed;
    private String agentRemark;

    public FieldData(boolean sameProduct, int quantity, boolean accessory, boolean issueCateogary, boolean productUsed, String agentRemark) {
        this.sameProduct = sameProduct;
        this.quantity = quantity;
        this.accessory = accessory;
        this.issueCateogary = issueCateogary;
        this.productUsed = productUsed;
        this.agentRemark = agentRemark;
    }

    public boolean isSameProduct() {
        return sameProduct;
    }

    public void setSameProduct(boolean sameProduct) {
        this.sameProduct = sameProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isAccessory() {
        return accessory;
    }

    public void setAccessory(boolean accessory) {
        this.accessory = accessory;
    }

    public boolean issueCateogary() {
        return issueCateogary;
    }

    public void setIssueCateogary(boolean issueCateogary) {
        this.issueCateogary = issueCateogary;
    }

    public boolean isProductUsed() {
        return productUsed;
    }

    public void setProductUsed(boolean productUsed) {
        this.productUsed = productUsed;
    }

    public String getAgentRemark() {
        return agentRemark;
    }

    public void setAgentRemark(String agentRemark) {
        this.agentRemark = agentRemark;
    }
}
