package com.anji.captcha.service;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;

public interface CaptchaService {
    ResponseModel verification(CaptchaVO captchaVO);
}
