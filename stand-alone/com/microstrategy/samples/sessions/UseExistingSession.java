package com.microstrategy.samples.sessions;

import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;

public class UseExistingSession {
    private static String DEFAULT_SERVER = "APS-TSIEBLER-VM";
    private static String DEFAULT_PROJECT = "Human Resources Analysis Module";
    private static String DEFAULT_USERNAME = "Administrator";
    private static String DEFAULT_PASSWORD = "";
    private static WebObjectsFactory factory = null;

    /*
     * Simple demonstration on reusing a session as a "session state" string, which can be used with other requests (e.g the task API or URL API).
     */
    public static void main(String[] args) {

        try {
            // create a session in any way
            WebIServerSession existingSession = SessionManager.getSessionWithDetails(DEFAULT_SERVER, DEFAULT_PROJECT, DEFAULT_USERNAME,
                DEFAULT_PASSWORD);
            System.out.println("-------------");

            // check the status of the existing session as a baseline
            System.out.println("Checking session information of original WebIServerSession object");
            CheckSessionStatus(existingSession);
            System.out.println("-------------");

            System.out.println("Setting original Session ID to new session");
            String existingSessionID = existingSession.getSessionID();
            WebIServerSession newSession = RestoreSessionID(existingSessionID);

            // validate the session has been duplicated / updated
            CheckSessionStatus(newSession);
            System.out.println("-------------");
      
            // Restore Session
            System.out.println("Restoring the original session into a WebIServerSession object using sessionState");
            String existingSessionState = existingSession.saveState();
            
            // Restoring the original Session using sessionState, which is commonly utilized in ESMs and can also be used in URL API, Task API, etc.
            WebIServerSession restoredSession = RestoreSessionState(existingSessionState);
            System.out.println("Checking status of restored session");
            CheckSessionStatus(restoredSession);

            System.out.println("EOF");
        } catch (WebObjectsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Takes a WebIServerSession and prints out the session ID and if the session is still alive
     * 
     * @param session
     */
    public static void CheckSessionStatus(WebIServerSession session) {
        try {
            System.out.println("Session ID: " + session.getSessionID());
            boolean isAlive = session.isAlive();
            if (isAlive) {
                System.out.println("Session is alive");
            } else {
                System.out.println("Session is not alive");
            }

        } catch (WebObjectsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Create the new WebIServerSession object and update it to utilize the session from an existing String sessionID
     * 
     * @param sessionID
     * @return
     */
    public static WebIServerSession RestoreSessionID(String sessionID) {
        factory = WebObjectsFactory.getInstance();
        WebIServerSession newSession = factory.getIServerSession();
        newSession.setSessionID(sessionID);
        return newSession;
    }

    
    /**
     * Restore the existing WebIServerSession utilizing a sessionState which is commonly used in ESMs and can also be used in URL API, Task API, etc.
     * @param sessionState
     * @return
     */
    public static WebIServerSession RestoreSessionState(String sessionState) {
        // https://lw.microstrategy.com/msdz/MSDL/GARelease_Current/docs/projects/WebSDK/Content/topics/sso/SSO_SSOSample_ESMCodeExpl.htm
        WebIServerSession restoredSession = WebObjectsFactory.getInstance().getIServerSession();
        restoredSession.restoreState(sessionState);
        return restoredSession;
    }

}
