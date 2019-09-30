package com.microstrategy.samples.objects.cubes;


import com.microstrategy.samples.sessions.SessionManager;
import java.util.Enumeration;
import com.microstrategy.webapi.EnumDSSCubeStates;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.web.objects.admin.WebObjectsAdminException;
import com.microstrategy.web.objects.admin.monitors.CacheResults;
import com.microstrategy.web.objects.admin.monitors.CacheSource;
import com.microstrategy.web.objects.admin.monitors.Caches;
import com.microstrategy.web.objects.admin.monitors.CubeCache;
import com.microstrategy.web.objects.admin.monitors.EnumWebMonitorType;
import com.microstrategy.web.objects.admin.monitors.MonitorSource;
import com.microstrategy.webapi.EnumDSSXMLLevelFlags;

public class listCubes {

	public static void main(String[] args) throws WebObjectsAdminException {

		// Set server connection information
		String intelligenceServerName = "sup-w-001603.labs.microstrategy.com"; 
		String projectName = "MicroStrategy Tutorial"; 
		String microstrategyUsername =  "administrator";
		String microstrategyPassword = "";

		// Create an IServer session
		WebIServerSession iServerSession = SessionManager.getSessionWithDetails(intelligenceServerName, projectName, microstrategyUsername, microstrategyPassword);

		checkCubesCacheDetails(iServerSession);
	}

	private static void checkCubesCacheDetails(WebIServerSession session) throws WebObjectsAdminException {
		WebObjectsFactory factory = session.getFactory();
		MonitorSource monitor = factory.getMonitorSource(EnumWebMonitorType.WebMonitorTypeCubeCache);	           
		monitor.setLevel(EnumDSSXMLLevelFlags.DssXmlDetailLevel);
		CacheSource cubesMonitor = (CacheSource)monitor;
		CacheResults allCubes = cubesMonitor.getCaches();
		Enumeration<Caches> cubesEnum = allCubes.elements();

		while(cubesEnum.hasMoreElements())
		{

			//Get the intelligent cubes for one project
			Caches cubesPerProject = cubesEnum.nextElement();
			System.out.println("For project: " + cubesPerProject.getProjectName() + ", number of cubes:"+cubesPerProject.getCount());

			//iterate through the list of intelligent cubes of one project
			for (int j = 0; j < cubesPerProject.getCount(); j++) {

				CubeCache cube = (CubeCache) cubesPerProject.get(j);

				//Get the name of the cube
				System.out.println("Info for cache: " + cube.getCacheSourceName());

				//Get the last time the cube was updated
				System.out.println("Last update date: " + cube.getLastUpdateTime());

				//Get the cubes current status
				//Output related to an enumeration value of EnumDSSCubeStates
				System.out.println("Cube Status: " + cube.getStatus() + '\n');

			}
			System.out.println("----------------------------------------------------" + '\n');
		}
	}

}
