package com.microstrategy.samples.subscriptions;

import com.microstrategy.samples.sessions.SessionManager;
import com.microstrategy.utils.Tree;
import com.microstrategy.web.objects.*;
import com.microstrategy.webapi.*;

public class CreateScheduleEvent {

    public static void main(String[] args) {
        // A schedule is a configuration-level object (not project level). Thus the session must be without a project too.
        String intelligenceServerName = "10.27.72.72";
        String projectName = "";
        String microstrategyUsername = "administrator";
        String microstrategyPassword = "";
        
        // Establishes a connection to the Intelligence Server
        WebIServerSession serverSession = SessionManager.getSessionWithDetails(intelligenceServerName, projectName, microstrategyUsername, microstrategyPassword);
        
        try {
            saveNewScheduleEvent(serverSession);
        } catch (WebObjectsException | IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Create a new event and save it in the events folder 
     * In Developer, find this in Administration -> Configuration Managers -> Events
     * @param serverSession
     * @throws WebObjectsException
     * @throws IllegalArgumentException
     */
    public static void saveNewScheduleEvent(WebIServerSession serverSession) throws WebObjectsException, IllegalArgumentException {
        WebObjectSource wos = serverSession.getFactory().getObjectSource();
        
        String eventsFolderId = wos.getFolderID(EnumDSSXMLFolderNames.DssXmlFolderNameEvents);
        WebFolder eventsFolder = (WebFolder) wos.getObject(eventsFolderId, EnumDSSXMLObjectTypes.DssXmlTypeFolder, true);

        WebScheduleEvent eventSchedule = (WebScheduleEvent) wos.getNewObject(EnumDSSXMLObjectTypes.DssXmlTypeScheduleEvent);
        eventSchedule.setDescription("This is a new event created using the Web SDK");
        
        wos.saveAs(eventSchedule, "My New Event Name", eventsFolder, true);
        
        System.out.println("New event saved with ID: " + eventSchedule.getID());
    }

}
