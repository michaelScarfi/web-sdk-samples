package com.microstrategy.samples.sessions;

import com.microstrategy.web.objects.*;
import com.microstrategy.webapi.EnumDSSXMLApplicationType;
import com.microstrategy.webapi.EnumDSSXMLAuthModes;

/*
 * This sample uses trusted authentication to create a session without a password.
 * 
 * Pre-requisites:
 * - There must be an active trust relationship between MicroStrategy Web and the Intelligence Server(s).
 * - The trust token must be extracted from the MicroStrategy Web server. 
 * - The user must be configured with a trusted user ID, or the server must be configured to allow login for users that are not linked.
 */
public class TrustedAuthentication {
    
    private static String INTELLIGENCE_SERVER = "APS-TSIEBLER-VM";
    private static String PROJECT_NAME = "MicroStrategy Tutorial";
    
    /*
     * The user ID must be set in the user object. 
     * This can be done by editing the user in MicroStrategy Developer and navigating to Authentication > Metadata > Trusted Authentication Request.
     */
    private static String TRUSTED_USER_ID = "user1";
    
    /*
     * - The trust token can be found in the WEB-INF/xml/ folder of MicroStrategy Web. 
     * - In the below example, the token file is called "APS-TSIEBLER-VM.token" and contains the "TRUST_TOKEN_VALUE" shown below.
     */
    private static String TRUST_TOKEN_VALUE = "Token7E64395E4ED9CE0FDF9E3C8800E3E8E4";
    
    public static void main(String[] args) {
        try {
            WebIServerSession mySession = getSessionWithToken(INTELLIGENCE_SERVER, PROJECT_NAME, TRUSTED_USER_ID, TRUST_TOKEN_VALUE);
        } catch (WebObjectsException ex) {
            System.out.println("getSessionWithToken - Error creating session - check DSSErrors.log authentication trace for details: " + ex.getMessage());
        }
    }
    
    public static WebIServerSession getSessionWithToken(String intelligenceServerName, String projectName, String trustedUserID, String trustTokenValue) throws WebObjectsException {
        WebIServerSession serverSession = WebObjectsFactory.getInstance().getIServerSession();
        
        serverSession.setApplicationType(EnumDSSXMLApplicationType.DssXmlApplicationCustomApp);
        serverSession.setAuthMode(EnumDSSXMLAuthModes.DssXmlAuthSimpleSecurityPlugIn);
        serverSession.setTrustToken(trustTokenValue);

        // Intelligence server and project
        serverSession.setServerName(intelligenceServerName);
        serverSession.setProjectName(projectName);
        
        // This is the trusted User ID (NOT the username)
        serverSession.setLogin(trustedUserID);

        System.out.println("getSessionWithToken - Session created with ID: " + serverSession.getSessionID());

        return serverSession;
    }

}
