/**
 * 
 */
package com.ideamoment.saml;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;


/**
 * @author Chinakite
 *
 */
public class TestSamlRequest {
    /**
     * 
     */
    @Test
    public void testGenerateAuthnRequest() {
        String issuerURL   = "http://www.ideamoment.com/sp";
        String provideName = "ideasaml-sp";
        String acsURL      = "http://www.ideamoment.com/idp";
        
        SamlRequest samlReq = new SamlRequest(issuerURL, provideName, acsURL);
        String reqStr = samlReq.generateAuthnRequest();
        
        String r;
        try {
            r = URLDecoder.decode(reqStr, "UTF-8");
            System.out.println("===========================================");
            System.out.println(r);
            
            Base64 base64encoder = new Base64();
            String rr = new String(base64encoder.decode(r));
            
            System.out.println("===========================================");
            System.out.println(rr);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
