package com.microstrategy.samples.objects.SecurityFilter;

import com.microstrategy.samples.sessions.SessionManager;
import com.microstrategy.web.objects.WebExpression;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebFolder;
import com.microstrategy.web.objects.WebObjectSource;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.admin.users.WebMDSecurityFilter;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import com.microstrategy.webapi.EnumDSSXMLFolderNames;

public class SecurityFilterCreation {

	public static void main(String[] args) {	
		// Connectivity for the intelligence server
		String intelligenceServerName = "sup-w-001643.labs.microstrategy.com";
	    String projectName = "MicroStrategy Tutorial";
	    String microstrategyUsername = "Administrator";
		String microstrategyPassword = "";
        String filterName = "test filter";
		String expression = "[Employee Age]@ID=23";

	    // Create our I-Server Session
	    WebIServerSession session = SessionManager.getSessionWithDetails(intelligenceServerName, projectName, microstrategyUsername, microstrategyPassword);
	    
		CreateSecurityFilter(session, expression, filterName);
	}

	public static void CreateSecurityFilter(WebIServerSession session, String expression, String filterName){
		WebObjectSource webObjectSource = session.getFactory().getObjectSource();
	    
	    WebMDSecurityFilter wMDFilter = (WebMDSecurityFilter) webObjectSource.getNewObject( EnumDSSXMLObjectTypes.DssXmlTypeMDSecurityFilter );
		WebExpression wExpr = wMDFilter.getExpression();
		
		try {
            String folderID = webObjectSource.getFolderID(EnumDSSXMLFolderNames.DssXmlFolderNameSystemMDSecurityFilters);
            WebFolder folder = (WebFolder) webObjectSource.getObject(folderID, EnumDSSXMLObjectTypes.DssXmlTypeFolder);

            wExpr.populate(expression);
            webObjectSource.save(wMDFilter, filterName, folder);
		} catch (WebObjectsException e) {
			e.printStackTrace();
		} 

	}

}