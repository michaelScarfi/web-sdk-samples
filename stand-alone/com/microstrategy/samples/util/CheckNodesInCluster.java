package com.microstrategy.samples.util;

import java.util.Enumeration;

import com.microstrategy.samples.sessions.AuthenticateBypassClustering;
import com.microstrategy.samples.sessions.SessionManager;
import com.microstrategy.web.objects.WebCluster;
import com.microstrategy.web.objects.WebIServerSession;

public class CheckNodesInCluster {

    // Connectivity for the intelligence server
    final static String intelligenceServerName = "10.23.1.51";
    final static String projectName = "MicroStrategy Tutorial";
    final static String microstrategyUsername = "Administrator";
    final static String microstrategyPassword = "";


    public static void main(String[] args) {
        // Create our I-Server Session
        WebIServerSession session = SessionManager.getSessionWithDetails(intelligenceServerName, projectName, microstrategyUsername,
            microstrategyPassword);
        accessAndPrintAllNodesInClusters();
    }


    /**
     * This method will access the ClusterAdmin object and print out the names of all Intelligence Server nodes attached to the designated Cluster
     */
    public static void accessAndPrintAllNodesInClusters() {
        WebClusterAdmin admin = WebObjectEnumeration<WebCluster>Instance().getClusterAdmin();
        Enumeration<WebCluster> clusters = admin.getClusters().elements();
        while (clusters.hasMoreElements())
        {
            WebCluster currentCluster = clusters.nextElement();
            System.out.println(currentCluster.toString());
        }
        
    }
    
    

}
