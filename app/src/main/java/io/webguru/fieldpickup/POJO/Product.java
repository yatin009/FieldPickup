package io.webguru.fieldpickup.POJO;

import java.io.Serializable;
import java.util.List;

import io.webguru.fieldpickup.ApiHandler.dto.QcQuestionDTO;

/**
 * Created by yatin on 20/03/17.
 */

public class Product implements Serializable{
    private int id;
    private FieldData fieldData;
    private String description;
    private String reason;
    private Integer quantity;
    private String productId;
    private List<QcQuestionDTO> qcQuestions;

    public Product(int id, FieldData fieldData, String description, String reason, Integer quantity, String productId, List<QcQuestionDTO> qcQuestions) {
        this.id = id;
        this.fieldData = fieldData;
        this.description = description;
        this.reason = reason;
        this.quantity = quantity;
        this.productId = productId;
        this.qcQuestions = qcQuestions;
    }

    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FieldData getFieldData() {
        return fieldData;
    }

    public void setFieldData(FieldData fieldData) {
        this.fieldData = fieldData;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public List<QcQuestionDTO> getQcQuestions() {
        return qcQuestions;
    }

    public void setQcQuestions(List<QcQuestionDTO> qcQuestions) {
        this.qcQuestions = qcQuestions;
    }
}
