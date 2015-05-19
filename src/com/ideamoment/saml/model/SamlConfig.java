/**
 * 
 */

package com.ideamoment.saml.model;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sun.security.provider.X509Factory;

/**
 * @author Chinakite
 */
public class SamlConfig {

    private static PublicKey   rootPublicKey;       //根证书公钥

    private static PrivateKey  rootPrivateKey;      //根证书私钥

    private static Certificate cert;                //根证书

    private static String      certstr;             //根证书串

    /**
     * 初始化加载证书
     */
    static{
        Provider p = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
        if(p == null)
            Security.addProvider(new BouncyCastleProvider());
        getRootKeys();
    }
    
    /**
     * 僵尸方法，调用用于触发静态块
     */
    public static void init(){};
    
    /**
     * 获取根证书
     */
    private static void getRootKeys() {
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance("RSA", "BC");
            kpg.initialize(1024);
            KeyPair keyPair = kpg.generateKeyPair();
            KeyStore store = KeyStore.getInstance("JKS");
            InputStream in = SamlConfig.class
                    .getResourceAsStream("/ideasaml.jks");

            store.load(in, "ideasaml".toCharArray());

            rootPrivateKey = (PrivateKey)store.getKey("ideasaml", "ideasaml".toCharArray());

            cert = store.getCertificate("ideasaml");
            rootPublicKey = cert.getPublicKey();

            StringBuffer cstr = new StringBuffer();

            cstr.append(X509Factory.BEGIN_CERT + "\n");
            Base64 base64hendle = new Base64();
            cstr.append(new String(base64hendle.encode(cert.getEncoded())));
            cstr.append("\n");
            cstr.append(X509Factory.END_CERT);
            certstr = cstr.toString();
            System.out.println(certstr);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
    }

    
    /**
     * @return the rootPublicKey
     */
    public static PublicKey getRootPublicKey() {
        return rootPublicKey;
    }

    
    /**
     * @param rootPublicKey the rootPublicKey to set
     */
    public static void setRootPublicKey(PublicKey rootPublicKey) {
        SamlConfig.rootPublicKey = rootPublicKey;
    }

    
    /**
     * @return the rootPrivateKey
     */
    public static PrivateKey getRootPrivateKey() {
        return rootPrivateKey;
    }

    
    /**
     * @param rootPrivateKey the rootPrivateKey to set
     */
    public static void setRootPrivateKey(PrivateKey rootPrivateKey) {
        SamlConfig.rootPrivateKey = rootPrivateKey;
    }

    
    /**
     * @return the cert
     */
    public static Certificate getCert() {
        return cert;
    }

    
    /**
     * @param cert the cert to set
     */
    public static void setCert(Certificate cert) {
        SamlConfig.cert = cert;
    }

    
    /**
     * @return the certstr
     */
    public static String getCertstr() {
        return certstr;
    }

    
    /**
     * @param certstr the certstr to set
     */
    public static void setCertstr(String certstr) {
        SamlConfig.certstr = certstr;
    }
}
