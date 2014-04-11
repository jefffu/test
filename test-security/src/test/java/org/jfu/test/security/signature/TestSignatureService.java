package org.jfu.test.security.signature;

import org.jfu.test.security.SignatureService;
import org.jfu.test.security.SignatureServiceImpl;
import org.junit.Assert;
import org.junit.Test;

public class TestSignatureService {

    private SignatureService signatureService = new SignatureServiceImpl();

    @Test
    public void test() {
        byte[] text = "test".getBytes();
        byte[] text2 = "test2".getBytes();

        byte[] signature = signatureService.sign(text);

        Assert.assertTrue(signatureService.verify(text, signature));

        Assert.assertFalse(signatureService.verify(text2, signature));

        byte[] signature2 = signatureService.sign(text2);
        Assert.assertTrue(signatureService.verify(text2, signature2));

    }
}
