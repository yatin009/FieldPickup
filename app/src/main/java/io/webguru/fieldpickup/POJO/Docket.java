package io.webguru.fieldpickup.POJO;

import java.io.Serializable;

/**
 * Created by yatin on 21/01/17.
 */

public class Docket implements Serializable{
    private long id;
    private Integer isPending;
    private Integer isSynced;
    private String awbNumber;
    private String customerAddress;
    private String customerContact;
    private FieldData fieldData;
    private String customerName;
    private String description;
    private String reason;
    private String pincode;
    private Integer quantity;
    private String orderNumber;


    public Docket(Integer isPending, String awbNumber, String customerAddress, String customerContact,
                  String customerName, String description, String reason, String pincode, Integer isSynced,
                  Integer quantity, String orderNumber) {
        this.isPending = isPending;
        this.awbNumber = awbNumber;
        this.customerAddress = customerAddress;
        this.customerContact = customerContact;
        this.customerName = customerName;
        this.description = description;
        this.isSynced = isSynced;
        this.reason = reason;
        this.pincode = pincode;
        this.quantity = quantity;
        this.orderNumber = orderNumber;
    }

    public Docket() {
    }

    public Integer isPending() {
        return isPending;
    }

    public void setPending(Integer pending) {
        isPending = pending;
    }

    public String getAwbNumber() {
        return awbNumber;
    }

    public void setAwbNumber(String awbNumber) {
        this.awbNumber = awbNumber;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public FieldData getFieldData() {
        return fieldData;
    }

    public void setFieldData(FieldData fieldData) {
        this.fieldData = fieldData;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(Integer isSynced) {
        this.isSynced = isSynced;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", isPending=" + isPending +
                ", isSynced=" + isSynced +
                ", awbNumber='" + awbNumber + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", customerContact='" + customerContact + '\'' +
                ", fieldData=" + fieldData +
                ", customerName='" + customerName + '\'' +
                ", description='" + description + '\'' +
                ", reason='" + reason + '\'' +
                ", pincode='" + pincode + '\'' +
                ", quantity='" + quantity + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                '}';
    }
}
