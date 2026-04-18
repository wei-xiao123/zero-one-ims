package com.zeroone.cloud.starter.gateway.service;

public interface ResponseDataWrapper {
    Object executeWrap(String code, String message, Object data);
}
