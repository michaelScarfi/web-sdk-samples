package com.microstrategy.samples.users;

import com.microstrategy.samples.sessions.SessionManager;
import com.microstrategy.web.beans.BeanFactory;
import com.microstrategy.web.beans.UserBean;
import com.microstrategy.web.beans.WebBeanException;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.admin.users.WebNTLoginInfo;
import com.microstrategy.web.objects.admin.users.WebUser;

public class CreateUser {

	public static void main(String[] args) {
		// Connectivity for the intelligence server
		String intelligenceServerName = "10.23.5.242";
		String projectName = "MicroStrategy Tutorial";
		String microstrategyUsername = "Administrator";
		String microstrategyPassword = "";

		//Standard User info.
		String loginName= "testUser";
		String fullName = "Test User's fullName";

		//NT User info.
		String NTloginName= "NTtestUser";
		String NTfullName = "NT Test User's fullName";
		String NTaccountName = "NTaccountName";
		
		// Create our I-Server Session
		WebIServerSession session = SessionManager.getSessionWithDetails(intelligenceServerName, projectName, microstrategyUsername, microstrategyPassword);
		WebUser standardUser = createStandardUser(session, loginName, fullName);
		WebUser NTUser = createNTUser(session, NTloginName, NTfullName, NTaccountName);
		
	}//End main().
	
	//Create user Bean as a base to create Users.
	public static UserBean createUserBeanInstance(WebIServerSession session) {
		System.out.println("Starting user creation workflow.");
		UserBean userBean = null;
		userBean = (UserBean) BeanFactory.getInstance().newBean("UserBean");
		userBean.setSessionInfo(session);
		userBean.InitAsNew();
		return userBean;
	}
	
	
	//Creates a base user using SDK
	public static WebUser createStandardUser(WebIServerSession session, String loginName, String fullName) {
		UserBean userBean = createUserBeanInstance(session);
		WebUser standardUser = null;
		try {
			standardUser = (WebUser) userBean.getUserEntityObject();
			standardUser.setLoginName(loginName);
			standardUser.setFullName(fullName);
			userBean.getObjectInfo().setDescription("User created using MicroStrategy Web SDK.");
			userBean.save();
			System.out.println("Base user created successfully.");
		} catch (WebBeanException e) {
			e.printStackTrace();
		}
		return standardUser;
	}
	
	
	/*
	* Create user with NT authentication.
	* 
	* Pre-requisites:
	* - NTAccountName must be an existing NT account name to map user to.
	* To set user's NT-specific properties the dll "MBNTVSUP.dll" is invoked. This dll is located under "Common Files/MicroStrategy/" directory.
	* - This code must be executed in Windows environments or having the mentioned dll available under Java PATH variable.  
	* 
	*/
	public static WebUser createNTUser(WebIServerSession session, String loginName, String fullName, String NTaccountName) {
		UserBean userBean =createUserBeanInstance(session);
		WebUser NTUser = null;
		try {
			NTUser = (WebUser) userBean.getUserEntityObject();
			WebNTLoginInfo webNTLoginInfo = NTUser.getNTLoginInfo();
			
			//Mapping NT account name to user.
			webNTLoginInfo.setWindowsAccountName(NTaccountName);
			
			NTUser.setLoginName(loginName);
			NTUser.setFullName(fullName);
			userBean.getObjectInfo().setDescription("User created using MicroStrategy Web SDK - NT Authentication.");
			userBean.save();
			System.out.println("NT user created sucessfully.");
		} catch (WebBeanException | WebObjectsException e) {
			e.printStackTrace();
		}
		return NTUser;
	}
}
