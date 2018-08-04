package com.pxy.pangjiao.net;

public class ResponseBase {

    protected boolean isConnectSuccess;
    protected String exception;

    public boolean isConnectSuccess() {
        return isConnectSuccess;
    }

    public void setConnectSuccess(boolean connectSuccess) {
        isConnectSuccess = connectSuccess;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}
