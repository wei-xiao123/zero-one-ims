package io.github.bluemiaomiao.service;

public interface FastdfsClientService {
    String[] autoUpload(byte[] fileContent, String extName) throws Exception;

    byte[] download(String group, String storageId) throws Exception;

    int delete(String group, String storageId) throws Exception;

    String autoDownloadWithToken(String group, String storageId, String urlPrefix) throws Exception;

    String autoDownloadWithoutToken(String group, String storageId, String urlPrefix) throws Exception;
}
