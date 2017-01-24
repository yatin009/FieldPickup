package io.webguru.fieldpickup.POJO;

import java.io.Serializable;

/**
 * Created by yatin on 21/01/17.
 */

public class FieldData implements Serializable{
    private Long id;
    private boolean isSameProduct;
    private int quantity;
    private boolean isAllPartsAvailable;
    private boolean isIssueCategoryCorrect;
    private boolean isProductDirty;
    private String agentRemarks;
    private Long docketId;

    public FieldData(boolean isSameProduct, int quantity, boolean isAllPartsAvailable, boolean isIssueCategoryCorrect, boolean isProductDirty, String agentRemarks,Long docketId) {
        this.isSameProduct = isSameProduct;
        this.quantity = quantity;
        this.isAllPartsAvailable = isAllPartsAvailable;
        this.isIssueCategoryCorrect = isIssueCategoryCorrect;
        this.isProductDirty = isProductDirty;
        this.agentRemarks = agentRemarks;
        this.docketId = docketId;
    }

    public FieldData() {
    }

    public boolean isSameProduct() {
        return isSameProduct;
    }

    public void setSameProduct(boolean sameProduct) {
        isSameProduct = sameProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isAllPartsAvailable() {
        return isAllPartsAvailable;
    }

    public void setAllPartsAvailable(boolean allPartsAvailable) {
        isAllPartsAvailable = allPartsAvailable;
    }

    public boolean issueCategoryCorrect() {
        return isIssueCategoryCorrect;
    }

    public void setIssueCategoryCorrect(boolean issueCategoryCorrect) {
        isIssueCategoryCorrect = issueCategoryCorrect;
    }

    public boolean isProductDirty() {
        return isProductDirty;
    }

    public void setProductDirty(boolean productDirty) {
        isProductDirty = productDirty;
    }

    public String getAgentRemarks() {
        return agentRemarks;
    }

    public void setAgentRemarks(String agentRemarks) {
        this.agentRemarks = agentRemarks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocketId() {
        return docketId;
    }

    public void setDocketId(Long docketId) {
        this.docketId = docketId;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", isSameProduct=" + isSameProduct +
                ", quantity=" + quantity +
                ", isAllPartsAvailable=" + isAllPartsAvailable +
                ", isIssueCategoryCorrect=" + isIssueCategoryCorrect +
                ", isProductDirty=" + isProductDirty +
                ", agentRemarks='" + agentRemarks + '\'' +
                ", docketId=" + docketId +
                '}';
    }
}
