package com.zeroone.cloud.starter.gateway.service;

import java.util.List;

public interface LoadResourcesData {
    List<String> loadRolesByUrlPath(String url);
}
