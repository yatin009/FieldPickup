package io.webguru.fieldpickup.ApiHandler.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.webguru.fieldpickup.POJO.Product;

/**
 * Created by mahto on 16/4/17.
 */
public class DeviceDataDTO {

    private String docketNumber;
    private String customerName;
    private String contactNumber;
    private String address;
    private ArrayList<Product> products;
    private String pinCode;

    public DeviceDataDTO() {
    }

    public DeviceDataDTO(String docketNumber, String customerName, String contactNumber, String address, ArrayList<Product> products, String pinCode) {
        this.docketNumber = docketNumber;
        this.customerName = customerName;
        this.contactNumber = contactNumber;
        this.address = address;
        this.products = products;
        this.pinCode = pinCode;
    }



    public String getDocketNumber() {
        return docketNumber;
    }

    public void setDocketNumber(String docketNumber) {
        this.docketNumber = docketNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }
}
