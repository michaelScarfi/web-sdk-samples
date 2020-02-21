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
	    final String INTELLIGENCE_SERVER_NAME = "sup-w-001643.labs.microstrategy.com";
	    final String PROJECT_NAME = "MicroStrategy Tutorial";
	    final String MICROSTRATEGY_USERNAME = "Administrator";
	    final String MICROSTRATEGY_PASSWORD = "";
	    
	    // Shortcut ID
	    final String SHORTCUT_ID = "3B32AEBA4A38426E6E0A588F5C938C5B";
	   
		// New shortcut description
		final String NEW_DESC = "our new description!";
		
	    // Create our I-Server Session
		final WebIServerSession SESSION = SessionManager.getSessionWithDetails(INTELLIGENCE_SERVER_NAME, PROJECT_NAME, MICROSTRATEGY_USERNAME, MICROSTRATEGY_PASSWORD);
		
		// Create WebObjectSource
		final WebObjectSource WEB_OBJECT_SOURCE = SESSION.getFactory().getObjectSource();
	    
	   	
	    
	    //Reads current description, changes description, and reads new description.
	    try {

			//Load and populate the shortcut object
			final WebShortcut SHORTCUT = getWebShortcut(WEB_OBJECT_SOURCE, SHORTCUT_ID);
			
			//Print the current value of the shortcuts description
	    	System.out.println("Previous Description: " + SHORTCUT.getDescription());
			
			//Print the new description and change the shortcut's description to that value.
	    	System.out.println("Setting new Description to: " + NEW_DESC);
	    	setShorDesc(WEB_OBJECT_SOURCE, SHORTCUT, NEW_DESC);
			
			//This confirms the change was saved by grabbing the shorcut again from the metadata
			final WebShortcut TEST_SHORTCUT = getWebShortcut(WEB_OBJECT_SOURCE, SHORTCUT_ID);
	    	System.out.println("New Description: " + TEST_SHORTCUT.getDescription());
	    } catch (Exception e) {
	    	System.out.println(e.getMessage());
	    }
	  
	}
	
	
	/**
     * Takes a WebObjectSource object, WebShortcut Object and String desc and changes the Destription of the WebShortcut object to the value of desc
     * 
     * @param objectSource Object of type WebObjectSource
     * @param shortcut Object of type WebShortcut
	 * @param desc String containing the new Description
     * @throws WebObjectsException
     */
	private static void setShorDesc(WebObjectSource objectSource, WebShortcut shortcut, String desc) throws WebObjectsException {
	    //Change Description to value of String desc
	    shortcut.setDescription(desc);
	    
	    //Save changes to object
	    objectSource.save(shortcut);
	}

	/**
	 * Creates, populates, and returns an object of type WebShortcut
	 * 
	 * @param objectSource Object of type WebObjectSource
	 * @param id String of the shortcut objects ID
	 * @throws WebObjectsException
	 */
	private static WebShortcut getWebShortcut(WebObjectSource objectSource, String id) throws WebObjectsException{
		WebShortcut shortcut = (WebShortcut)objectSource.getObject(id,EnumDSSXMLObjectTypes.DssXmlTypeShortcut); 
		shortcut.populate();
		return shortcut;
	}

}
