package org.jfu.test.security;

public interface EncryptionService {

    byte[] encrypt(byte[] plain);

    byte[] decrypt(byte[] secret);
}
