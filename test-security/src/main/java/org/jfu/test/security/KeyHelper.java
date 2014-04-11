package org.jfu.test.security;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Collection;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyHelper {
    private static Logger logger = LoggerFactory.getLogger(KeyHelper.class);

    public static PrivateKey readPrivateKey(String keyStoreFile,
            String storePass, String storeType, String keyAlias, String keyPass) {
        FileInputStream fis = null;
        try {

            KeyStore ks = KeyStore.getInstance(storeType);

            fis = new FileInputStream(keyStoreFile);
            ks.load(fis, storePass.toCharArray());

            KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(
                    keyPass.toCharArray());

            KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) ks
                    .getEntry(keyAlias, protParam);
            return pkEntry.getPrivateKey();
        } catch (IOException e) {
            logger.error("Read private key exception", e);
        } catch (KeyStoreException e) {
            logger.error("Read private key exception", e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Read private key exception", e);
        } catch (CertificateException e) {
            logger.error("Read private key exception", e);
        } catch (UnrecoverableEntryException e) {
            logger.error("Read private key exception", e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.error("Close File to "+keyStoreFile+" exception", e);
                }
            }
        }
        return null;
    }

    public static Certificate readCertificate(String certFile) {
        FileInputStream fis = null;
        Certificate cert = null;
        try {
            fis = new FileInputStream(certFile);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Collection<?> c = cf.generateCertificates(fis);
            Iterator<?> i = c.iterator();

            if (i.hasNext()) {
                cert = (Certificate) i.next();
            }
        } catch (FileNotFoundException e) {
            logger.error("Read certificate exception", e);
        } catch (CertificateException e) {
            logger.error("Read certificate exception", e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.error("Close File to "+certFile+" exception", e);
                }
            }
        }
        return cert;
    }
}
