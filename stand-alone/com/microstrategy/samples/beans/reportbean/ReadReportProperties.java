package com.microstrategy.samples.beans.reportbean;

import java.util.Enumeration;

import com.microstrategy.samples.sessions.SessionManager;
import com.microstrategy.web.beans.*;
import com.microstrategy.web.objects.*;
import com.microstrategy.webapi.*;

public class ReadReportProperties {

  public static void main(String[] args) {
    // Connectivity for the intelligence server
    String intelligenceServerName = "APS-TSIEBLER-VM";
    String projectName = "MicroStrategy Tutorial";
    String microstrategyUsername = "Administrator";
    String microstrategyPassword = "";
    
    // Create our I-Server Session
    WebIServerSession session = SessionManager.getSessionWithDetails(intelligenceServerName, projectName, microstrategyUsername, microstrategyPassword);
    
    // The GUID of the data mart report to use
    String reportID = "F76698D44CC957971487B5A2E82956DF";
    
    listProperties(reportID, session);
  }
  
  public static void listProperties(String reportID, WebIServerSession session) {    
    System.out.println("Loading report");
    
    try {
      
      WebObjectSource wos = session.getFactory().getObjectSource();
      WebObjectInfo woi = wos.getObject(reportID,EnumDSSXMLObjectTypes.DssXmlTypeReportDefinition, true);
      
      WebPropertyGroup wpg = woi.getPropertySets();
      
      Enumeration<WebPropertySet> e = wpg.elements();
      while (e.hasMoreElements()) {
        WebPropertySet props = e.nextElement();
        
        System.out.println("+ Found '" + props.size() + "' elements in the property set with name: '" + props.getName() + "'");
        
        for (int i=0;i<props.size();i++){
          System.out.println("---> '" + props.get(i).getName() + "' hasValue: '" + props.get(i).getValue() + "'");
        }
      }
      
    } catch (WebObjectsException | IllegalArgumentException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
