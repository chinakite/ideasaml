/**
 * 
 */
package com.ideamoment.saml.model;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;


/**
 * 用户，定义了SAML中用户对象必须提供的接口
 * 
 * @author Chinakite
 *
 */
public interface SamlUser {
    /**
     * 用户Id
     * @return
     */ 
    public Serializable getId();
    
    public void setId(Serializable id);
    
    /**
     * 用户名
     * @return
     */
    public String getName();
    
    public void setName(String name);
    
    /**
     * 私钥
     * 
     * @return
     */
    public PrivateKey getPrivateKey();
    
    public void setPrivateKey(PrivateKey privateKey);
    
    /**
     * 私钥（字符串）
     * 
     * @return
     */
    public String getPrivateKeyStr();
    
    public void setPrivateKeyStr(String privateKeyStr);
    
    /**
     * 公钥
     * 
     * @return
     */
    public PublicKey getPublicKey();
    
    public void setPublicKey(PublicKey publicKey);
    
    /**
     * 公钥（字符串）
     * 
     * @return
     */
    public String getPublicKeyStr();
    
    public void setPublicKeyStr(String publicKeyStr);
    
    /**
     * 证书
     * 
     * @return
     */
    public Certificate getCertificate();
    
    public void setCertificate(Certificate certificate);
    
    /**
     * 证书（字符串）
     * 
     * @return
     */
    public String getCertificateStr();
    
    public void setCertificateStr(String certificateStr);
    
    /**
     * 不早于
     * 
     * @return
     */
    public int getSamlNotBefore();
    
    public void setSamlNotBefore(int samlNotBefore);
    
    /**
     * 不晚于
     * 
     * @return
     */
    public int getSamlNotAfter();
    
    public void setSamlNotAfter(int samlNotAfter);
    
    /**
     * 生成新的密钥
     */
    public void generateNewKey();
    
    /**
     * 生成证书
     * 
     * @param caCert
     * @param prikey
     */
    public void generateCertificate(X509Certificate caCert, PrivateKey privateKey);
    
    /**
     * 读取字符串密钥信息
     */
    public void readKeyFromStr();
}
