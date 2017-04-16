package io.webguru.fieldpickup.ApiHandler.dto;


import java.util.List;

/**
 * Created by mahto on 31/3/17.
 */
public class LoginDataDTO {

    private List<DeviceDataDTO> deviceDataList;

    private String status;

    private Integer numberOfPickups;

    private Long drsId;

    public LoginDataDTO() {
    }

    public LoginDataDTO(List<DeviceDataDTO> deviceDataList, String status, Integer numberOfPickups, Long drsId) {
        this.deviceDataList = deviceDataList;
        this.status = status;
        this.numberOfPickups = numberOfPickups;
        this.drsId = drsId;
    }

    public List<DeviceDataDTO> getDeviceDataList() {
        return deviceDataList;
    }

    public void setDeviceDataList(List<DeviceDataDTO> deviceDataList) {
        this.deviceDataList = deviceDataList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getNumberOfPickups() {
        return numberOfPickups;
    }

    public void setNumberOfPickups(Integer numberOfPickups) {
        this.numberOfPickups = numberOfPickups;
    }

    public Long getDrsId() {
        return drsId;
    }

    public void setDrsId(Long drsId) {
        this.drsId = drsId;
    }

    @Override
    public String toString() {
        return "LoginDataDTO{" +
            "deviceDataList=" + deviceDataList +
            ", status='" + status + '\'' +
            ", numberOfPickups=" + numberOfPickups +
            ", drsId=" + drsId +
            '}';
    }
}
