package io.github.jotabrc.security;

import io.github.jotabrc.handler.AccessDeniedException;
import io.github.jotabrc.handler.AuthenticationDeniedException;
import io.github.jotabrc.handler.UnauthorizedException;
import io.github.jotabrc.util.PasswordUtil;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ApplicationContextHolderImpl implements ApplicationContextHolder {

    private final ApplicationContext applicationContext;

    public ApplicationContextHolderImpl() {
        this.applicationContext = ApplicationContext.getInstance();
    }

    @Override
    public void auth(String password, String salt, String hash) throws NoSuchAlgorithmException {
        String hashed = PasswordUtil.getHash(password, salt);
        if (!hashed.equals(hash)) throw new AuthenticationDeniedException("Authentication denied");
    }

    @Override
    public void setContext(final String userUuid) {
        applicationContext
                .setUserUuid(userUuid)
                .setExpiration(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()));
    }

    @Override
    public String getContextDetail() {
        return applicationContext.getUserUuid()
                .orElseThrow(() -> new AccessDeniedException("Access denied"));
    }

    @Override
    public void hasAuthorization(final String userUuid) {
        authIsActive();
        boolean isEqual = getContextDetail().equals(userUuid);
        if (!isEqual) throw new UnauthorizedException("Access denied");
    }

    @Override
    public void authIsActive() {
        applicationContext.checkExpiration();
    }
}
