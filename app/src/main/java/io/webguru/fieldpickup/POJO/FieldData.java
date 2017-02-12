package io.webguru.fieldpickup.POJO;

import java.io.Serializable;

/**
 * Created by yatin on 21/01/17.
 */

public class FieldData{
    private Long id;
    private String isSameProduct;
    private int quantity;
    private String isAllPartsAvailable;
    private String isIssueCategoryCorrect;
    private String isProductDirty;
    private String agentRemarks;
    private Long docketId;
    private String rescheduleDate;
    private String status;

    public FieldData(String isSameProduct, int quantity, String isAllPartsAvailable, String isIssueCategoryCorrect, String isProductDirty, String agentRemarks,Long docketId) {
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

    public String getIsSameProduct() {
        return isSameProduct;
    }

    public void setIsSameProduct(String sameProduct) {
        isSameProduct = sameProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getIsAllPartsAvailable() {
        return isAllPartsAvailable;
    }

    public void setIsAllPartsAvailable(String allPartsAvailable) {
        isAllPartsAvailable = allPartsAvailable;
    }

    public String getIsIssueCategoryCorrect() {
        return isIssueCategoryCorrect;
    }

    public void setIsIssueCategoryCorrect(String issueCategoryCorrect) {
        isIssueCategoryCorrect = issueCategoryCorrect;
    }

    public String getIsProductDirty() {
        return isProductDirty;
    }

    public void setIsProductDirty(String productDirty) {
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

    public String getRescheduleDate() {
        return rescheduleDate;
    }

    public void setRescheduleDate(String rescheduleDate) {
        this.rescheduleDate = rescheduleDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "FieldData{" +
                "id=" + id +
                ", isSameProduct='" + isSameProduct + '\'' +
                ", quantity=" + quantity +
                ", isAllPartsAvailable='" + isAllPartsAvailable + '\'' +
                ", isIssueCategoryCorrect='" + isIssueCategoryCorrect + '\'' +
                ", isProductDirty='" + isProductDirty + '\'' +
                ", agentRemarks='" + agentRemarks + '\'' +
                ", docketId=" + docketId +
                ", rescheduleDate='" + rescheduleDate + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
