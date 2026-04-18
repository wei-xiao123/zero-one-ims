package com.zeroone.cloud.oauth2.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SecurityUser implements Serializable {
    private Object extendsObject;
    private String username;
    private String password;
    private List<String> roles = new ArrayList<>();

    public static SecurityUser create(Object extendsObject, String username, String password, List<String> roles) {
        SecurityUser securityUser = new SecurityUser();
        securityUser.setExtendsObject(extendsObject);
        securityUser.setUsername(username);
        securityUser.setPassword(password);
        securityUser.setRoles(roles);
        return securityUser;
    }

    public Object getExtendsObject() {
        return extendsObject;
    }

    public void setExtendsObject(Object extendsObject) {
        this.extendsObject = extendsObject;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
