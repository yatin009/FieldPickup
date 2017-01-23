package io.webguru.fieldpickup.POJO;

import java.io.Serializable;

/**
 * Created by yatin on 21/01/17.
 */

public class Docket implements Serializable{
    private boolean isPending;
    private String docketNumber;
    private String custoumerAddress;
    private String custoumerContact;
    private FieldData fieldData;
    private String customerName;

    public Docket(boolean isPending, String docketNumber, String custoumerAddress, String custoumerContact,String customerName) {
        this.isPending = isPending;
        this.docketNumber = docketNumber;
        this.custoumerAddress = custoumerAddress;
        this.custoumerContact = custoumerContact;
        this.customerName = customerName;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
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
}
