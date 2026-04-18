package com.anji.captcha.service;

public interface CaptchaCacheService {
    String type();

    void set(String key, String value, long expiresInSeconds);

    boolean exists(String key);

    void delete(String key);

    String get(String key);

    Long increment(String key, long val);
}
