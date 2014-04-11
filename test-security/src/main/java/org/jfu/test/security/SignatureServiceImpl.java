package org.jfu.test.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;

public class SignatureServiceImpl extends AbstractSecurityService implements SignatureService {

    @Override
    public byte[] sign(byte[] text) {
        try {
            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initSign(privateKey);
            sig.update(text);
            return sig.sign();
        } catch (NoSuchAlgorithmException
                | InvalidKeyException | SignatureException e) {
            throw new RuntimeException("Sign exception", e);
        }
    }

    @Override
    public boolean verify(byte[] text, byte[] signature) {
        try {
            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initVerify(certificate);
            sig.update(text);
            return sig.verify(signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException("Verify signature exception", e);
        }
    }

}
