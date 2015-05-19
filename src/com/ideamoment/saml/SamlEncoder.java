/**
 * 
 */
package com.ideamoment.saml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

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
    
    private String samlRequest;
    private String samlResult;
    
    public SamlEncoder(String samlRequest) {
        this.samlRequest = samlRequest;
    }
    
    @SuppressWarnings("deprecation")
    public String encode(){
        if(samlRequest == null){
            return null;
        }
        
        try {
            byte[] samlResponseByte = Base64.encodeBase64(samlRequest.getBytes());
            samlResult = URLEncoder.encode(new String(samlResponseByte), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            samlRequest = null;
            logger.error(e.getMessage(), e);
        }
        
//        Deflater deflater = new Deflater(Deflater.DEFLATED, true);
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(
//                byteArrayOutputStream, deflater);
//        try {
//            deflaterOutputStream.write(samlRequest.getBytes());
//            deflaterOutputStream.close();
//            Base64 base64encoder = new Base64();
//            byte[] samlResponseByte = base64encoder.encode(byteArrayOutputStream.toByteArray());
//            
//            samlResult = URLEncoder.encode(new String(samlResponseByte), "UTF-8");
//        } catch (IOException e) {
//            samlRequest = null;
//            logger.error(e.getMessage(), e);
//            e.printStackTrace();
//        }
        return samlResult;
    }
    
    
}
