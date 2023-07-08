package com.rehman.womansecuritysystem.Model;

public class RequestModel
{
    String requestKey;
    String driverUsername;
    String requestMessage;
    String requestStatus;
    String accountType;

    public RequestModel(String requestKey, String driverUsername, String requestMessage, String requestStatus, String accountType) {
        this.requestKey = requestKey;
        this.driverUsername = driverUsername;
        this.requestMessage = requestMessage;
        this.requestStatus = requestStatus;
        this.accountType = accountType;
    }

    public RequestModel() {
    }

    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }

    public String getDriverUsername() {
        return driverUsername;
    }

    public void setDriverUsername(String driverUsername) {
        this.driverUsername = driverUsername;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
