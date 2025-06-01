package io.github.jotabrc.util;

import io.github.jotabrc.dto.UserRegisterDto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {

    public static String getEncodedSalt(final byte[] salt) throws NoSuchAlgorithmException {
        return Base64.getEncoder().encodeToString(salt);
    }

    public static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[32];
        sr.nextBytes(salt);
        return salt;
    }

    public static String getHash(final String password, final byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }

    public static String getHash(final String password, final String salt) throws NoSuchAlgorithmException {
        byte[] saltByte = Base64.getDecoder().decode(salt);
        return getHash(password, saltByte);
    }

    public static SaltAndHash getSaltAndHash(final UserRegisterDto dto) throws NoSuchAlgorithmException {
        byte[] salt = PasswordUtil.getSalt();
        String encodedSalt = PasswordUtil.getEncodedSalt(salt);
        String hash = PasswordUtil.getHash(dto.getPassword(), salt);
        return new SaltAndHash(encodedSalt, hash);
    }

    public record SaltAndHash(String encodedSalt, String hash) {}
}
