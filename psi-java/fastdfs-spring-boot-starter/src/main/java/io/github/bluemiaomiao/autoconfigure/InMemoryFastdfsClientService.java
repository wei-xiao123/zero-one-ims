package io.github.bluemiaomiao.autoconfigure;

import io.github.bluemiaomiao.service.FastdfsClientService;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

final class InMemoryFastdfsClientService implements FastdfsClientService {

    private final Map<String, byte[]> store = new ConcurrentHashMap<>();

    @Override
    public String[] autoUpload(byte[] fileContent, String extName) {
        String group = "group1";
        String storageId = UUID.randomUUID().toString().replace("-", "");
        store.put(key(group, storageId), fileContent == null ? new byte[0] : fileContent);
        return new String[]{group, storageId};
    }

    @Override
    public byte[] download(String group, String storageId) {
        return store.getOrDefault(key(group, storageId), new byte[0]);
    }

    @Override
    public int delete(String group, String storageId) {
        return store.remove(key(group, storageId)) == null ? 1 : 0;
    }

    @Override
    public String autoDownloadWithToken(String group, String storageId, String urlPrefix) {
        return normalize(urlPrefix) + "/" + group + "/" + storageId + "?token=local";
    }

    @Override
    public String autoDownloadWithoutToken(String group, String storageId, String urlPrefix) {
        return normalize(urlPrefix) + "/" + group + "/" + storageId;
    }

    private static String key(String group, String storageId) {
        return group + ":" + storageId;
    }

    private static String normalize(String urlPrefix) {
        if (urlPrefix == null || urlPrefix.isEmpty()) {
            return "http://localhost";
        }
        return urlPrefix.endsWith("/") ? urlPrefix.substring(0, urlPrefix.length() - 1) : urlPrefix;
    }
}
