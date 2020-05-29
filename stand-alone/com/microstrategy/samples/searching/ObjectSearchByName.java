package com.microstrategy.samples.searching;

/*
 * SDK sample
 * Purpose: to find a user's ID using their name
 * 
 * Notes: The method searchObject can be used as a standalone function
 * The searchObject method will only search for one type of object as defined by int objectType (see notes for values)
 * 
 * Created by: Michael Scarfi
 * Creation date: 5/29/2020
 */

//SDK sample package
import com.microstrategy.samples.sessions.SessionManager;

//Import MicroStrategy Packages
import com.microstrategy.webapi.*;
import com.microstrategy.web.objects.*;

public class ObjectSearchByName {

	public static void main(String[] args) {
	    // Connectivity for the intelligence server
	    final String intelligenceServerName = "sup-w-001643.labs.microstrategy.com";
	    final String projectName = "MicroStrategy Tutorial";
	    final String microstrategyUsername = "Administrator";
	    final String microstrategyPassword = "";
	    
	    /**
	     * This determines the type of object for the search, currently it is set to attribute
	     * Changing DSSXML will allow you to choose what type of object the search will return
	     * Values for this can be found at https://lw.microstrategy.com/msdz/MSDL/GARelease_Current/docs/ReferenceFiles/reference/com/microstrategy/webapi/EnumDSSXMLObjectSubTypes.html
	     */
	    final int objectType = EnumDSSXMLObjectSubTypes.DssXmlSubTypeAttribute;
	    
	    // Object Name for search
	    String searchName = "test";
	    
	    // Create our I-Server Session
	    WebIServerSession session = SessionManager.getSessionWithDetails(intelligenceServerName, projectName, microstrategyUsername, microstrategyPassword);
	    
	    // Find User and output search results
	    String[] objectIDs = searchObject(session, searchName, objectType);
	    
	    // As multiple objects can have the same name, this iterates through returned values and displays each
	    for (int i = 0; i < objectIDs.length; i++) {
	    	System.out.println("Object ID: " + objectIDs[i]);
	    }
	    
	}
	 
	/**
	 * Takes a WebIServerSession object, and a String name and searches for the IDs of objects that matche the name provided
	 * 
	 * @param session Object of type WebIServerSession
	 * @param searchName String containing the search String
	 * @param objectType int containing the int value of the object type for the search
	 * @return String[] containing the object IDs, if none are found first string value will equal "no objects found"
	 */
	private static String[] searchObject(WebIServerSession session, String searchName, int objectType) {
		//Create and populate Factory
		WebObjectSource webObjectSource = session.getFactory().getObjectSource();
		
		//Create blank user and the search object
		WebObjectInfo object = null;
		WebSearch search = webObjectSource.getNewSearchObject();
		
		//Create empty String to store search results
		String[] objectIDs = {"no objects found"};
		
		try {
			//Populate search parameters
			search.setNamePattern(searchName);
			search.setSearchFlags(search.getSearchFlags() | EnumDSSXMLSearchFlags.DssXmlSearchNameWildCard);
			
			//asynchronized search
			search.setAsync(false);

			//Set Object type for search
			search.types().add(new Integer(objectType));

			//search on repository
			search.setDomain(EnumDSSXMLSearchDomain.DssXmlSearchDomainRepository);
			search.submit();
			
			//Create WebFolder object to store search results
			WebFolder f = search.getResults();
			
			//Finding size of the search results since this value is used multiple times.
			int size = f.size();

			//iterate through search results and add to objectIDs
			if (size > 0) {
				System.out.println("found something");
				objectIDs = new String[size];
				for(int i = 0; i < f.size();i++) {
					object = (WebObjectInfo) f.get(i);
					object.populate();
					objectIDs[i] = object.getID();
				}
			}
			
		} catch (Exception e) {
			//print error message to console if an exception occurs.
			System.out.println(e.getMessage());
		}
		
		return objectIDs;
		
	}

}
