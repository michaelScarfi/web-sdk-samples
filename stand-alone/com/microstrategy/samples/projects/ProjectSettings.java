package com.microstrategy.samples.projects;

import com.microstrategy.samples.serverconfiguration.UpdateServerDefinitionSettings;
import com.microstrategy.samples.sessions.SessionManager;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectSource;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.web.objects.WebProjectReference;
import com.microstrategy.web.objects.WebProjectReferences;
import com.microstrategy.web.objects.WebProjectSetting;
import com.microstrategy.web.objects.WebProjectSettings;
import com.microstrategy.web.objects.WebServerDef;
import com.microstrategy.webapi.EnumDSSXMLServerProjectSettingID;

public class ProjectSettings {

	/*
	 * Demonstration on how to change project settings.
	 * These settings are located under Project Configuration -> Governing Rules
	 */
	public static void main(String[] args) {

		// Connectivity for the intelligence server
		String intelligenceServerName = "10.23.1.124";
		String projectName = "MicroStrategy Tutorial";
		String microstrategyUsername = "Administrator";
		String microstrategyPassword = "";

		// Obtain IServer session
		WebIServerSession session = SessionManager.getSessionWithDetails(intelligenceServerName, projectName,
		    microstrategyUsername, microstrategyPassword);

		/*
		 * List of project settings are defined by EnumDSSXMLServerProjectSettingID.
		 * https://lw.microstrategy.com/msdz/MSDL/GARelease_Current/docs/ReferenceFiles/reference/com/microstrategy/webapi/EnumDSSXMLServerProjectSettingID.html
		 */

		// Project's setting to modify
		int projectSettingKey = EnumDSSXMLServerProjectSettingID.DssXmlServerProjectMaxResultRowCount;
		String settingNewValue = "16000";

		WebObjectsFactory factory = session.getFactory();
		WebObjectSource webObjectSource = factory.getObjectSource();
		try {
			WebServerDef serverDef = UpdateServerDefinitionSettings.getServerDefForCurrentSession(webObjectSource);

			// Setting new value for the setting.
			SetNewProjectSetting(serverDef, projectName, projectSettingKey, settingNewValue);

			// Important: need to save setting using the ObjectSource object.
			webObjectSource.save(serverDef);
			System.out.println("Setting changed.");

		} catch (WebObjectsException e) {
			e.printStackTrace();
		}

	}

	public static void SetNewProjectSetting(WebServerDef serverDef, String projectName, int projectSetting,
	    String newValue) {
		WebProjectReferences webProjectReferences = serverDef.getProjectReferences();
		WebProjectReference webProjectReference = webProjectReferences.itemByName(projectName);
		WebProjectSettings webProjectSettings = webProjectReference.getProjectSettings();
		WebProjectSetting webProjectSetting = webProjectSettings.get(projectSetting);
		System.out.println("Current value [" + webProjectSetting.getDataType() + "]: " + webProjectSetting.getValue());

		/*
		 * Setting new value. Data type of the setting must be taken into account. See
		 * link to documentation below (setValue()).
		 * https://lw.microstrategy.com/msdz/MSDL/GARelease_Current/docs/ReferenceFiles/reference/com/microstrategy/web/objects/WebProjectSetting.html#setValue(java.lang.String)
		 */

		webProjectSetting.setValue(newValue);
		System.out.println("New value [" + webProjectSetting.getDataType() + "]:" + webProjectSetting.getValue());
	}

}
