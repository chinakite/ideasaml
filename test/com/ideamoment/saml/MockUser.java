/**
 * 
 */

package com.ideamoment.saml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import sun.security.provider.X509Factory;

import com.ideamoment.saml.model.SamlUser;

/**
 * @author Chinakite
 */
public class MockUser implements SamlUser {

    private String      id;

    private String      name;

    private PrivateKey  privateKey;

    private String      privateKeyStr;

    private PublicKey   publicKey;

    private String      publicKeyStr;

    private Certificate certificate;

    private String      certificateStr;

    private int         samlNotBefore = 15;

    private int         samlNotAfter  = 30;

    /*
     * (non-Javadoc)
     * @see com.ideamoment.saml.model.IUser#getId()
     */
    @Override
    public String getId() {
        return this.id;
    }

    /*
     * (non-Javadoc)
     * @see com.ideamoment.saml.model.IUser#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }

    /*
     * (non-Javadoc)
     * @see com.ideamoment.saml.model.IUser#getPrivateKey()
     */
    @Override
    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    /*
     * (non-Javadoc)
     * @see com.ideamoment.saml.model.IUser#getPrivateKeyStr()
     */
    @Override
    public String getPrivateKeyStr() {
        return this.privateKeyStr;
    }

    /*
     * (non-Javadoc)
     * @see com.ideamoment.saml.model.IUser#getPublicKey()
     */
    @Override
    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    /*
     * (non-Javadoc)
     * @see com.ideamoment.saml.model.IUser#getPublicKeyStr()
     */
    @Override
    public String getPublicKeyStr() {
        return this.publicKeyStr;
    }

    /*
     * (non-Javadoc)
     * @see com.ideamoment.saml.model.IUser#getCertificate()
     */
    @Override
    public Certificate getCertificate() {
        return this.certificate;
    }

    /*
     * (non-Javadoc)
     * @see com.ideamoment.saml.model.IUser#getCertificateStr()
     */
    @Override
    public String getCertificateStr() {
        return this.certificateStr;
    }

    /*
     * (non-Javadoc)
     * @see com.ideamoment.saml.model.IUser#getSamlNotBefore()
     */
    @Override
    public int getSamlNotBefore() {
        return this.samlNotBefore;
    }

