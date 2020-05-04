package com.microstrategy.samples.objectmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.microstrategy.samples.sessions.SessionManager;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.web.objects.WebSourceManipulator;

public class ImportPackage {


    public static void main(String[] args) {

        // Required information to generate a connection to the Intelligence Server
        final String SERVER = "10.23.3.184";
        final String PROJECT = "MicroStrategy Tutorial";
        final String USERNAME = "Administrator";
        final String PASSWORD = "";

        WebIServerSession session = SessionManager.getSessionWithDetails(SERVER, PROJECT, USERNAME, PASSWORD);
        WebObjectsFactory wof = session.getFactory();

        // Please update this to the location of the package you would like to load into MicroStrategy
        String myPackagePath = "C:\\Users\\ddechent\\Documents\\MMPTest\\RESTAPIPackage.mmp";

        WebSourceManipulator wsm = wof.getObjectSource().getSourceManipulator();
        importPackage(wsm, myPackagePath);

    }

    /**
     * This method loads a MicroStrategy package (.mmp) to the Intelligence Server using a FileInputStream
     * 
     * @param wsm
     * @param myPackagePath
     */
    public static void importPackage(WebSourceManipulator wsm, String absolutePathToFile) {
        try {
            System.out.println("Opening File and importing the package");
            File myPackagePath = new File(absolutePathToFile);
            FileInputStream fis = new FileInputStream(myPackagePath);
            wsm.applyDeltaPackage(fis, myPackagePath.length());
            wsm.invoke();
            fis.close();
            System.out.println("Package imported.");
        } catch (FileNotFoundException | WebObjectsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
