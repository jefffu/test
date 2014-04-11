package org.jfu.test.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;

public class SignatureServiceImpl extends AbstractSecurityService implements SignatureService {

    @Override
    public byte[] sign(byte[] text) {
        try {
            Signature dsa = Signature.getInstance("SHA1withRSA");
            dsa.initSign(privateKey);
            dsa.update(text);
            byte[] realSig = dsa.sign();
            return realSig;
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
