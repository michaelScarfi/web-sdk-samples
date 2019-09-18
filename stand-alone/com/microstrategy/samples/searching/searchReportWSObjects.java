import com.microstrategy.web.objects.SimpleList;
import com.microstrategy.web.objects.WebFolder;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectInfo;
import com.microstrategy.web.objects.WebObjectSource;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.web.objects.WebReportInstance;
import com.microstrategy.web.objects.WebReportSource;
import com.microstrategy.web.objects.WebWorkingSet;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;

public class searchReportWSObjects {

	public static void main(String[] args) {
		
		// Create an IServer session
		WebObjectsFactory objectfactory = WebObjectsFactory.getInstance();
		WebIServerSession iServerSession = objectfactory.getIServerSession();
	    
		// Set server connection information
	    iServerSession.setServerName("servername.microstrategy.com"); 
	    iServerSession.setServerPort(0);
	    iServerSession.setProjectName("MicroStrategy Tutorial"); 
	    iServerSession.setLogin("administrator");
	    iServerSession.setPassword("password");
	    
	    String reportID = "018FD577479CF15774B163B9FDF477FA";
		
		try {
			// Call our function which will get all objects available on the working set of a report
			getWSObjects(iServerSession, reportID);
		} catch (WebObjectsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// getWSObjects is used to gather all objects found on the report's working set. 
	// Requires an active IServer session as well as a given report ID
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
