/**
 * 
 */
package com.ideamoment.saml;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.joda.time.DateTime;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameIDPolicy;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.UnmarshallingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;


/**
 * @author Chinakite
 *
 */
public class SamlRequest extends Saml {
    
    private static Logger logger = LoggerFactory.getLogger(SamlRequest.class);
    
    String samlRequest;

    String issuerURL;
    String requestID;
    String acsURL;
    String provideName;
    
    public SamlRequest(String issuerURL, String provideName, String acsURL) {
        super();
        this.issuerURL = issuerURL;
        this.provideName = provideName;
        this.acsURL = acsURL;
    }
    public SamlRequest(String request) {
        super();
        this.samlRequest = request;
    }
    
    /**
     * 生成AuthnRequest报文
     * 
     * @return
     */
    public String generateAuthnRequest(){
        if(issuerURL == null || provideName == null || acsURL == null){
            return null;
        }
        Issuer issuer = create(Issuer.class, Issuer.DEFAULT_ELEMENT_NAME);
        issuer.setValue(issuerURL);
        DateTime now = new DateTime ();
        AuthnRequest authnrequest = (AuthnRequest) create(AuthnRequest.class
                ,AuthnRequest.DEFAULT_ELEMENT_NAME);
        
        NameIDPolicy nameIdPolicy = create(NameIDPolicy.class, NameIDPolicy.DEFAULT_ELEMENT_NAME);
        
        authnrequest.setIssuer(issuer);
        authnrequest.setID(generator.generateIdentifier());
        authnrequest.setAssertionConsumerServiceURL(acsURL);
        authnrequest.setIssueInstant(now);
        authnrequest.setProtocolBinding(SAMLConstants.SAML2_POST_BINDING_URI);
        authnrequest.setProviderName(provideName);
        
        authnrequest.setNameIDPolicy(nameIdPolicy);
        
        try {
            samlRequest = printToString(authnrequest);
            SamlEncoder encoder = new SamlEncoder(samlRequest);
            samlRequest = encoder.encode();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            samlRequest = null;
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } catch (MarshallingException e) {
            // TODO Auto-generated catch block
            samlRequest = null;
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            samlRequest = null;
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }

        return samlRequest;
    }
    
    public boolean readFromRequest(){
        if(samlRequest == null){
            return false;
        }
//        SamlDecoder decoder = new SamlDecoder(samlRequest);
//        String samlXmlRequest = decoder.decode();
//        if(samlRequest == null){
//            return false;
//        }
        try {
            AuthnRequest request = (AuthnRequest) readFromString(samlRequest);
            
            issuerURL   = request.getIssuer().getValue();
            requestID   = request.getID();
            acsURL      = request.getAssertionConsumerServiceURL();
            provideName = request.getProviderName();
            return true;
            
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } catch (UnmarshallingException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } catch (SAXException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        
        return false;
    }
    

    
    public String getSamlRequest() {
        return samlRequest;
    }
    public void setSamlRequest(String samlRequest) {
        this.samlRequest = samlRequest;
    }
    public String getIssuerURL() {
        return issuerURL;
    }
    public void setIssuerURL(String issuerURL) {
        this.issuerURL = issuerURL;
    }
    public String getRequestID() {
        return requestID;
    }
    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }
    public String getAcsURL() {
        return acsURL;
    }
    public void setAcsURL(String acsURL) {
        this.acsURL = acsURL;
    }
    public String getprovideName() {
        return provideName;
    }
    public void setprovideName(String provideName) {
        this.provideName = acsURL;
    }
}
