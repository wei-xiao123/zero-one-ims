package com.anji.captcha.model.common;

import java.io.Serializable;

public class ResponseModel implements Serializable {
    private boolean success;
    private String repCode;
    private String repMsg;
    private Object data;

    public static ResponseModel errorMsg(String msg) {
        ResponseModel model = new ResponseModel();
        model.setSuccess(false);
        model.setRepCode("6111");
        model.setRepMsg(msg);
        return model;
    }

    public static ResponseModel success(Object data) {
        ResponseModel model = new ResponseModel();
        model.setSuccess(true);
        model.setRepCode("0000");
        model.setRepMsg("success");
        model.setData(data);
        return model;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getRepCode() {
        return repCode;
    }

    public void setRepCode(String repCode) {
        this.repCode = repCode;
    }

    public String getRepMsg() {
        return repMsg;
    }

    public void setRepMsg(String repMsg) {
        this.repMsg = repMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
