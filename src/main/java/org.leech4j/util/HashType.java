package org.leech4j.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Oleg Marchenko
 */

public enum HashType {

    SHA1;

    public byte[] valueOf(byte[] data) {
        try {
            MessageDigest hasher = MessageDigest.getInstance(name());
            hasher.update(data);
            return hasher.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
