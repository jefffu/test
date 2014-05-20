package org.jfu.test.security.signature;

import java.util.Random;

import org.jfu.test.security.EncryptionService;
import org.jfu.test.security.EncryptionServiceUnsymmetricImpl;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestEncryptionService {
    private static Logger logger = LoggerFactory.getLogger(TestEncryptionService.class);

    private EncryptionService encryptionService = new EncryptionServiceUnsymmetricImpl();

    private Random random = new Random(System.nanoTime());
    private static final String SEEDS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private String generate(int length) {
        String result = "";
        for (int i=0; i<length; i++) {
            random.setSeed(random.nextLong());
            result += SEEDS.charAt(random.nextInt(SEEDS.length()));
        }
        return result;
    }

    @Test
    public void test() {
        String plain = generate(8);

        logger.debug("Plain text: " + plain);
        byte[] cipher = encryptionService.encrypt(plain.getBytes());
        logger.debug("Cipher length: " + cipher.length);

        byte[] decrpted = encryptionService.decrypt(cipher);
        logger.debug("Plain text from cipher: " + new String(decrpted));

        Assert.assertEquals(plain, new String(decrpted));

    }
}
