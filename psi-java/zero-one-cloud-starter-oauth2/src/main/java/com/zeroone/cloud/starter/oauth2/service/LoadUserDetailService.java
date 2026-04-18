package com.zeroone.cloud.starter.oauth2.service;

import com.zeroone.cloud.oauth2.entity.SecurityUser;

public interface LoadUserDetailService {
    SecurityUser loadUserDetailForMgr(String username);

    SecurityUser loadUserDetailForUser(String username);
}
