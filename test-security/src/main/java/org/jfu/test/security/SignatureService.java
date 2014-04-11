package org.jfu.test.security;

public interface SignatureService {

    byte[] sign(byte[] text);

    boolean verify(byte[] text, byte[] signature);
}
