package com.microstrategy.samples.sessions;

import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.webapi.EnumDSSXMLApplicationType;
import com.microstrategy.webapi.EnumDSSXMLSessionFlags;

public class AuthenticateBypassClustering {
    // Connectivity for the intelligence server
    final static String intelligenceServerName = "10.23.1.51";
    final static String projectName = "MicroStrategy Tutorial";
    final static String microstrategyUsername = "Administrator";
    final static String microstrategyPassword = "";

    public static WebIServerSession serverSession = null;
    private static WebObjectsFactory factory = null;

    public static void main(String[] args) {


        // Create our I-Server Session
        WebIServerSession session = AuthenticateBypassClustering.getSessionBypassClustering(intelligenceServerName, projectName,
            microstrategyUsername, microstrategyPassword);

    }

    /**
     * This method connects to the requested Intelligence Server and bypasses clustering, which would normally redirect the user session. * @return
     */
    public static WebIServerSession getSessionBypassClustering(String serverName, String projectName, String login, String password) {
        // create factory object
        factory = WebObjectsFactory.getInstance();
        serverSession = factory.getIServerSession();

        // Set up session properties
        serverSession.setServerName(serverName); // Should be replaced with the name of an Intelligence Server // aps-tsiebler-vm

        if (projectName != null) {
            // Project where the session should be created
            serverSession.setProjectName(projectName);
        }

        serverSession.setLogin(login); // User ID
        serverSession.setPassword(password); // Password

        serverSession.setApplicationType(EnumDSSXMLApplicationType.DssXmlApplicationCustomApp);

        // sets the I-Server to bypass clustering
        serverSession.setSessionFlags(EnumDSSXMLSessionFlags.DssXmlSessionBypassClustering);

        // Initialize the session with the above parameters
        try {
            System.out.println("_getSessionWithDetails - Session created with ID: " + serverSession.getSessionID());
        } catch (WebObjectsException ex) {
            System.out.println("_getSessionWithDetails - Error creating session:" + ex.getMessage());
        }
        return serverSession;
    }



}
