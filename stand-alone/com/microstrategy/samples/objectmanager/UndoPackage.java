package com.microstrategy.samples.objectmanager;

import com.microstrategy.samples.sessions.SessionManager;
import com.microstrategy.samples.util.FileHelper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebSourceManipulator;
import com.microstrategy.webapi.EnumDSSXMLConflictResolution;

public class UndoPackage {
	private static String localFolderPath = "/Users/dzumbaugh/Documents/packages/";
	private static String packageFilePath = "/Users/dzumbaugh/Documents/packages/ProjectPackage.mmp";

	public static void main(String[] args) throws IOException {
		System.out.println("Preparing session");

		WebIServerSession session = SessionManager.getSessionWithDetails("was-psalazar6.corp.microstrategy.com", "MicroStrategy Tutorial", "Administrator", "");
		PackageCreation.setupWithSession(session);

		createSimpleUndoPackageFromExistingPackage(packageFilePath);
	}

	public static void createSimpleUndoPackageFromExistingPackage(String filePath) throws IOException {
		// Read an existing package file into a byte array
		byte[] packageWithReport = FileHelper.readFiletoByteArray(filePath);
		if (packageWithReport == null) {
			System.out.println("Cannot create undo package if package not provided");
			return;
		}

		// now that we have a package, generate an undo package
		String fileName = "undoExistingReportPackage";
		createUndoPackageForPackage(packageWithReport, fileName);
	}

	public static void createUndoPackageForPackage(byte[] inputPackage, String fileName) {
		WebSourceManipulator manipulator = PackageCreation.source.getSourceManipulator();
		manipulator.setACLConflictRule(EnumDSSXMLConflictResolution.DssXmlConflictReplace);

		InputStream inputPackageStream = new ByteArrayInputStream(inputPackage);

		byte[] undoPackage = null;
		try {
			undoPackage = manipulator.createUndoPackageClientToClient(inputPackageStream, inputPackage.length);
		} catch (WebObjectsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		// save the undo package to file
		String pathToFile = localFolderPath + fileName + ".mmp";
		System.out.println("Saved undo package to: " + pathToFile);
		FileHelper.saveByteArrayToFile(undoPackage, pathToFile);
	}

	// This function creates a package and an undo package from it
	// This is not a complete scenario for when you would want to create an undo package. It can be useful to understand the undo package creation
	// An undo package should be created off of an existing package prior to importing it into the project
	// Utilize the function createSimpleUndoPackageFromExistingPackage for these scenarios 
	public static void createSimpleUndoPackage() {
		// wrapped logic to create a simple package containing a report
		byte[] packageWithReport = PackageCreation.createSingleObjectPackageWithReport();
		if (packageWithReport == null) {
			System.out.println("Cannot create undo package if main package creation failed");
			return;
		}

		// now that we have a package, generate an undo package
		String fileName = "undoReportPackage";
		createUndoPackageForPackage(packageWithReport, fileName);
	}

}
