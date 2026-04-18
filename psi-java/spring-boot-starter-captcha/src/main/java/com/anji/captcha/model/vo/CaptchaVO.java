package com.anji.captcha.model.vo;

import java.io.Serializable;

public class CaptchaVO implements Serializable {
    private String captchaVerification;

    public String getCaptchaVerification() {
        return captchaVerification;
    }

    public void setCaptchaVerification(String captchaVerification) {
        this.captchaVerification = captchaVerification;
    }
}
