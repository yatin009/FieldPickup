package io.webguru.fieldpickup.POJO;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import io.webguru.fieldpickup.ApiHandler.dto.DeviceDataDTO;

/**
 * Created by yatin on 21/01/17.
 */

public class Docket implements Serializable {
    private long id;
    private Integer isPending;
    private Integer isSynced;
    private String awbNumber;
    private String customerAddress;
    private String customerContact;
    private String customerName;
    private String pincode;
    private String orderNumber;
    private ArrayList<Product> products;


    public Docket(long id, Integer isPending, Integer isSynced, String awbNumber, String customerAddress, String customerContact, String customerName, String pincode, String orderNumber, ArrayList<Product> products) {
        this.id = id;
        this.isPending = isPending;
        this.isSynced = isSynced;
        this.awbNumber = awbNumber;
        this.customerAddress = customerAddress;
        this.customerContact = customerContact;
        this.customerName = customerName;
        this.pincode = pincode;
        this.orderNumber = orderNumber;
        this.products = products;
    }

    public Docket() {
    }

    public Docket(DeviceDataDTO deviceDataDTO) {
        this.isPending = 1;
        this.isSynced = 0;
        this.awbNumber = deviceDataDTO.getDocketNumber();
        this.customerAddress = deviceDataDTO.getAddress();
        this.customerContact = deviceDataDTO.getContactNumber();
        this.customerName = deviceDataDTO.getCustomerName();
        this.pincode = deviceDataDTO.getPinCode();
        this.orderNumber = null;
        this.products = deviceDataDTO.getProducts();
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

    public Integer getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(Integer isSynced) {
        this.isSynced = isSynced;
    }


    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getIsPending() {
        return isPending;
    }

    public void setIsPending(Integer isPending) {
        this.isPending = isPending;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public String getProductsStringJson(){
        return new Gson().toJson(products);
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public void setProducts(String products) {
        Type collectionType = new TypeToken<Collection<Product>>(){}.getType();
        this.products = new Gson().fromJson(products, collectionType);
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
                ", customerName='" + customerName + '\'' +
                ", pincode='" + pincode + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                '}';
    }
}
