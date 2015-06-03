/**
 * 
 */
package com.ideamoment.saml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.apache.commons.codec.binary.Base64;
import org.opensaml.common.SAMLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 解码器，目前没有启用压缩算法，只做了Base64处理
 * 
 * @author Chinakite
 *
 */
public class SamlDecoder {
    
    private static Logger logger = LoggerFactory.getLogger(SamlDecoder.class);
    
    String samlInput;
    String samlResult;
    
    public SamlDecoder(String samlInput) {
        super();
        this.samlInput = samlInput;
    }
    public String decode(){
        return decode(true);
    }
    
    public String decode(boolean urlDecoding){
        if(samlInput == null || samlInput.equals("undefined")){
            return null;
        }
        try {
            samlResult = decodeAuthnRequestXML(samlInput, urlDecoding);
        } catch (SAMLException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            samlResult = null;
        }
        return samlResult;
    }
    
    
    
    /** (Based on the Google reference implementation, with some modifications suggested in the Google Api's group
    *  -Nate- to avoid buffer size problems in the original code) 
    * Retrieves the AuthnRequest from the encoded and compressed String extracted
    * from the URL. The AuthnRequest XML is retrieved in the following order: <p>
       * 1. URL decode <br> 2. Base64 decode <br> 3. Inflate <br> Returns the String
    * format of the AuthnRequest XML.
    * 
    * @param encodedString the encoded request xml
    * @return the string format of the authentication request XML.
    * 
    */
    private String decodeAuthnRequestXML(String encodedString, boolean urlDecoding) 
        throws SAMLException {
//        String uncompressed = null; 
        try {
            // URL decode
            // No need to URL decode: auto decoded by request.getParameter() method
            // Base64 decode
            if(urlDecoding) {
                encodedString = URLDecoder.decode(encodedString, "UTF-8");
            }
            
            Base64 base64Decoder = new Base64();
            byte[] xmlBytes = encodedString.getBytes("UTF-8");
            byte[] base64DecodedByteArray = base64Decoder.decode(xmlBytes);
     
            return new String(base64DecodedByteArray, "UTF-8");
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new SAMLException("Error decoding AuthnRequest: " +
               "Check decoding scheme - " + e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new SAMLException("Error decoding AuthnRequest: " +
             "Check decoding scheme - " + e.getMessage());
        }
    }
  /** This version is based in the reference implementation, with a modification that avoids using a 
   * fixed buffer size during uncompression. (in its place uses an expandable ByteArrayOutputStream).
   * NOTE: not sure whether it is still necessary to call this method with the boolean flag.
   * @param bytes
   * @param nowrap
   * @return an array of bytes with the inflated content
   * @throws IOException
   */
    private static byte[] inflate(byte[] bytes, boolean nowrap)
            throws IOException {
        Inflater decompressor = null;
        ByteArrayOutputStream out = null;
        try {
            decompressor = new Inflater(nowrap);
            decompressor.setInput(bytes);
            out = new ByteArrayOutputStream(bytes.length);
            byte[] buf = new byte[1024];
            while (!decompressor.finished()) {
                try {
                    int count = decompressor.inflate(buf); // PROBLEM
                    out.write(buf, 0, count);
                    // added check to avoid loops
                    if (count == 0) {
                        return altInflate(bytes);
                    }
                } catch (DataFormatException e) {
                    logger.error(e.getMessage(), e);
                    System.out.println("DataFormatException while inflating "+e);
                    return altInflate(bytes);

                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    System.out.println("Unexpected Exception while inflating "+e);
                    return altInflate(bytes);
                } catch (Throwable e) {

                    System.out.println("Unexpected Throwable while inflating "+e);
                    return altInflate(bytes);
                }
            }
            return out.toByteArray();
        } finally {
            if (decompressor != null)
                decompressor.end();
            try {
                if (out != null)
                    out.close();
            } catch (IOException ioe) {
                /* ignore */
            }
        }
    }
  
    /**
     * Alternative method for inflating the content, used when the default
     * method is not successful.
     * 
     * @param bytes
     * @return an array of bytes containing the inflated content
     * @throws IOException
     */
    protected static byte[] altInflate(byte[] bytes) throws IOException {
        System.out.println("AltInflate Processing... ");
        ByteArrayInputStream bais = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InflaterInputStream iis = null;
        byte[] buf = new byte[1024];
        try {
            // if DEFLATE fails, then attempt to unzip the byte array according
            // to
            // zlib (rfc 1950)
            bais = new ByteArrayInputStream(bytes);
            iis = new InflaterInputStream(bais);
            buf = new byte[1024];
            int count = iis.read(buf); // PROBLEM
            while (count != -1) {
                baos.write(buf, 0, count);
                count = iis.read(buf);
            }
            return baos.toByteArray();
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            if (iis != null)
                try {
                    iis.close();
                } catch (IOException ex2) {
                }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException ex2) {
                }
            }
        }
    }
}
