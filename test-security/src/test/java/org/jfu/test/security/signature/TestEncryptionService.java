package org.jfu.test.security.signature;

import org.jfu.test.security.ByteHelper;
import org.jfu.test.security.EncryptionService;
import org.jfu.test.security.EncryptionServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestEncryptionService {
    private static Logger logger = LoggerFactory.getLogger(TestEncryptionService.class);

    private EncryptionService encryptionService = new EncryptionServiceImpl();

    @Test
    public void test() {
        String plain = "test";

        String hex = ByteHelper.toHex(plain.getBytes());
        logger.debug("Hex for plain: " + hex);
        byte[] fromHex = ByteHelper.toByteArray(hex);
        logger.debug("Plain from hex: " + new String(fromHex));

        byte[] cipher = encryptionService.encrypt(plain.getBytes());

        byte[] decrpted = encryptionService.decrypt(cipher);

        Assert.assertEquals(plain, new String(decrpted));

    }
}