    /*
     * (non-Javadoc)
     * @see com.ideamoment.saml.model.IUser#getSamlNotAfter()
     */
    @Override
    public int getSamlNotAfter() {
        return this.samlNotAfter;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param privateKey
     *            the privateKey to set
     */
    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    /**
     * @param privateKeyStr
     *            the privateKeyStr to set
     */
    public void setPrivateKeyStr(String privateKeyStr) {
        this.privateKeyStr = privateKeyStr;
    }

    /**
     * @param publicKey
     *            the publicKey to set
     */
    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * @param publicKeyStr
     *            the publicKeyStr to set
     */
    public void setPublicKeyStr(String publicKeyStr) {
        this.publicKeyStr = publicKeyStr;
    }

    /**
     * @param certificate
     *            the certificate to set
     */
    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    /**
     * @param certificateStr
     *            the certificateStr to set
     */
    public void setCertificateStr(String certificateStr) {
        this.certificateStr = certificateStr;
    }

    /**
     * @param samlNotBefore
     *            the samlNotBefore to set
     */
    public void setSamlNotBefore(int samlNotBefore) {
        this.samlNotBefore = samlNotBefore;
    }

    /**
     * @param samlNotAfter
     *            the samlNotAfter to set
     */
    public void setSamlNotAfter(int samlNotAfter) {
        this.samlNotAfter = samlNotAfter;
    }

    @Override
    public void generateNewKey() {

        KeyPairGenerator keygen;
        long start = System.currentTimeMillis();
        try {
            keygen = KeyPairGenerator.getInstance("DSA", "BC");
            keygen.initialize(1024);
            KeyPair keyPair = keygen.genKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
            writeKeyToStr();
            long end = System.currentTimeMillis();
            System.out.println("create keys using time: " + (end - start));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void generateCertificate(X509Certificate caCert,
                                    PrivateKey privKey) {

        String subject = "o="+this.id+",dc=org";
        String issuer = caCert.getIssuerDN().toString();  
        Certificate cert = null;
        try {
            cert = generateV3(issuer, subject,  
                    BigInteger.ZERO, new Date(System.currentTimeMillis() - 1000  
                            * 60 * 60 * 24),  
                    new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24  
                            * 365 * 32), publicKey,//待签名的公钥  
                            privKey//CA的私钥  
                    , null);
        } catch (OperatorCreationException e) {
            cert = null;
            e.printStackTrace();
        } catch (CertificateException e) {
            cert = null;
            e.printStackTrace();
        } catch (IOException e) {
            cert = null;
            e.printStackTrace();
        }
        certificate = cert;
        writeCertToString();

    }

    @Override
    public void setId(Serializable id) {
        this.id = (String)id;
    }

    @Override
    public void readKeyFromStr() {
        if (privateKeyStr == null || publicKeyStr == null) {
            generateNewKey();
            writeKeyToStr();
            return;
        }
        Base64 base64encoder = new Base64();
        KeyFactory keyfact;
        try {
            keyfact = KeyFactory.getInstance("DSA", "BC");
            // public
            readPublicKeyFromStr(keyfact, base64encoder);
            // private
            readPrivateKeyFromStr(keyfact, base64encoder);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            privateKey = null;
            publicKey = null;
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            privateKey = null;
            publicKey = null;
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            privateKey = null;
            publicKey = null;
        }
    }

    @Override
    public void readPublicKeyFromStr() {
        try {
            KeyFactory keyfact = KeyFactory.getInstance("DSA", "BC");
            Base64 base64encoder = new Base64();
            readPublicKeyFromStr(keyfact, base64encoder);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }
    
    private void readPublicKeyFromStr(KeyFactory keyfact, Base64 base64encoder) throws InvalidKeySpecException {
        byte[] pub = base64encoder.decode(publicKeyStr.getBytes());
        X509EncodedKeySpec pubkeyspec = new X509EncodedKeySpec(pub);
        publicKey = keyfact.generatePublic(pubkeyspec);
    }

    @Override
    public void readPrivateKeyFromStr() {
        try {
            KeyFactory keyfact = KeyFactory.getInstance("DSA", "BC");
            Base64 base64encoder = new Base64();
            readPrivateKeyFromStr(keyfact, base64encoder);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }
    
    private void readPrivateKeyFromStr(KeyFactory keyfact, Base64 base64encoder) throws InvalidKeySpecException {
        byte[] pri = base64encoder.decode(privateKeyStr.getBytes());
        PKCS8EncodedKeySpec prikeyspec = new PKCS8EncodedKeySpec(pri);
        privateKey = keyfact.generatePrivate(prikeyspec);
    }

    @Override
    public void readCertificateFromStr() {
        // TODO Auto-generated method stub
    }

    public void writeKeyToStr() {
        if (privateKey == null || publicKey == null) {
            generateNewKey();
        }
        Base64 base64encoder = new Base64();
        // public
        byte[] pub = base64encoder.encode(publicKey.getEncoded());
        publicKeyStr = new String(pub);
        // private
        byte[] pri = base64encoder.encode(privateKey.getEncoded());
        privateKeyStr = new String(pri);
    }

    public Certificate generateV3(String issuer,
                                  String subject,
                                  BigInteger serial,
                                  Date notBefore,
                                  Date notAfter,
                                  PublicKey publicKey,
                                  PrivateKey privKey,
                                  List<Extension> extensions)
            throws OperatorCreationException, CertificateException, IOException {

        X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(
                new X500Name(issuer), serial, notBefore, notAfter,
                new X500Name(subject), publicKey);
        ContentSigner sigGen = new JcaContentSignerBuilder("SHA1withRSA")
                .setProvider("BC").build(privKey);
        // privKey是CA的私钥，publicKey是待签名的公钥，那么生成的证书就是被CA签名的证书。
        if (extensions != null)
            for (Extension ext : extensions) {
                builder.addExtension((ext.getExtnId()), ext.isCritical(),
                                     ext.getExtnValue());
            }
        X509CertificateHolder holder = builder.build(sigGen);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream is1 = new ByteArrayInputStream(holder.toASN1Structure()
                .getEncoded());
        X509Certificate theCert = (X509Certificate) cf.generateCertificate(is1);
        is1.close();
        return theCert;
    }

    private void writeCertToString(){
        Base64 base64hendle = new Base64();
        StringBuffer cstr = new StringBuffer();
        String certstr = null;
        try {
            cstr.append(X509Factory.BEGIN_CERT);
            cstr.append("\n");
            cstr.append(new String(base64hendle.encode(certificate.getEncoded())));
            cstr.append("\n");
            cstr.append(X509Factory.END_CERT);
            certstr = cstr.toString();
        } catch (CertificateEncodingException e) {
            certstr = null;
            e.printStackTrace();
        }
        certificateStr = certstr;
    }
}
