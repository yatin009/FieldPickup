package io.webguru.fieldpickup.POJO;

import java.io.Serializable;

/**
 * Created by yatin on 21/01/17.
 */

public class Docket implements Serializable{
    private long id;
    private Integer isPending;
    private Integer isSynced;
    private String docketNumber;
    private String custoumerAddress;
    private String custoumerContact;
    private FieldData fieldData;
    private String customerName;
    private String description;

    public Docket(Integer isPending, String docketNumber, String custoumerAddress, String custoumerContact,String customerName, String description,Integer isSynced) {
        this.isPending = isPending;
        this.docketNumber = docketNumber;
        this.custoumerAddress = custoumerAddress;
        this.custoumerContact = custoumerContact;
        this.customerName = customerName;
        this.description = description;
        this.isSynced = isSynced;
    }

    public Docket() {
    }

    public Integer isPending() {
        return isPending;
    }

    public void setPending(Integer pending) {
        isPending = pending;
    }

    public String getDocketNumber() {
        return docketNumber;
    }

    public void setDocketNumber(String docketNumber) {
        this.docketNumber = docketNumber;
    }

    public String getCustoumerAddress() {
        return custoumerAddress;
    }

    public void setCustoumerAddress(String custoumerAddress) {
        this.custoumerAddress = custoumerAddress;
    }

    public String getCustoumerContact() {
        return custoumerContact;
    }

    public void setCustoumerContact(String custoumerContact) {
        this.custoumerContact = custoumerContact;
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

    @Override
    public String toString() {
        return "Docket{" +
                "id=" + id +
                ", isPending=" + isPending +
                ", isSynced=" + isSynced +
                ", docketNumber='" + docketNumber + '\'' +
                ", custoumerAddress='" + custoumerAddress + '\'' +
                ", custoumerContact='" + custoumerContact + '\'' +
                ", fieldData=" + fieldData +
                ", customerName='" + customerName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
