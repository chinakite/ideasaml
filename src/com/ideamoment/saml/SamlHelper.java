/**
 * 
 */
package com.ideamoment.saml;

import java.security.cert.X509Certificate;

import com.ideamoment.saml.model.SamlUser;
import com.ideamoment.saml.model.SamlConfig;


/**
 * @author Chinakite
 *
 */
public class SamlHelper {
    
    /**
     * 
     * 
     * @param config
     */
    public static void generateCert(SamlUser user){
        user.generateCertificate((X509Certificate)SamlConfig.getCert(), SamlConfig.getRootPrivateKey());     
    }
    
    
}
