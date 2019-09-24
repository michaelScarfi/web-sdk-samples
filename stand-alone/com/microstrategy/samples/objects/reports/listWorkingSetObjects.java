package com.microstrategy.samples.objects.reports;

import com.microstrategy.samples.sessions.SessionManager;
import com.microstrategy.web.objects.WebFolder;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectInfo;
import com.microstrategy.web.objects.WebObjectSource;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebReportInstance;
import com.microstrategy.web.objects.WebReportSource;
import com.microstrategy.web.objects.WebWorkingSet;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;

public class listWorkingSetObjects {

	public static void main(String[] args) {

		// Set server connection information
		String intelligenceServerName = "sup-w-001603.labs.microstrategy.com"; 
		String projectName = "MicroStrategy Tutorial"; 
		String microstrategyUsername =  "administrator";
		String microstrategyPassword = "";

		// Create an IServer session
		WebIServerSession iServerSession = SessionManager.getSessionWithDetails(intelligenceServerName, projectName, microstrategyUsername, microstrategyPassword);

		// Report which will be used
		String reportID = "018FD577479CF15774B163B9FDF477FA";

		try {
			// Call our function which will get all objects available on the working set of a report
			// Takes the IServer session and report ID
			getWSObjects(iServerSession, reportID);
		} catch (WebObjectsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void getWSObjects(WebIServerSession session, String reportID) throws WebObjectsException {
		//Create a report source with the server session
		WebReportSource reportSource = session.getFactory().getReportSource();

		// Get a report instance given a reportID
		WebReportInstance reportInstance = reportSource.getNewInstance(reportID);

		// Get the working set instance for this report
		WebWorkingSet reportWS = reportInstance.getWorkingSet();

		// Get all working set objects within a Web Folder object
		WebFolder wsFolderObjects = reportWS.getWorkingSetObjects();

		// Loop through the Web Folder and output the display name for each object
		WebObjectInfo objectInfo = null;
		if(wsFolderObjects.size() > 0) {
			for(int i = 0; i <wsFolderObjects.size(); i++) {
				objectInfo = wsFolderObjects.get(i);
				System.out.println("   " + objectInfo.getDisplayName());
			}
		}
	}

}
