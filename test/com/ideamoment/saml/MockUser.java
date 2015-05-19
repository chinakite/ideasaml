/**
 * 
 */
package com.ideamoment.saml;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import com.ideamoment.saml.model.SamlUser;


/**
 * @author Chinakite
 *
 */
public class MockUser implements SamlUser {

    private String id;
    private String name;
    private PrivateKey privateKey;
    private String privateKeyStr;
    private PublicKey publicKey;
    private String publicKeyStr;
    private Certificate certificate;
    private String certificateStr;
    private int samlNotBefore = 15;
    private int samlNotAfter = 30;
    
    /* (non-Javadoc)
     * @see com.ideamoment.saml.model.IUser#getId()
     */
    @Override
    public String getId() {
        return this.id;
    }

    /* (non-Javadoc)
     * @see com.ideamoment.saml.model.IUser#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }

    /* (non-Javadoc)
     * @see com.ideamoment.saml.model.IUser#getPrivateKey()
     */
    @Override
    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    /* (non-Javadoc)
     * @see com.ideamoment.saml.model.IUser#getPrivateKeyStr()
     */
    @Override
    public String getPrivateKeyStr() {
        return this.privateKeyStr;
    }

    /* (non-Javadoc)
     * @see com.ideamoment.saml.model.IUser#getPublicKey()
     */
    @Override
    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    /* (non-Javadoc)
     * @see com.ideamoment.saml.model.IUser#getPublicKeyStr()
     */
    @Override
    public String getPublicKeyStr() {
        return this.publicKeyStr;
    }

    /* (non-Javadoc)
     * @see com.ideamoment.saml.model.IUser#getCertificate()
     */
    @Override
    public Certificate getCertificate() {
        return this.certificate;
    }

    /* (non-Javadoc)
     * @see com.ideamoment.saml.model.IUser#getCertificateStr()
     */
    @Override
    public String getCertificateStr() {
        return this.certificateStr;
    }

    /* (non-Javadoc)
     * @see com.ideamoment.saml.model.IUser#getSamlNotBefore()
     */
    @Override
    public int getSamlNotBefore() {
        return this.samlNotBefore;
    }

    /* (non-Javadoc)
     * @see com.ideamoment.saml.model.IUser#getSamlNotAfter()
     */
    @Override
    public int getSamlNotAfter() {
        return this.samlNotAfter;
    }

    
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    
    /**
     * @param privateKey the privateKey to set
     */
    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    
    /**
     * @param privateKeyStr the privateKeyStr to set
     */
    public void setPrivateKeyStr(String privateKeyStr) {
        this.privateKeyStr = privateKeyStr;
    }

    
    /**
     * @param publicKey the publicKey to set
     */
    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    
    /**
     * @param publicKeyStr the publicKeyStr to set
     */
    public void setPublicKeyStr(String publicKeyStr) {
        this.publicKeyStr = publicKeyStr;
    }

    
    /**
     * @param certificate the certificate to set
     */
    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    
    /**
     * @param certificateStr the certificateStr to set
     */
    public void setCertificateStr(String certificateStr) {
        this.certificateStr = certificateStr;
    }

    
    /**
     * @param samlNotBefore the samlNotBefore to set
     */
    public void setSamlNotBefore(int samlNotBefore) {
        this.samlNotBefore = samlNotBefore;
    }

    
    /**
     * @param samlNotAfter the samlNotAfter to set
     */
    public void setSamlNotAfter(int samlNotAfter) {
        this.samlNotAfter = samlNotAfter;
    }

    @Override
    public void generateNewKey() {

        // TODO Auto-generated method stub
        
    }

    @Override
    public void generateCertificate(X509Certificate caCert,
                                    PrivateKey privateKey) {

        // TODO Auto-generated method stub
        
    }

    @Override
    public void setId(Serializable id) {

        // TODO Auto-generated method stub
        
    }

    @Override
    public void readKeyFromStr() {

        // TODO Auto-generated method stub
        
    }
    
    

}
