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
  
  // Simple sample to read report properties, change a value, then read properties again to confirm change was made
  public static void readThenSetProperties(WebIServerSession session) {
      String reportID = "F76698D44CC957971487B5A2E82956DF";
      String defaultBrowsingFolderID = "B2EA2A4C43C4F105BE483EA3DB3A1D66";

      listProperties(reportID, session);
      
      setPropertyWithKey(reportID, session, "GeneralReportProperties", "StartBrowsingFolderID", defaultBrowsingFolderID);
      
      listProperties(reportID, session);
  }
  
  // Loop through all properties stored within the report definition
  public static void listProperties(String reportID, WebIServerSession session) {    
    System.out.println("Loading report");
    
    try {
      
      WebObjectSource wos = session.getFactory().getObjectSource();
      WebObjectInfo woi = wos.getObject(reportID, EnumDSSXMLObjectTypes.DssXmlTypeReportDefinition, true);
      
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
  
  // Attempt to replace the value of a specific property within a set on the Report level. Use listProperties to find the propertySetName and propertyName.
  public static void setPropertyWithKey(String reportID, WebIServerSession session, String propertySetName, String propertyName, String newValue) {
      WebObjectSource wos = session.getFactory().getObjectSource();
      try {
        WebObjectInfo woi = wos.getObject(reportID, EnumDSSXMLObjectTypes.DssXmlTypeReportDefinition, true);
        
        WebPropertyGroup propertyGroup = woi.getPropertySets();
        WebPropertySet propertySet = propertyGroup.findItemByName(propertySetName);
        if (propertySet == null) {
            System.err.println("Property set " + propertySetName + " could not be found for report with ID " + reportID);
            return;
        }
        
        WebProperty property = propertySet.findItemByName(propertyName);
        if (property == null) {
            System.err.println("Property " + propertyName + " could not be found in " + propertySetName + " propertySet for report with ID " + reportID);
            return;
        }
        
        System.out.println("Property " + propertySetName + "." + propertyName + " has value: " + property.getValue());

        property.setValue(newValue);
        System.out.println("Property " + propertySetName + "." + propertyName + " now has changed value: " + property.getValue());

        property.save();
        
        System.out.println("Property saved to set.");
    } catch (WebObjectsException | IllegalArgumentException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
  }
}
