/**
 * 
 */
package com.ideamoment.saml;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 编码器，目前没有启用压缩算法，只做了Base64处理
 * 
 * @author Chinakite
 *
 */
public class SamlEncoder {
    
    private static Logger logger = LoggerFactory.getLogger(SamlEncoder.class);
    
    private String samlInput;       //输入
    private String samlResult;      //编码后的结果
    
    public SamlEncoder(String samlInput) {
        this.samlInput = samlInput;
    }
    
    @SuppressWarnings("deprecation")
    public String encode(){
        return encode(true);
    }
    
    public String encode(boolean urlEncoding) {
        if(samlInput == null){
            return null;
        }
        
        try {
            byte[] samlResponseByte = Base64.encodeBase64(samlInput.getBytes());
            samlResult = new String(samlResponseByte);
            if(urlEncoding) {
                samlResult = URLEncoder.encode(samlResult, "UTF-8");
            }
            return samlResult;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            samlInput = null;
            logger.error(e.getMessage(), e);
        }
        return samlResult;
    }
}
