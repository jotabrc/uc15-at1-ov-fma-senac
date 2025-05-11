package io.github.jotabrc.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.NONE)
public class ApplicationContext {

    private String userUuid;
    private ZonedDateTime expiration;
    private static ApplicationContext context;

    public static ApplicationContext getInstance() {
        if (context == null) context = new ApplicationContext();
        return context;
    }

    public Optional<String> getUserUuid() {
        return Optional.ofNullable(this.userUuid);
    }

    public ApplicationContext setUserUuid(final String uuid) {
        if (this.userUuid == null) {
            this.userUuid = uuid;
        }
        return this;
    }

    public ApplicationContext setExpiration(final ZonedDateTime expiration) {
        if (this.expiration == null) {
            this.expiration = expiration;
        }
        return this;
    }

    public void checkExpiration() {
        if (context == null || expiration == null || this.expiration.isAfter(LocalDateTime.now().atZone(ZoneId.of("UTC"))))
            throw new RuntimeException("Not Authorized, authentication expired");
    }

    public void close() {
        if (context != null)
            context = null;
    }
}
