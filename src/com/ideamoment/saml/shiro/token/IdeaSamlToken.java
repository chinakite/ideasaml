/**
 * 
 */
package com.ideamoment.saml.shiro.token;

import org.apache.shiro.authc.RememberMeAuthenticationToken;

import com.ideamoment.saml.model.SamlUser;


/**
 * @author Chinakite
 *
 */
public class IdeaSamlToken implements RememberMeAuthenticationToken {

    private static final long serialVersionUID = -7367764085109288043L;
    
    
    private SamlUser user;
    private boolean rememberMe = false;
    
    public IdeaSamlToken(SamlUser user, boolean rememberMe) {
        this.user = user;
        this.rememberMe = rememberMe;
    }
    
    @Override
    public Object getPrincipal() {
        return user.getId();
    }

    @Override
    public Object getCredentials() {
        return user.getId();
    }

    @Override
    public boolean isRememberMe() {
        return rememberMe;
    }
    
    public void setRememberMe(boolean value) {
        this.rememberMe = value;
    }

    
    /**
     * @return the user
     */
    public SamlUser getUser() {
        return user;
    }
    
    
}
