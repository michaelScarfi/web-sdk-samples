package com.microstrategy.samples.cachemanager;

import com.microstrategy.samples.sessions.SessionManager;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.web.objects.admin.WebObjectsAdminException;
import com.microstrategy.web.objects.admin.monitors.CacheManipulator;
import com.microstrategy.web.objects.admin.monitors.CacheResults;
import com.microstrategy.web.objects.admin.monitors.CacheSource;
import com.microstrategy.web.objects.admin.monitors.Caches;
import com.microstrategy.web.objects.admin.monitors.EnumWebMonitorType;
import com.microstrategy.web.objects.admin.monitors.MonitorFilter;
import com.microstrategy.web.objects.admin.monitors.MonitorManipulationException;
import com.microstrategy.webapi.EnumDSSXMLCacheAdminAction;
import com.microstrategy.webapi.EnumDSSXMLCacheInfo;
import com.microstrategy.webapi.EnumDSSXMLLevelFlags;
import com.microstrategy.webapi.EnumDSSXMLMonitorFilterOperator;

public class RemoveReportCaches {
	private static String DEFAULT_SERVER = "10.23.1.124";
	private static String DEFAULT_PROJECT = "MicroStrategy Tutorial";
	private static String DEFAULT_USERNAME = "Administrator";
	private static String DEFAULT_PASSWORD = "";

	/*
	 * Demonstration on how to delete a report's cache given its ID.
	 * This example could be adapted to work with caches enumerated in EnumWebMonitorType.
	 * EnumWebMonitorType
	 * https://lw.microstrategy.com/msdz/MSDL/GARelease_Current/docs/ReferenceFiles/reference/com/microstrategy/web/objects/admin/monitors/EnumWebMonitorType.html
	 */
	public static void main(String[] args) {
		// Report ID
		String reportID = "2BB4B32446D5BB674705B6AAE457E92E";

		// Create session.
		WebIServerSession iServerSession = SessionManager.getSessionWithDetails(DEFAULT_SERVER, DEFAULT_PROJECT, DEFAULT_USERNAME, DEFAULT_PASSWORD);

		// Retrieve reports caches for project.
		Caches reportCaches = getReportCaches(iServerSession);
		System.out.println("Retrieving report caches for project: " + reportCaches.getProjectName());
		System.out.println("Caches found: " + reportCaches.getCount());

		// Deleting caches for specific report.
		System.out.println("Deleting cache for report: " + reportID);
		deleteReportCaches(iServerSession, reportID);
		System.out.println("Cache deleted.");
	}

	// Delete caches for report identified by reportID
	public static void deleteReportCaches(WebIServerSession iServerSession, String reportID) {
		int monitorType = EnumWebMonitorType.WebMonitorTypeCache;
		int cacheInfo = EnumDSSXMLCacheInfo.DssXmlCacheInfoReportId;
		removeCachesFromObject(iServerSession, reportID, monitorType, cacheInfo);
	}

	// Retrieve report caches
	public static Caches getReportCaches(WebIServerSession iServerSession) {
		int monitorType = EnumWebMonitorType.WebMonitorTypeCache;
		Caches reportCaches = getCaches(iServerSession, monitorType);
		return reportCaches;
	}

	// Generic method to retrieve any object type's caches
	public static Caches getCaches(WebIServerSession iServerSession, int monitorType) {
		Caches projectCaches = null;

		WebObjectsFactory factory = iServerSession.getFactory();
		CacheSource cacheSource = (CacheSource) factory.getMonitorSource(monitorType);
		cacheSource.setLevel(EnumDSSXMLLevelFlags.DssXmlDetailLevel);

		try {
			CacheResults cacheResults = cacheSource.getCaches();
			projectCaches = cacheResults.item(iServerSession.getProjectID());
		} catch (WebObjectsAdminException | WebObjectsException | IllegalArgumentException e) {
			e.printStackTrace();
		}
		return projectCaches;
	}

	// Generic method to remove caches for any object determined by objectID.
	public static void removeCachesFromObject(WebIServerSession iServerSession, String objectID, int monitorType, int cacheInfo) {

		WebObjectsFactory factory = iServerSession.getFactory();
		CacheSource cacheSource = (CacheSource) factory.getMonitorSource(monitorType);
		cacheSource.setLevel(EnumDSSXMLLevelFlags.DssXmlDetailLevel);

		// Obtain cache manipulator object for caches.
		CacheManipulator cacheManipulator = cacheSource.getManipulator();

		try {
			MonitorFilter monitorFilter = cacheManipulator.newMonitorFilter();
			monitorFilter.add(cacheInfo, EnumDSSXMLMonitorFilterOperator.DssXmlEqual, objectID);
			cacheManipulator.alter(iServerSession.getProjectID(), EnumDSSXMLCacheAdminAction.DssXmlDeleteCache, monitorFilter);
		} catch (WebObjectsAdminException | MonitorManipulationException | WebObjectsException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
}
