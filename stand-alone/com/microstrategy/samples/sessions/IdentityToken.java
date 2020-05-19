package com.microstrategy.samples.sessions;

import com.microstrategy.utils.serialization.EnumWebPersistableState;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.web.preferences.Preferences;
import com.microstrategy.web.preferences.PreferencesException;
import com.microstrategy.web.preferences.PreferencesMgr;

public class IdentityToken {
	private static String DEFAULT_SERVER = "10.23.1.124";
	private static String DEFAULT_PROJECT = "MicroStrategy Tutorial";
	private static String DEFAULT_USERNAME = "Administrator";
	private static String DEFAULT_PASSWORD = "";

	// Secret Key to generate session.
	// private static String IDENTITY_TOKEN_SECRET_KEY =
	// "ThisIsSampleIdentityTokenSecretKey";

	public static void main(String[] args) {

		// Setup the secret key
		setUpSecretKeyPreferences();

		// Create session.
		WebIServerSession iServerSession = SessionManager.getSessionWithDetails(DEFAULT_SERVER, DEFAULT_PROJECT,
		    DEFAULT_USERNAME, DEFAULT_PASSWORD);

		// Create identity token
		System.out.println("\nCreating identity token.\n");
		String identityToken = null;
		try {
			identityToken = createIdentityTokenFromSession(iServerSession);
			System.out.println(identityToken + "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Creating new session.
		System.out.println("Restoring session from identity token: \n");
		WebIServerSession restoredSession = restoreWebSessionFromIdentityToken(identityToken);
		try {
			System.out.println("Session created: \n" + restoredSession.getSessionID());
		} catch (WebObjectsException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create identityToken from an existing session.
	 * 
	 * @param iServerSession
	 * @return idToken as String
	 */
	public static String createIdentityTokenFromSession(WebIServerSession iServerSession) {
		String idToken = null;
		try {
			idToken = iServerSession.createIdentityToken(EnumWebPersistableState.MINIMAL_STATE_INFO);
		} catch (WebObjectsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idToken;
	}

	/**
	 * Restore session from existing identity token
	 * 
	 * @param identityToken
	 * @return WebIServerSession prepared using identityToken
	 */
	public static WebIServerSession restoreWebSessionFromIdentityToken(String identityToken) {
		WebObjectsFactory factory = null;
		factory = WebObjectsFactory.getInstance();
		WebIServerSession webIServerSession = factory.getIServerSession();
		try {
			webIServerSession.restoreStateFromIdentityToken(identityToken);
		} catch (WebObjectsException e) {
			e.printStackTrace();
		}

		return webIServerSession;
	}

	/**
	 * MicroStrategy Web reads a Secret Key from ConfigOverrides.properties file
	 * located under 'WEB-INF/classes/config' directory and Stores it in
	 * PreferencesMgr System Default Preferences store.
	 * 
	 * Since this is a Standalone class the preferences need to be set up
	 * programatically. This method sets up the secret key in preferences. In Web
	 * user interface this is done following steps in link below.
	 * 
	 * How to Enable Seamless Login Between Web, Library, and Workstation
	 * https://www2.microstrategy.com/producthelp/current/Library/en-us/Content/
	 * enable_seamless_login_web_library.htm
	 * 
	 * 
	 */
	public static void setUpSecretKeyPreferences() {
		String secretKey = "thisisthesecretkey";
		String PROPERTY_SECRET_KEY = "identityTokenSecretKey";

		PreferencesMgr prefMgr = PreferencesMgr.getInstance();
		Preferences prefs;
		try {
			prefs = prefMgr.getSysDefaultPreferences();
			prefs.setValue(PROPERTY_SECRET_KEY, secretKey);

		} catch (PreferencesException e1) {
			e1.printStackTrace();
		}
	}

}
