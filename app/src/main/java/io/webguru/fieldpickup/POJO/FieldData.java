package io.webguru.fieldpickup.POJO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yatin on 21/01/17.
 */

public class FieldData implements Serializable{
    private Long id;
    private String agentRemarks;
    private Long docketId;
    private int productId;
    private String rescheduleDate;
    private String status;
    private Integer isQcCleared;
    private List<QcQuestionDetails> qcQuestionDetails;

    public FieldData(String agentRemarks, Long docketId, Integer isQcCleared, int productId, List<QcQuestionDetails> qcQuestionDetails) {
        this.agentRemarks = agentRemarks;
        this.docketId = docketId;
        this.isQcCleared = isQcCleared;
        this.productId = productId;
        this.qcQuestionDetails = qcQuestionDetails;
    }

    public FieldData() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgentRemarks() {
        return agentRemarks;
    }

    public void setAgentRemarks(String agentRemarks) {
        this.agentRemarks = agentRemarks;
    }

    public Long getDocketId() {
        return docketId;
    }

    public void setDocketId(Long docketId) {
        this.docketId = docketId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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

    public Integer getIsQcCleared() {
        return isQcCleared;
    }

    public void setIsQcCleared(Integer isQcCleared) {
        this.isQcCleared = isQcCleared;
    }

    public List<QcQuestionDetails> getQcQuestionDetails() {
        return qcQuestionDetails;
    }

    public void setQcQuestionDetails(List<QcQuestionDetails> qcQuestionDetails) {
        this.qcQuestionDetails = qcQuestionDetails;
    }


    @Override
    public String toString() {
        return "FieldData{" +
                "id=" + id +
                ", agentRemarks='" + agentRemarks + '\'' +
                ", docketId=" + docketId +
                ", productId=" + productId +
                ", rescheduleDate='" + rescheduleDate + '\'' +
                ", status='" + status + '\'' +
                ", isQcCleared=" + isQcCleared +
                ", qcQuestionDetails=" + qcQuestionDetails +
                '}';
    }
}
