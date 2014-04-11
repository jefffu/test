package org.jfu.test.security;

import java.security.PrivateKey;
import java.security.cert.Certificate;

/**
 * Step 1 Generate keystore
 *   keytool -genkeypair -alias test -keysize 1024 -keypass 123456
 *     -keystore /tmp/keystore -storepass 654321 -storetype jks
 *     -keyalg RSA -validity 36500
 * Step 2 Export certificate from the keystore
 * keytool -exportcert -keystore /tmp/keystore -storepass 654321
 *   -storetype jks -alias test -file /tmp/test.cer
 *
 * @author jefffu
 *
 */
public abstract class AbstractSecurityService {

    protected PrivateKey privateKey;

    protected Certificate certificate;

    AbstractSecurityService() {
        String keyStoreFile = getClass().getClassLoader()
                .getResource("org/jfu/test/security/keystore").getPath();
        String certFile = getClass().getClassLoader()
                .getResource("org/jfu/test/security/test.cer").getPath();

        this.privateKey = KeyHelper.readPrivateKey(keyStoreFile, "654321",
                "jks", "test", "123456");
        this.certificate = KeyHelper.readCertificate(certFile);

        if (privateKey == null) {
            throw new RuntimeException("Private key shouldn't be null!");
        }
        if (certificate == null) {
            throw new RuntimeException("Certificate shouldn't be null!");
        }
    }

}
