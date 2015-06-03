/**
 * 
 */
package com.ideamoment.saml;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.joda.time.DateTime;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.LogoutRequest;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Status;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.saml2.core.StatusMessage;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.UnmarshallingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;


/**
 * @author Chinakite
 *
 */
public class SamlLogoutRequest extends Saml {
    
    private static Logger logger = LoggerFactory.getLogger(SamlLogoutRequest.class);
    
    String issuerURL;
    String nameId;
    
    String samlLogoutRequest;
    
    public SamlLogoutRequest(String issueURL, String nameId) {
        this.issuerURL = issueURL;
        this.nameId = nameId;
    }
    
    public SamlLogoutRequest(String samlLogoutRequest) {
        this.samlLogoutRequest = samlLogoutRequest;
    }
    
    /**
     * 生成AuthnRequest报文
     * 
     * @return
     */
    public String generateLogoutRequest() {
        if(issuerURL == null || nameId == null){
            return null;
        }
        
        Issuer issuer = create(Issuer.class, Issuer.DEFAULT_ELEMENT_NAME);
        issuer.setValue(issuerURL);
        DateTime now = new DateTime ();
        
        LogoutRequest logoutRequest = (LogoutRequest)create(LogoutRequest.class, 
                                                            LogoutRequest.DEFAULT_ELEMENT_NAME);
        
        NameID nameIdElement = create(NameID.class, NameID.DEFAULT_ELEMENT_NAME);
        nameIdElement.setValue(nameId);
        
        logoutRequest.setIssuer(issuer);
        logoutRequest.setNameID(nameIdElement);
        logoutRequest.setID(generator.generateIdentifier());
        logoutRequest.setIssueInstant(now);
        logoutRequest.setVersion(SAMLVersion.VERSION_20);
        
        try {
            samlLogoutRequest = printToString(logoutRequest);
            SamlEncoder encoder = new SamlEncoder(samlLogoutRequest);
            samlLogoutRequest = encoder.encode();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            samlLogoutRequest = null;
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } catch (MarshallingException e) {
            // TODO Auto-generated catch block
            samlLogoutRequest = null;
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            samlLogoutRequest = null;
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }

        return samlLogoutRequest;
        
    }
    
    
    public boolean readFromLogoutRequest(){
        if(samlLogoutRequest == null){
            return false;
        }
        
        try {
            LogoutRequest logoutRequest = (LogoutRequest)readFromString(samlLogoutRequest);
            
            issuerURL = logoutRequest.getIssuer().getValue();
            nameId = logoutRequest.getNameID().getValue();
            
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
    
    
    /**
     * @return the issuerURL
     */
    public String getIssuerURL() {
    
        return issuerURL;
    }
    
    /**
     * @param issuerURL the issuerURL to set
     */
    public void setIssuerURL(String issuerURL) {
    
        this.issuerURL = issuerURL;
    }
    
    /**
     * @return the nameId
     */
    public String getNameId() {
    
        return nameId;
    }
    
    /**
     * @param nameId the nameId to set
     */
    public void setNameId(String nameId) {
    
        this.nameId = nameId;
    }
    
    
}
