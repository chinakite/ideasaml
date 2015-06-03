/**
 * 
 */
package com.ideamoment.saml;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.TransformerException;

import org.joda.time.DateTime;
import org.opensaml.common.SAMLException;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.Statement;
import org.opensaml.saml2.core.Subject;
import org.opensaml.security.SAMLSignatureProfileValidator;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.schema.XSAny;
import org.opensaml.xml.security.SecurityConfiguration;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.credential.BasicCredential;
import org.opensaml.xml.security.keyinfo.KeyInfoGenerator;
import org.opensaml.xml.security.keyinfo.KeyInfoGeneratorFactory;
import org.opensaml.xml.security.keyinfo.KeyInfoGeneratorManager;
import org.opensaml.xml.security.keyinfo.NamedKeyInfoGeneratorManager;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureConstants;
import org.opensaml.xml.signature.SignatureException;
import org.opensaml.xml.signature.SignatureValidator;
import org.opensaml.xml.signature.Signer;
import org.opensaml.xml.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.ideamoment.saml.model.SamlUser;

/**
 * @author Chinakite
 *
 */
public class SamlResponse extends Saml {
    
    private static Logger logger = LoggerFactory.getLogger(SamlResponse.class);
    
    private static String IDP_URL = "http://localhost:9080/wanr-usercenter/sso";
    
    private String issuerURL;
    private String requestID;
    private String acsURL;
    private String sessionId;
    private String samlResponse;
    
    private SamlUser user;
    
    
    public SamlResponse(SamlUser user, String issuerURL, String requestID,
                        String acsURL, String sessionId) {
        super();
        this.user = user;
        this.issuerURL = issuerURL;
        this.requestID = requestID;
        this.acsURL = acsURL;
        this.sessionId = sessionId;
    }
    
    public SamlResponse(SamlUser user, String issuerURL, String requestID,
                        String acsURL) {
        super();
        this.user = user;
        this.issuerURL = issuerURL;
        this.requestID = requestID;
        this.acsURL = acsURL;
    }

    public SamlResponse(SamlUser user, String samlResponse) {
        super();
        this.samlResponse = samlResponse;
        this.user = user;
    }
    
    public boolean generateAuthnResponse(){
        try {
            Assertion assertion = createStockAuthnAssertion();
            
            List publicKeys = new ArrayList();
            publicKeys.add(user.getPublicKeyStr());
            addAttribution(assertion, "publicKey", publicKeys);
            
            assertion = createAssertionSignatrue(assertion);
            samlResponse = showResponse(assertion);
            if(samlResponse != null){
                return true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e); 
            e.printStackTrace();
            return false;
        }
        return false;
    }
    
    public String showResponse(Assertion assertion) 
            throws IOException, MarshallingException, TransformerException{
        Response response = createResponse (assertion);
        Issuer issuer = create(Issuer.class, Issuer.DEFAULT_ELEMENT_NAME);
        issuer.setValue (IDP_URL);
        response.setIssuer (issuer);
        
        response.setInResponseTo(requestID);
        return printToString(response);
    }
    
    
    /**
     * Creates a assertion whose contents are a SAML authentication assertion.
     */
    public Assertion createStockAuthnAssertion ()
        throws Exception
    {
        Issuer issueridp = create(Issuer.class, Issuer.DEFAULT_ELEMENT_NAME);
        issueridp.setValue(IDP_URL);
        
        Issuer issuersp = create(Issuer.class, Issuer.DEFAULT_ELEMENT_NAME);
        issuersp.setValue(issuerURL);
        
        NameID nameID = create(NameID.class, NameID.DEFAULT_ELEMENT_NAME);
        nameID.setValue((String)user.getId());
        
        
        Subject subject = create(Subject.class, Subject.DEFAULT_ELEMENT_NAME);
        subject.setNameID (nameID);


        AuthnContextClassRef ref = create(AuthnContextClassRef.class, 
            AuthnContextClassRef.DEFAULT_ELEMENT_NAME);
        ref.setAuthnContextClassRef (AuthnContext.PPT_AUTHN_CTX);
        
        // As of this writing, OpenSAML doesn't model the wide range of
        // authentication context namespaces defined in SAML 2.0.
        // For a real project we'd probably move on to 
        //    XSAny objects, setting QNames and values each-by-each
        //    a JAXB mapping of the required schema
        //    DOM-building
        // For classroom purposes the road ends here ...
        
        AuthnContext authnContext = create 
            (AuthnContext.class, AuthnContext.DEFAULT_ELEMENT_NAME);
        authnContext.setAuthnContextClassRef(ref);

        AuthnStatement authnStatement = create 
            (AuthnStatement.class, AuthnStatement.DEFAULT_ELEMENT_NAME);
        authnStatement.setAuthnContext (authnContext);
        
        Assertion assertion = 
            create(Assertion.class, Assertion.DEFAULT_ELEMENT_NAME);
        //assertion.setID ("Assertion12345789");
        DateTime now = new DateTime();
        assertion.setID(generator.generateIdentifier());
        assertion.setIssueInstant (now);
        assertion.setIssuer (issuersp);
        assertion.setSubject (subject);
        assertion.getStatements().add(authnStatement);
        
        Conditions conditions = create 
                (Conditions.class, Conditions.DEFAULT_ELEMENT_NAME);
        conditions.setNotBefore(now.minusSeconds(user.getSamlNotBefore()));
        conditions.setNotOnOrAfter(now.plusSeconds(user.getSamlNotAfter()));     
        assertion.setConditions(conditions);
        
        return assertion;
    }
    
    
    public Assertion createAssertionSignatrue(Assertion assertion) throws NoSuchAlgorithmException, NoSuchProviderException, SecurityException, SignatureException, UnmarshallingException{
        //signed
        PrivateKey priKey = user.getPrivateKey();
        PublicKey pubKey = user.getPublicKey();
        BasicCredential goodCredential = SecurityHelper.getSimpleCredential(pubKey, priKey);

        Signature signature = create(Signature.class, Signature.DEFAULT_ELEMENT_NAME);
        signature.setSigningCredential(goodCredential);
        signature.setCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
        signature.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_DSA_SHA1);
        
        
        SecurityConfiguration secConfiguration = Configuration.getGlobalSecurityConfiguration(); 
        NamedKeyInfoGeneratorManager namedKeyInfoGeneratorManager = secConfiguration.getKeyInfoGeneratorManager(); 
        KeyInfoGeneratorManager keyInfoGeneratorManager = namedKeyInfoGeneratorManager.getDefaultManager();
        KeyInfoGeneratorFactory keyInfoGeneratorFactory = keyInfoGeneratorManager.getFactory(goodCredential);
        KeyInfoGenerator keyInfoGenerator = keyInfoGeneratorFactory.newInstance();
        KeyInfo keyInfo = null;

