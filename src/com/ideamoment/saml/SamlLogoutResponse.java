/**
 * 
 */
package com.ideamoment.saml;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.joda.time.DateTime;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.LogoutResponse;
import org.opensaml.saml2.core.Status;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.UnmarshallingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;


/**
 * @author Chinakite
 *
 */
public class SamlLogoutResponse extends Saml {
    private static Logger logger = LoggerFactory.getLogger(SamlLogoutResponse.class);
    
    private static String IDP_URL = "http://localhost:9080/wanr-usercenter/sso";
    
    private String issuerURL;
    private String inResponseTo;
    
    private String samlLogoutResponse;
    
    public SamlLogoutResponse(String issuerURL, String inResponseTo) {
        this.issuerURL = issuerURL;
        this.inResponseTo = inResponseTo;
    }
    
    public SamlLogoutResponse(String samlLogoutResponse) {
        this.samlLogoutResponse = samlLogoutResponse;
    }
    
    public String generateLogoutResponse(){
        if(issuerURL == null){
            return null;
        }
        
        Issuer issuer = create(Issuer.class, Issuer.DEFAULT_ELEMENT_NAME);
        issuer.setValue(issuerURL);
        DateTime now = new DateTime();
        
        LogoutResponse logoutResponse = create(LogoutResponse.class, LogoutResponse.DEFAULT_ELEMENT_NAME);
        logoutResponse.setVersion(SAMLVersion.VERSION_20);
        logoutResponse.setID(generator.generateIdentifier());
        logoutResponse.setIssueInstant(now);
        
        StatusCode statusCodeElement = create 
                (StatusCode.class, StatusCode.DEFAULT_ELEMENT_NAME);
        statusCodeElement.setValue(StatusCode.SUCCESS_URI);
        
        Status status = create (Status.class, Status.DEFAULT_ELEMENT_NAME);
        status.setStatusCode (statusCodeElement);
        logoutResponse.setStatus (status);
        
        Issuer issueridp = create(Issuer.class, Issuer.DEFAULT_ELEMENT_NAME);
        issueridp.setValue(IDP_URL);
        logoutResponse.setIssuer(issueridp);
        
        try {
            samlLogoutResponse = printToString(logoutResponse);
            SamlEncoder encoder = new SamlEncoder(samlLogoutResponse);
            samlLogoutResponse = encoder.encode();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            samlLogoutResponse = null;
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } catch (MarshallingException e) {
            // TODO Auto-generated catch block
            samlLogoutResponse = null;
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            samlLogoutResponse = null;
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        
        return samlLogoutResponse;
    }
    
    public boolean readFromLogoutResponse(){
        if(samlLogoutResponse == null){
            return false;
        }
        
        try {
            LogoutResponse logoutResponse = (LogoutResponse)readFromString(samlLogoutResponse);
            issuerURL = logoutResponse.getIssuer().getValue();
            inResponseTo = logoutResponse.getInResponseTo();
            
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
    
}
