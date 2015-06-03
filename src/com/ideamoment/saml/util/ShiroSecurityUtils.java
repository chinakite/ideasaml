/**
 * 
 */
package com.ideamoment.saml.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;


/**
 * @author Chinakite
 *
 */
public class ShiroSecurityUtils extends SecurityUtils {
    /**
     * 
     * @param sessionId
     * @return
     */
    public static Subject getSubject(String sessionId) {
        Subject subject = ThreadContext.getSubject();
        if (subject == null) {
            subject = (new Subject.Builder()).sessionId(sessionId).buildSubject();
            ThreadContext.bind(subject);
        }
        return subject;
    }
}
