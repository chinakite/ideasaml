/**
 * 
 */
package com.ideamoment.saml;

import java.security.cert.X509Certificate;

import org.junit.Test;

import com.ideamoment.saml.model.SamlConfig;


/**
 * @author Chinakite
 *
 */
public class TestSamlResponse {
    /**
     * 
     */
    @Test
    public void testGenerateResponse() {
        SamlConfig.init();
        
        MockUser user = new MockUser();
        user.setId("402882f24d32b4b1014d32b4b1ef0000");
        user.setName("chinakite");
        
        user.generateNewKey();
        user.generateCertificate((X509Certificate)SamlConfig.getCert(), SamlConfig.getRootPrivateKey());
        
        String issuerURL = "http://localhost:9080/wanr-usercenter";
        String requestID = "abcdefg";
        String acsURL = "http://localhost:9080/ideasaml-sp/sso";
        
        SamlResponse resp = new SamlResponse(user, issuerURL, requestID, acsURL);
        resp.generateAuthnResponse();
        String respStr = resp.getSamlResponse();
        
        System.out.println(respStr);
        
    }
}
