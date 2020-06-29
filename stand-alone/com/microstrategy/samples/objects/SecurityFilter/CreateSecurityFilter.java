package com.microstrategy.samples.objects.WebExpression;

import com.microstrategy.samples.sessions.SessionManager;
import com.microstrategy.web.objects.WebExpression;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebFolder;
import com.microstrategy.web.objects.WebObjectSource;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.admin.users.WebMDSecurityFilter;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import com.microstrategy.webapi.EnumDSSXMLFolderNames;

public class Create_Web_Expression {

	public static void main(String[] args) {	
		// Connectivity for the intelligence server
		String intelligenceServerName = "sup-w-001643.labs.microstrategy.com";
	    String projectName = "MicroStrategy Tutorial";
	    String microstrategyUsername = "Administrator";
	    String microstrategyPassword = "";
        String value = "23";
        String filterName = "test filter";
			    
	    // Create our I-Server Session
	    WebIServerSession session = SessionManager.getSessionWithDetails(intelligenceServerName, projectName, microstrategyUsername, microstrategyPassword);
	    WebObjectSource webObjectSource = session.getFactory().getObjectSource();
	    
	    WebMDSecurityFilter wkFilter = (WebMDSecurityFilter) webObjectSource.getNewObject( EnumDSSXMLObjectTypes.DssXmlTypeMDSecurityFilter );
        WebExpression wkExpr = wkFilter.getExpression();
        
        

        try {
            String folderID = webObjectSource.getFolderID(EnumDSSXMLFolderNames.DssXmlFolderNameSystemMDSecurityFilters);
            WebFolder folder = (WebFolder) webObjectSource.getObject(folderID, EnumDSSXMLObjectTypes.DssXmlTypeFolder);

            wkExpr.populate( "[Employee Age]@ID=" + value);
            webObjectSource.save(wkFilter, filterName, folder);
		} catch (WebObjectsException e) {
			e.printStackTrace();
		} 
        
        
	    
	    
	}

}