package com.microstrategy.samples.serveradmin;

import java.util.Enumeration;


import com.microstrategy.samples.sessions.SessionManager;
import com.microstrategy.web.objects.WebClusterNode;
import com.microstrategy.web.objects.WebClusters;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsException;

public class ListNodesInCluster {

    // Connectivity for the intelligence server
    final static String intelligenceServerName = "SUP-W-002036";
    final static String projectName = "MicroStrategy Tutorial";
    final static String microstrategyUsername = "Administrator";
    final static String microstrategyPassword = "";


    public static void main(String[] args) {
        // Create our I-Server Session
        WebIServerSession session = SessionManager.getSessionWithDetails(intelligenceServerName, projectName, microstrategyUsername,
            microstrategyPassword);
        listNodes(session);
    }


    /**
     * This will attempt to access the first Node in the cluster and print all Intelligence Server names them using printNodes method
     * 
     * @param session
     */
    private static void accessNodes(WebIServerSession session) {
        try {
            WebClusters webClusters = session.getFactory().getInstance().getClusterAdmin().getClusters();
            if (webClusters.size() > 0) {
                Enumeration<WebClusterNode> webClusterNodes = webClusters.get(0).elements();
                printNodes(webClusterNodes);
            } else {
                System.out.println("No running nodes found in the cluster.");
                System.exit(0);
            }
        } catch (WebObjectsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * This takes a WebCluster object and prints all of the Intelligence Server node names
     * 
     * @param webClusters
     */
    private static void printNodes(Enumeration<WebClusterNode> webClusters) {
        while (webClusters.hasMoreElements()) {
            System.out.println(webClusters.nextElement().getNodeName());

        }
    }

}
