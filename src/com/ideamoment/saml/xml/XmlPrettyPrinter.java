/**
 * 
 */
package com.ideamoment.saml.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * @author Chinakite
 *
 */
public class XmlPrettyPrinter extends DefaultHandler {
    
    private static Logger logger = LoggerFactory.getLogger(XmlPrettyPrinter.class);
    
    private String indent = "";
    
    private StringBuffer currentValue = null;
    
    private StringBuffer output = new StringBuffer ();    
    
    private boolean justHitStartTag;
    
    private static final String standardIndent = "  ";
    private static final String endLine = System.getProperty ("line.separator");
    
    public static String prettyPrint (byte[] content) {
        try {
            XmlPrettyPrinter pretty = new XmlPrettyPrinter();
            SAXParserFactory factory = SAXParserFactory.newInstance ();
            factory.setFeature
                ("http://xml.org/sax/features/namespace-prefixes", true);
            factory.newSAXParser ().parse 
                (new ByteArrayInputStream (content), pretty);
            return pretty.toString ();
        } catch (Exception ex) {
            ex.printStackTrace ();
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }
    
    public static String prettyPrint(String content) {
        try {
            XmlPrettyPrinter pretty = new XmlPrettyPrinter();
            SAXParserFactory factory = SAXParserFactory.newInstance ();
            factory.setFeature
                ("http://xml.org/sax/features/namespace-prefixes", true);
            factory.newSAXParser ().parse (content, pretty);
            return pretty.toString ();
        } catch (Exception ex) {
            ex.printStackTrace ();
            logger.error(ex.getMessage(), ex);
            return "EXCEPTION: " + ex.getClass ().getName () + " saying \"" +
                ex.getMessage () + "\"";
        }
    }
    
    public static String prettyPrint(InputStream content) {
        try {
            XmlPrettyPrinter pretty = new XmlPrettyPrinter ();
            SAXParserFactory factory = SAXParserFactory.newInstance ();
            factory.setFeature
                ("http://xml.org/sax/features/namespace-prefixes", true);
            factory.newSAXParser ().parse (content, pretty);
            return pretty.toString ();
        } catch (Exception ex) {
            ex.printStackTrace ();
            logger.error(ex.getMessage(), ex);
            return "EXCEPTION: " + ex.getClass ().getName () + " saying \"" +
                ex.getMessage () + "\"";
        }
    }

    public static String prettyPrint(Document doc) throws TransformerException {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream ();
            TransformerFactory.newInstance ().newTransformer()
                .transform (new DOMSource (doc), new StreamResult (buffer));
            byte[] rawResult = buffer.toByteArray ();
            buffer.close ();
            //return new String(rawResult);
            return prettyPrint (rawResult);
        } catch (Exception ex) {
            ex.printStackTrace ();
            logger.error(ex.getMessage(), ex);
            return "EXCEPTION: " + ex.getClass ().getName () + " saying \"" +
                ex.getMessage () + "\"";
        }
    }
    
    public static class StreamAdapter extends OutputStream {
        
        private ByteArrayOutputStream out = new ByteArrayOutputStream ();
        Writer finalDestination;
        
        public StreamAdapter (Writer finalDestination){
            this.finalDestination = finalDestination;
        }
    
        public void write (int b) {
            out.write (b);
        }
    
        public void flushPretty () throws IOException {
            PrintWriter finalPrinter = new PrintWriter (finalDestination);
            finalPrinter.println 
                (XmlPrettyPrinter.prettyPrint (out.toByteArray ()));
            finalPrinter.close ();
            out.close ();
        }
    }

    public String toString() {
        return output.toString ();
    }

    public void startDocument() throws SAXException {
        output.append ("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
              .append (endLine);
    }

    public void endDocument () throws SAXException {
        output.append (endLine);
    }

    public void startElement(String URI, String name, String qName, Attributes attributes) throws SAXException {
        if (justHitStartTag)
            output.append ('>');
    
        output.append (endLine)
              .append (indent)
              .append ('<')
              .append (qName);
    
        int length = attributes.getLength ();        
        for (int a = 0; a < length; ++a)
            output.append (endLine)
                  .append (indent)
                  .append (standardIndent)
                  .append (attributes.getQName (a))
                  .append ("=\"")
                  .append (attributes.getValue (a))
                  .append ('\"');
                  
        if (length > 0)
            output.append (endLine)
                  .append (indent);
            
        indent += standardIndent;
        currentValue = new StringBuffer ();
        justHitStartTag = true;
    }

    public void endElement (String URI, String name, String qName) throws SAXException {
        indent = indent.substring 
            (0, indent.length () - standardIndent.length ());
        
        if (currentValue == null)
            output.append (endLine)
                  .append (indent)
                  .append ("</")
                  .append (qName)
                  .append ('>');
        else if (currentValue.length () != 0)
            output.append ('>')
                  .append (currentValue.toString ())
                  .append ("</")
                  .append (qName)
                  .append ('>');
        else
            output.append ("/>");
              
        currentValue = null;
        justHitStartTag = false;
    }
    
    public void characters (char[] chars, int start, int length) throws SAXException {
        if (currentValue != null)
            currentValue.append (escape (chars, start, length));
    }

    private static String escape (char[] chars, int start, int length) {
        StringBuffer result = new StringBuffer ();
        for (int c = start; c < start + length; ++c)
            if (chars[c] == '<')
                result.append ("&lt;");
            else if (chars[c] == '&')
                result.append ("&amp;");
            else
                result.append (chars[c]);
                
        return result.toString ();
    }
}
