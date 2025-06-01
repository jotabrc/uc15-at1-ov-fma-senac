package io.github.jotabrc.security;

import java.security.NoSuchAlgorithmException;

public interface ApplicationContextHolder {

    void auth(String password, String salt, String hash) throws NoSuchAlgorithmException;
    void setContext(String userUuid);
    void authIsActive();
    String getContextDetail();
    void hasAuthorization(String userUuid);
}
