package com.zeroone.star.login.config;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.service.CaptchaService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaptchaFallbackConfig {

    @Bean
    @ConditionalOnMissingBean(CaptchaService.class)
    public CaptchaService captchaServiceFallback() {
        return captchaVO -> ResponseModel.success(null);
    }
}
