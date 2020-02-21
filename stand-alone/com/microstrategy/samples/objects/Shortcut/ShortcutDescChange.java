package com.microstrategy.samples.objects.Shortcut;

/*
 * SDK sample
 * Purpose: to change the description field of a shortcut object
 * 
 * Created by: Michael Scarfi
 * Creation date: 2/20/2020
 */


//SDK sample package
import com.microstrategy.samples.sessions.SessionManager;

//MicroStrategy SDK packages
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectSource;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebShortcut;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;


public class ShortcutDescChange {
	
	public static void main(String[] args) {
		
		// Connectivity for the intelligence server
	    String intelligenceServerName = "iserver.name";
	    String projectName = "MicroStrategy Tutorial";
	    String microstrategyUsername = "Administrator";
	    String microstrategyPassword = "";
	    
	    // Shortcut ID
	    String shortcutID = "3B32AEBA4A38426E6E0A588F5C938C5B";
	    
	    // Create our I-Server Session
	    WebIServerSession session = SessionManager.getSessionWithDetails(intelligenceServerName, projectName, microstrategyUsername, microstrategyPassword);
	    
	    // New shortcut description
	    String desc = "our new description!";
	    
	    //Reads current description, changes description, and reads new description.
	    try {
	    	System.out.println("Previous Description: " + getShorDesc(session, shortcutID));
	    	
	    	System.out.println("Setting new Description to: " + desc);
	    	setShorDesc(session, shortcutID, desc);
	    	
	    	System.out.println("New Description: " + getShorDesc(session, shortcutID));
	    } catch (Exception e) {
	    	System.out.println(e.getMessage());
	    }
	  
	}
	
	private static String getShorDesc(WebIServerSession session, String id) throws WebObjectsException {
		 //Create the factory and initialize the web object source
	    WebObjectSource webObjectSource = session.getFactory().getObjectSource();
      
	    //Load and populate the shortcut object
	    WebShortcut shortcut = (WebShortcut)webObjectSource.getObject(id,EnumDSSXMLObjectTypes.DssXmlTypeShortcut); 
	    shortcut.populate();
	    
	    //Return current Description string
	    return shortcut.getDescription();
	}
	
	private static void setShorDesc(WebIServerSession session, String id, String desc) throws WebObjectsException {
		//Create the factory and initialize the web object source
	    WebObjectSource webObjectSource = session.getFactory().getObjectSource();
     
	    //Load and populate the shortcut object
	    WebShortcut shortcut = (WebShortcut)webObjectSource.getObject(id,EnumDSSXMLObjectTypes.DssXmlTypeShortcut); 
	    shortcut.populate();
	    
	    //Change Description to value of String desc
	    shortcut.setDescription(desc);
	    
	    //Save changes to object
	    webObjectSource.save(shortcut);
	}

}
