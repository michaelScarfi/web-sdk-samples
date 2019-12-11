package com.microstrategy.samples.serveradmin;

import com.microstrategy.samples.sessions.SessionManager;
import com.microstrategy.web.objects.*;
import com.microstrategy.web.objects.admin.WebObjectsAdminException;
import com.microstrategy.web.objects.admin.monitors.*;
import com.microstrategy.webapi.*;

public class ListCaches {

    // Connectivity for the intelligence server
    final static String intelligenceServerName = "APS-TSIEBLER-VM";
    final static String projectName = "";
    final static String microstrategyUsername = "Administrator";
    final static String microstrategyPassword = "";


    public static void main(String[] args) {
        // Create our I-Server Session
        WebIServerSession session = SessionManager.getSessionWithDetails(intelligenceServerName, projectName, microstrategyUsername,
            microstrategyPassword);
        
        listCaches(session);
    }

    /**
     * List all caches, broken down per project. Based on KB44043
     * Note: caches are separated by object type into separate monitor types.
     * @param session
     */
    private static void listCaches(WebIServerSession session) {
        WebObjectsFactory objectFactory = session.getFactory();
            
        CacheSource reportCacheSource = (CacheSource) objectFactory.getMonitorSource(EnumWebMonitorType.WebMonitorTypeCache);
        reportCacheSource.setLevel(EnumDSSXMLLevelFlags.DssXmlDetailLevel);        
        
        System.out.println("Looping report caches: ");
        loopCachesForSource(reportCacheSource);
        System.out.println("End of report caches");
        
        CacheSource rwCacheSource = (CacheSource) objectFactory.getMonitorSource(EnumWebMonitorType.WebMonitorTypeRWDCache);
        rwCacheSource.setLevel(EnumDSSXMLLevelFlags.DssXmlDetailLevel);   
        
        System.out.println("Looping document caches: ");
        loopCachesForSource(rwCacheSource);
        System.out.println("End of document caches");

    }
    
    private static void loopCachesForSource(CacheSource cacheSource) {
        try {
            CacheResults cachesPerProject = cacheSource.getCaches();
            
            int cacheCount = cachesPerProject.getCount();
            System.out.println("Total number of caches: " + cacheCount);
    
            CacheManipulator cm = cacheSource.getManipulator();
    
            // Caches are group on project level, so get the cache collection for each project
            for (int j = 0; j < cachesPerProject.size(); j++) {
                Caches projectCache = cachesPerProject.get(j);
                System.out.println(projectCache.getCount() + " caches for project: " + projectCache.getProjectName());
                
                // Then we can loop through each cache on the project level
                for (int i = 0; i < projectCache.getCount(); i++) {
                    Cache cache = projectCache.get(i);
                    String cacheName = cache.getCacheSourceName();
                    
                    // Example for deleting a report cache using ID
                    if (cacheName.contains("simple report")) {
                        System.out.println("---> DELETING " + (i + 1) + ") name: " + cacheName);
                        queueDeleteCacheWithId(cm, projectCache.getProjectDSSID(), cache.getID());
                    } else {
                        System.out.println("---> " + (i + 1) + ") name: " + cacheName);
                    }
                }
            }
            
            cm.submit(); 
        } catch (WebObjectsAdminException | MonitorManipulationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }
    
    public static void queueDeleteCacheWithId(CacheManipulator cacheManiplator, String projectId, String cacheId) {
        MonitorFilter filter = cacheManiplator.newMonitorFilter();
        filter.add(EnumDSSXMLCacheInfo.DssXmlCacheInfoProjectGuid, EnumDSSXMLMonitorFilterOperator.DssXmlEqual, projectId);
        filter.add(EnumDSSXMLCacheInfo.DssXmlCacheInfoId, EnumDSSXMLMonitorFilterOperator.DssXmlEqual, cacheId);
        
        cacheManiplator.addManipualtionTask(projectId, EnumDSSXMLCacheAdminAction.DssXmlDeleteCache, filter);
    }

}
