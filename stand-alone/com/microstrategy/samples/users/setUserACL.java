package com.microstrategy.samples.users;


import com.microstrategy.samples.sessions.SessionManager;
import java.util.Dictionary;
import java.util.Hashtable;

import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectSource;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.admin.WebObjectsAdminException;
import com.microstrategy.web.objects.admin.users.WebPrivilegeCategories;
import com.microstrategy.web.objects.admin.users.WebPrivilegeCategory;
import com.microstrategy.web.objects.admin.users.WebPrivilegeEntry;
import com.microstrategy.web.objects.admin.users.WebUser;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;

public class setUserACL {
	
	public static void main(String[] args) throws WebObjectsAdminException, WebObjectsException {
	
		// Set server connection information
		String intelligenceServerName = "sup-002222.labs.microstrategy.com"; 
		String projectName = "MicroStrategy Tutorial"; 
		String microstrategyUsername =  "administrator";
		String microstrategyPassword = "";
		String userID = "123DE1C247BD70F42980B3B8336EA6CF"; // User which will have privileges modified
		
		// Create an IServer session
		WebIServerSession iServerSession = SessionManager.getSessionWithDetails(intelligenceServerName, projectName, microstrategyUsername, microstrategyPassword);

		verifyPrivileges(iServerSession, userID);
	}
	
	// Given a user, this method will list all privileges for said user and verify if granted.
	// Allows option 
	private static void verifyPrivileges(WebIServerSession session, String userID) throws WebObjectsException, IllegalArgumentException {
        WebObjectSource  objectSource = session.getFactory().getObjectSource();
        WebUser user = (WebUser) objectSource.getObject(userID, EnumDSSXMLObjectTypes.DssXmlTypeUser);
        WebPrivilegeCategories privilegeCategories = objectSource.getUserServicesSource().getPrivilegeCategories(user);
        
        for (int i = 0; i < privilegeCategories.size(); i++) {
            WebPrivilegeCategory category = privilegeCategories.get(i);
            
            // Create a Dictionary for all the Privilege categories so that the category name can be mapped correctly
            Dictionary<String, String> categoryDict = new Hashtable<String, String>();
            categoryDict.put("mstrWeb.16665", "Client - Application - Tableau");
            categoryDict.put("mstrWeb.16666", "Client - Application - PowerBI");
            categoryDict.put("mstrWeb.16667", "Client - Application - Qlik");
            categoryDict.put("mstrWeb.16668", "Client - Application - Office");
            categoryDict.put("mstrWeb.16669", "Client - Application - Jupyter");
            categoryDict.put("mstrWeb.16670", "Client - Application - RStudio");
            categoryDict.put("mstrWeb.16302", "Client - Hyper - Web");
            categoryDict.put("mstrWeb.16671", "Client - Hyper - Mobile");
            categoryDict.put("mstrWeb.16672", "Client - Hyper - Office");
            categoryDict.put("mstrWeb.16168", "Client - Reporter");
            categoryDict.put("mstrWeb.16169", "Client - Web");
            categoryDict.put("mstrWeb.16170", "Client - Application");
            categoryDict.put("mstrWeb.16171", "Client - Mobile");
            categoryDict.put("mstrWeb.16172", "Client - Architect");
            categoryDict.put("mstrWeb.16173", "Server - Reporter");
            categoryDict.put("mstrWeb.16174", "Server - Intelligence");
            categoryDict.put("mstrWeb.16175", "Server - Analytics");
            categoryDict.put("mstrWeb.16176", "Server - Collaboration");
            categoryDict.put("mstrWeb.16177", "Server - Distribution");
            categoryDict.put("mstrWeb.16178", "Server - Transaction");
            
            // Format the string returned by getName() to be of format: mstrWeb.XXXXX
            String categoryKey = category.getName();;
            categoryKey = categoryKey.substring(categoryKey.indexOf("'") + 1);
            categoryKey = categoryKey.substring(0, categoryKey.indexOf("'"));
            
            // Given a formatted getName() we can return a valid category name
            String categoryName = categoryDict.get(categoryKey);
            
            System.out.println("Category: " + categoryName + "\n");
            
            // Retrieving individual privileges within a category
            for (int k = 0; k < category.size(); k++) {
            	WebPrivilegeEntry entry = category.get(k);
            	String entryName = entry.getName();
            	Boolean granted = entry.isPrivilegeGranted();
            	
                System.out.println("Privilege: " + entryName + " - Granted: " + granted);
                // *** OPTIONAL ***
                // Revoking all Server - Intelligence privileges for a user 
                if(categoryName == "Server - Intelligence" && granted == true) {
                	entry.revoke();
                	System.out.println("     Revoked privilege: " + entryName);
                }
                
                // *** OPTIONAL ***
                // Granting all Server - Intelligence privileges for a user
                /*
                if(categoryName == "Server - Intelligence" && granted == false) {
                	entry.grant();
                	System.out.println("     Granted privilege: " + entryName);
                }
                */
            }
            
            System.out.println("+++++++++++ \n");
        }
        objectSource.save(user); // Apply and save changes made to the user
        
	}
	
	
}