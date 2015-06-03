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
public class TestSamlLogoutRequest {
    /**
     * 由于这个测试逻辑与时间有关，因此没有使用assertEquals来校验，需要生成后自行检查输出结果。
     */
    @Test
    public void testGenerateAuthnRequest() {
        String issuerURL   = "http://localhost:9080/ideasaml-sp";
        String nameID      = "abcdefg";
        
        SamlLogoutRequest samlLogoutReq = new SamlLogoutRequest(issuerURL, nameID);
        String reqStr = samlLogoutReq.generateLogoutRequest();
        
        System.out.println("================ Generate AuthnRequest ====================");
        System.out.println(reqStr);
        
        String r;
        try {
            r = URLDecoder.decode(reqStr, "UTF-8");
            Base64 base64encoder = new Base64();
            String rr = new String(base64encoder.decode(r));
            
            System.out.println("================ Decode AuthnRequest ====================");
            System.out.println(rr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
