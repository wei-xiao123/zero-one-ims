package com.zeroone.star.login.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.zeroone.cloud.oauth2.entity.Oauth2Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.cert.Certificate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class LocalDevAuthService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${zo.local-auth.keystore-path:../psi-oauth2/src/main/resources/jwt.jks}")
    private String keyStorePath;

    @Value("${zo.local-auth.keystore-password:123456}")
    private String keyStorePassword;

    @Value("${zo.local-auth.key-alias:01star}")
    private String keyAlias;

    @Value("${zo.local-auth.key-password:123456}")
    private String keyPassword;

    @Value("${zo.local-auth.access-expire-seconds:36000}")
    private Long accessExpireSeconds;

    @Value("${zo.local-auth.refresh-expire-seconds:604800}")
    private Long refreshExpireSeconds;

    public Oauth2Token localLogin(String username, String password, String clientId) throws Exception {
        LocalUser user = queryUser(username);
        if (user == null || !passwordEncoder.matches(password, user.password)) {
            throw new IllegalArgumentException("用户名或密码错误");
        }

        List<String> authorities = user.keyword == null || user.keyword.isEmpty()
                ? Collections.emptyList()
                : Collections.singletonList(user.keyword);

        Oauth2Token token = new Oauth2Token();
        token.setToken(buildToken(user, authorities, accessExpireSeconds));
        token.setRefreshToken(buildToken(user, authorities, refreshExpireSeconds));
        token.setTokenHead("Bearer ");
        token.setExpiresIn(accessExpireSeconds.intValue());
        token.setClientId(clientId);
        return token;
    }

    private LocalUser queryUser(String username) {
        String sql = "SELECT u.id, u.user AS username, u.pwd, u.frame, " +
                "COALESCE(f.name, '') AS frame_name, COALESCE(r.keyword, '') AS keyword " +
                "FROM user u " +
                "LEFT JOIN frame f ON f.id = u.frame " +
                "LEFT JOIN role r ON r.id = u.role " +
                "WHERE u.user = ? LIMIT 1";
        List<LocalUser> users = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new LocalUser(
                        rs.getString("id"),
                        rs.getString("username"),
                        rs.getString("pwd"),
                        rs.getString("frame"),
                        rs.getString("frame_name"),
                        rs.getString("keyword")
                ),
                username
        );
        return users.isEmpty() ? null : users.get(0);
    }

    private String buildToken(LocalUser user, List<String> authorities, Long expireSeconds) throws Exception {
        RSAKey rsaKey = loadRsaKey();
        JSONObject payload = new JSONObject();
        payload.set("id", user.id);
        payload.set("user_name", user.username);
        payload.set("authorities", new JSONArray(authorities));
        payload.set("frameId", user.frameId);
        payload.set("frameName", user.frameName);
        payload.set("exp", System.currentTimeMillis() / 1000 + expireSeconds);

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .type(JOSEObjectType.JWT)
                .build();
        JWSObject jwsObject = new JWSObject(header, new Payload(payload.toString()));
        jwsObject.sign(new RSASSASigner(rsaKey.toRSAPrivateKey()));
        return jwsObject.serialize();
    }

    private RSAKey loadRsaKey() throws Exception {
        Path path = Paths.get(keyStorePath);
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (InputStream inputStream = Files.newInputStream(path)) {
            keyStore.load(inputStream, keyStorePassword.toCharArray());
        }
        Certificate certificate = keyStore.getCertificate(keyAlias);
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias, keyPassword.toCharArray());
        return new RSAKey.Builder((RSAPublicKey) certificate.getPublicKey())
                .privateKey((RSAPrivateKey) privateKey)
                .build();
    }

    private static class LocalUser {
        private final String id;
        private final String username;
        private final String password;
        private final String frameId;
        private final String frameName;
        private final String keyword;

        private LocalUser(
                String id,
                String username,
                String password,
                String frameId,
                String frameName,
                String keyword
        ) {
            this.id = id;
            this.username = username;
            this.password = password;
            this.frameId = frameId;
            this.frameName = frameName;
            this.keyword = keyword;
        }
    }
}
