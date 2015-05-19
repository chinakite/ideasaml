/**
 * 
 */
package com.ideamoment.saml;

import org.junit.Test;

import com.ideamoment.saml.model.SamlUser;


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
        MockUser user = new MockUser();
        user.setId("402882f24d32b4b1014d32b4b1ef0000");
        user.setName("chinakite");
        
        String issuerURL = "http://localhost:9080/wanr-usercenter";
        String requestID = "abcdefg";
        String acsURL = "http://localhost:9080/wanr-usercenter/sso";
        
        SamlResponse resp = new SamlResponse(user, issuerURL, requestID, acsURL);
        resp.generateAuthnResponse();
        String respStr = resp.getSamlResponse();
        
        System.out.println(respStr);
        
    }
}
