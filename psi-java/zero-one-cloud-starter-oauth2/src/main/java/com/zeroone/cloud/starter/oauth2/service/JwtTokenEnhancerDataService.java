package com.zeroone.cloud.starter.oauth2.service;

import com.zeroone.cloud.oauth2.entity.SecurityUser;

import java.util.Map;

public interface JwtTokenEnhancerDataService {
    Map<String, Object> enhance(SecurityUser securityUser);
}
