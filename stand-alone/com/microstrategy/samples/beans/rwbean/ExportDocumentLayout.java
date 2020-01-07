package com.microstrategy.samples.beans.rwbean;

import com.microstrategy.samples.sessions.SessionManager;
import com.microstrategy.samples.util.FileHelper;
import com.microstrategy.web.beans.BeanFactory;
import com.microstrategy.web.beans.RWBean;
import com.microstrategy.web.beans.WebBeanException;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.rw.EnumRWExecutionModes;
import com.microstrategy.web.objects.rw.EnumRWExportModes;
import com.microstrategy.web.objects.rw.RWDefinition;
import com.microstrategy.web.objects.rw.RWExportSettings;
import com.microstrategy.web.objects.rw.RWInstance;
import com.microstrategy.web.objects.rw.RWManipulation;

public class ExportDocumentLayout {

  public static void main(String[] args) {
	// Connectivity for the intelligence server
    String intelligenceServerName = "10.23.5.242";
    String projectName = "MicroStrategy Tutorial";
    String microstrategyUsername = "Administrator";
    String microstrategyPassword = "";
    
    // Create our I-Server Session
    WebIServerSession session = SessionManager.getSessionWithDetails(intelligenceServerName, projectName, microstrategyUsername, microstrategyPassword);
    
    //Document ID
    String documentID = "C0AEF3624E8FE0A8E5CBC09ACBF51FA2";
    
    //Path to save pdf file needs to be provided via "pathToSavePDFFile" var.
    String pathToSavePDFFile = "< path/to/save/pdf/file >";
    
    //Layout to export.
    String layoutName = "Layout 01";
    
	try {
		byte[] exportLayoutResults = exportDocumentLayoutToPDF(documentID, session, layoutName);
		
		//Saving export to file.
		FileHelper.saveByteArrayToFile(exportLayoutResults, pathToSavePDFFile);
		
		System.out.println("Files saved to: " + pathToSavePDFFile);
	} catch (WebObjectsException e) {
		e.printStackTrace();
	}
  }
	
	
  public static byte[] exportDocumentLayoutToPDF(String documentID, WebIServerSession session, String layoutName ) throws WebObjectsException {
	System.out.println("Exporting layout.");
	
	RWBean rwb = (RWBean)BeanFactory.getInstance().newBean("RWBean");

    // ID of the object we wish to execute
    rwb.setObjectID(documentID);
    
    // session used for execution
    rwb.setSessionInfo(session);
    RWInstance rwInstance = null;
    RWInstance rwInstanceToExport = null;
    String layoutToExportKey = null;
	try {
		
		rwInstance = rwb.getRWInstance();

		RWDefinition rwDefinition = rwInstance.getDefinition();

		//Get layout key from layout name.
		layoutToExportKey = getLayoutKeyFromName(rwDefinition, layoutName);
		
		RWManipulation rwManipulation = rwInstance.getRWManipulator(true);
		rwInstanceToExport = rwManipulation.setCurrentLayout(layoutToExportKey);
	    
		// set execution mode to PDF export
	    rwb.setExecutionMode(EnumRWExecutionModes.RW_MODE_PDF);
	    
	    RWExportSettings exportSettings = rwInstance.getExportSettings();
	    exportSettings.setMode(EnumRWExportModes.RW_EXPORT_CURRENT_LAYOUT);
	    
	    //Switch the current layout.
	    rwInstanceToExport.setCurrentLayoutKey(layoutToExportKey);
	    rwInstanceToExport.setAsync(false);
	    rwInstanceToExport.pollStatus();
		
		} catch (WebBeanException | WebObjectsException e) {
		e.printStackTrace();
	}
	
    
    //return rwInstance.getExportData();
    return rwInstanceToExport.getExportData();

  }


  public static String getLayoutKeyFromName(RWDefinition rwDefinition, String layoutName) {
	String layoutKey = null;
	String iterationName = null;
	int layoutIteration = 0;
	
	do {
		iterationName = rwDefinition.getLayout(layoutIteration).getName();
		layoutKey = rwDefinition.getLayout(layoutIteration).getKey();
		layoutIteration++;
	}
	while( !(layoutName.equals(iterationName)));
	
	return layoutKey;
  }

}
