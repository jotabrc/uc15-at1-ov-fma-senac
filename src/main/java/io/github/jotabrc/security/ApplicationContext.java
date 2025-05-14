package io.github.jotabrc.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor(access = AccessLevel.NONE)
public class ApplicationContext {

    private final AtomicReference<String> userUuid = new AtomicReference<>();
    private final AtomicReference<ZonedDateTime> expiration = new AtomicReference<>();
    private static final ApplicationContext context = new ApplicationContext();

    private ApplicationContext() {}

    public static ApplicationContext getInstance() {
        return context;
    }

    public Optional<String> getUserUuid() {
        return Optional.ofNullable(this.userUuid.get());
    }

    public ApplicationContext setUserUuid(final String uuid) {
        this.userUuid.compareAndSet(null, uuid);
        return this;
    }

    public ApplicationContext setExpiration(final ZonedDateTime expiration) {
        this.expiration.set(expiration);
        return this;
    }

    public void checkExpiration() {
        Optional.ofNullable(this.expiration.get())
                .filter(expiration -> expiration.isAfter(LocalDateTime.now().atZone(ZoneId.systemDefault())))
                .orElseThrow(() -> new RuntimeException("Not Authorized, authentication expired"));
    }

    public void reset() {
        this.userUuid.set(null);
        this.expiration.set(null);
    }
}
