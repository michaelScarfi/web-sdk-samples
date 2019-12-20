package com.microstrategy.samples.objects.reports;

import com.microstrategy.samples.sessions.SessionManager;
import com.microstrategy.web.beans.WebBeanException;
import com.microstrategy.web.objects.EnumWebReportSourceType;
import com.microstrategy.web.objects.WebFolder;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectInfo;
import com.microstrategy.web.objects.WebObjectSource;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.web.objects.WebReportExecutionSettings;
import com.microstrategy.web.objects.WebReportInstance;
import com.microstrategy.web.objects.WebReportSource;
import com.microstrategy.web.objects.WebReportValidationException;
import com.microstrategy.web.objects.admin.WebObjectsAdminException;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import com.microstrategy.webapi.EnumDSSXMLReportSaveAsFlags;

public class updateDatasetOnReport {
	public static void main(String[] args) throws WebObjectsAdminException, WebObjectsException, WebBeanException, WebReportValidationException {
		
		// Set server connection information
		String intelligenceServerName = "sup-002222.labs.microstrategy.com"; 
		String projectName = "MicroStrategy Tutorial"; 
		String microstrategyUsername =  "administrator";
		String microstrategyPassword = "";
		
		String reportID = "E872BB2E46AFC4D2EA046D84759894E3"; //Report which will have it's dataset updated
		String cubeID = "AF97F9474EA8D219382D87947B4C02CF"; //Cube which will replace the existing dataset on the report
		
		// Create an IServer session
		WebIServerSession iServerSession = SessionManager.getSessionWithDetails(intelligenceServerName, projectName, microstrategyUsername, microstrategyPassword);

		replaceCubeOnReport(iServerSession, reportID, cubeID);
		//createCopyOfCube(iServerSession, cubeID);
		
	}
	
	// Given a reportID, will replace the current cube dataset on the report with the new cubeID
	private static void replaceCubeOnReport(WebIServerSession session, String reportID, String cubeID) throws WebObjectsException { 
		WebObjectsFactory factory = session.getFactory();
		WebObjectSource objectSource = factory.getObjectSource();
		WebReportSource reportSource = factory.getReportSource();
		
		// Set the execution settings for the report to include the updated cubeID as its source
		WebReportExecutionSettings settings = reportSource.newExecutionSettings();
		settings.setSource(cubeID, EnumWebReportSourceType.WebReportSourceTypeCube);
		settings.setReportID(reportID);
		
		// Generate a new report instance by executing the report with the new execution settings.
		WebReportInstance cubeReportSource = reportSource.getNewInstance(settings);
		cubeReportSource.setAsync(false);
		cubeReportSource.pollStatusOnly();
		
		// Retrieve the Shared Reports folder to use for saving
		WebFolder folder = (WebFolder) objectSource.getObject(reportID, EnumDSSXMLObjectTypes.DssXmlTypeReportDefinition).getParent();

		// A new web object for the report which was updated so that this can be saved over the old report
		WebObjectInfo updatedReport = objectSource.getObject(reportID, EnumDSSXMLObjectTypes.DssXmlTypeReportDefinition, true);
		
		//Save to overwrite the report 
		cubeReportSource.setSaveAsFlags(EnumDSSXMLReportSaveAsFlags.DssXmlReportSaveAsOverWrite);
		cubeReportSource.saveAs(folder, updatedReport.getName());

	}
	
	// Given a any cube, create a backup copy in the same location of the source cube.
	private static void createCopyOfCube(WebIServerSession session, String cubeID) throws WebObjectsException, IllegalArgumentException{
		WebObjectSource  objectSource = session.getFactory().getObjectSource();
		WebObjectInfo objectInfo = objectSource.getObject(cubeID, EnumDSSXMLObjectTypes.DssXmlTypeReportDefinition, true);
		WebObjectInfo newSavedObject = objectSource.copy(objectInfo, "copiedCube");
		
		System.out.println("Cube named: " + newSavedObject.getName() + " with ID: " + newSavedObject.getID());
		System.out.println();
	}

}