        try {
            keyInfo = keyInfoGenerator.generate(goodCredential);
            signature.setKeyInfo(keyInfo);
            assertion.setSignature(signature);
            Assertion signedAssertion = null;
            
            Configuration.getMarshallerFactory().getMarshaller(assertion).marshall(assertion);
            Signer.signObject(signature);
            signedAssertion = 
                    (Assertion) Configuration.getUnmarshallerFactory().getUnmarshaller(assertion.getDOM()).unmarshall(assertion.getDOM());
        
            return signedAssertion;
        } catch (MarshallingException e) {
            e.printStackTrace();
            return null;
        } catch (org.opensaml.xml.security.SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String getSamlResponse() {
        return samlResponse;
    }
    
    public String getBase64SamlResponse() {
        SamlEncoder encoder = new SamlEncoder(samlResponse);
        return encoder.encode();
    }
    
    public void readResponseFromStr(){
        try {
            if(samlResponse == null){
                throw new IdeaSamlException("samlresponse is empty!");
            }
            Response response = (Response) readFromString(samlResponse);
            List<Assertion> assertions = response.getAssertions();
            
            if(assertions.size() < 1){
                throw new IdeaSamlException("no assertion found!");
            }
            if(assertions.size() > 1){
                throw new IdeaSamlException("multiple assertions found!");
            }
            Assertion assertion = assertions.get(0);
            
            NameID nameID = assertion.getSubject().getNameID();
            user.setId(nameID.getValue());
            
            List<String> publicKeys = readAttribution(assertion, "publicKey");
            if(publicKeys != null && publicKeys.size() > 0) {
                user.setPublicKeyStr(publicKeys.get(0));
                user.readPublicKeyFromStr();
            }
                    
            if(validateAssertionSignature(assertion) == false){
                throw new IdeaSamlException("Assertion signature is invalid.");
            }
            
            List<String> sessionIds = readAttribution(assertion, "sessionId");
            if(sessionIds != null && sessionIds.size() > 0) {
                this.sessionId = sessionIds.get(0);
            }
            
            //List<String> usergroup = readAttribution(assertion, UserRole.USERGROUP_KEY);
            //returnuser.setUsergroup(usergroup);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e); 
            e.printStackTrace();
        } catch (UnmarshallingException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e); 
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e); 
            e.printStackTrace();
        }
    }
    
    
    private boolean validateAssertionSignature(Assertion assertion){
        boolean result = false;
        SAMLSignatureProfileValidator samlvalidator = new SAMLSignatureProfileValidator();
        try {
            samlvalidator.validate(assertion.getSignature());
            result = true;
        } catch (ValidationException e) {
            result = false;
            e.printStackTrace();
        }
        BasicCredential credential = new BasicCredential();
        credential.setPublicKey(user.getPublicKey());
        SignatureValidator validator = new SignatureValidator(credential);
        try {
            validator.validate(assertion.getSignature());
            result = true;
            System.out.println("signature is valid!");
        } catch (ValidationException e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }
    
    private List<String> readAttribution(Assertion assertion, String key){
        List<String> result = new ArrayList<String>();
        for (Statement statement : assertion.getStatements ()){
            if (statement instanceof AttributeStatement)
                for (Attribute attribute : 
                        ((AttributeStatement) statement).getAttributes ())
                {
                    if(attribute.getName().equals(key)){
                        for (XMLObject value : attribute.getAttributeValues ())
                            if (value instanceof XSAny)
                                result.add(((XSAny) value).getTextContent());
                    }
                }
        }
        return result;
    }
    
    private Assertion addAttribution(Assertion assertion, String key, List<String> values){
        if(assertion == null || key == null ||values == null){
            return null;
        }
        AttributeStatement statement = create (AttributeStatement.class, 
                AttributeStatement.DEFAULT_ELEMENT_NAME);
                
        addAttribute (statement, key, values);
        assertion.getStatements ().add (statement);
        
        return assertion;
    }
    
    /**
     * @return the user
     */
    public SamlUser getUser() {
        return user;
    }
    
    /**
     * @param user the user to set
     */
    public void setUser(SamlUser user) {
        this.user = user;
    }
    
    public String getSessionId() {
        return this.sessionId;
    }
}
