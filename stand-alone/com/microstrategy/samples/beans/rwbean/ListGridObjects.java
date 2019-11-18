package com.microstrategy.samples.beans.rwbean;

import java.util.Enumeration;
import java.util.Iterator;

import com.microstrategy.samples.sessions.SessionManager;
import com.microstrategy.web.beans.BeanFactory;
import com.microstrategy.web.beans.RWBean;
import com.microstrategy.web.beans.WebBeanException;
import com.microstrategy.web.objects.WebHyperLink;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebTemplateAttribute;
import com.microstrategy.web.objects.WebTemplateMetric;
import com.microstrategy.web.objects.rw.EnumRWExecutionModes;
import com.microstrategy.web.objects.rw.EnumRWUnitTypes;
import com.microstrategy.web.objects.rw.RWDefinition;
import com.microstrategy.web.objects.rw.RWGridGraphDef;
import com.microstrategy.web.objects.rw.RWInstance;
import com.microstrategy.web.objects.rw.RWUnitDef;

public class ListGridObjects {

    public static void main(String[] args) {

        // connectivity for the Intelligence Server
        final String INTELLIGENCE_SERVER_NAME = "10.23.3.184";
        final String PROJECT_NAME = "MicroStrategy Tutorial";
        final String MICROSTRATEGY_USERNAME = "Administrator";
        final String MICROSTRATEGY_PASSWORD = "";

        // DocumentID for which we will list out all grid objects
        final String DOCUMENT_ID = "4D2E479249F34B903BE958B0AFEB2C86";

        try {
            // Establishes user session on the Intelligence Server
            WebIServerSession session = SessionManager.getSessionWithDetails(INTELLIGENCE_SERVER_NAME, PROJECT_NAME,
                MICROSTRATEGY_USERNAME, MICROSTRATEGY_PASSWORD);

            // The below document instance is opened in design mode
            RWInstance rwi = getDesignDocumentInstance(DOCUMENT_ID, session);

            // Obtains an array of all grid graphs associated with the requested document using the RWDefinition
            RWDefinition rwd = rwi.getDefinition();
            RWUnitDef[] allGrids = findAllGrids(rwd);

            // Executes the various methods utilizing the array of grids
            printAllMetrics(allGrids);
            printAllAttributes(allGrids);
            printMetricHyperlinks(allGrids);
        } catch (WebBeanException | WebObjectsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Takes a documentID, RWBean, and a session and provides the instance of the specified document in DESIGN mode
     * 
     * @param objectID
     * @param session
     * @return
     * @throws WebBeanException
     */
    public static RWInstance getDesignDocumentInstance(String objectID, WebIServerSession session)
        throws WebBeanException {

        // Instantiates RWBean which is the first step in obtaining the RWInstance of this Document
        RWBean rwb = (RWBean) BeanFactory.getInstance().newBean("RWBean");

        // Sets the appropriate parameters including opening the document in design mode
        rwb.setObjectID(objectID);
        rwb.setSessionInfo(session);
        rwb.setExecutionMode(EnumRWExecutionModes.RW_MODE_DESIGN);

        return rwb.getRWInstance();
    }

    /**
     * Returns an array of RWUnitDef of type GridGraphs for a specific Document
     * 
     * @param rwd
     * @return
     */
    private static RWUnitDef[] findAllGrids(RWDefinition rwd) {
        RWUnitDef[] allGrids = rwd.findUnitsByType(EnumRWUnitTypes.RWUNIT_GRIDGRAPH);
        return allGrids;
    }

    /**
     * This takes an array of RWUnitDef with type GridGraph and prints out all metrics on the template
     * 
     * @param allGrids
     */
    public static void printAllMetrics(RWUnitDef[] allGrids) {
        System.out.println("Printing all metrics on all grids");
        for (int i = 0; i < allGrids.length; i++) {
            try {
                RWGridGraphDef grid = (RWGridGraphDef) allGrids[i];

                // For a specific grid, obtain the collection of metrics
                Enumeration<WebTemplateMetric> webTemplateMetrics = grid.getViewInstance().getTemplate().getTemplateMetrics().elements();

                // prints all metrics in the collection
                while (webTemplateMetrics.hasMoreElements()) {
                    System.out.println(webTemplateMetrics.nextElement().getName());
                }
                System.out.println("EOF");
            } catch (WebObjectsException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * This takes an array of RWUnitDef with type GridGraph and prints out all attribute names on the template
     * 
     * @param allGrids
     */
    public static void printAllAttributes(RWUnitDef[] allGrids) {
        System.out.println("Printing all attributes on all grids");
        for (int i = 0; i < allGrids.length; i++) {
            try {
                RWGridGraphDef grid = (RWGridGraphDef) allGrids[i];

                // For a specific grid, obtain the collection of attributes
                Enumeration<WebTemplateAttribute> webTemplateAttributes = (Enumeration<WebTemplateAttribute>) grid.getViewInstance().getTemplate()
                    .getTemplateAttributesCollection().elements();

                // Prints every attribute in the collection
                while (webTemplateAttributes.hasMoreElements()) {
                    System.out.println(webTemplateAttributes.nextElement().getName());
                }
                System.out.println("EOF");
            } catch (WebObjectsException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * This takes an array of RWUnitDef with type GridGraph and prints out all HyperLinks associated for each metric on the template
     * 
     * @param allGrids
     */
    public static void printMetricHyperlinks(RWUnitDef[] allGrids) {
        System.out.println("Printing all hyperlinks associated with metrics on all grids");
        try {
            for (int i = 0; i < allGrids.length; i++) {
                // For this specific grid
                RWGridGraphDef grid = (RWGridGraphDef) allGrids[i];

                // For a specific grid, obtain the collection of metrics
                Enumeration<WebTemplateMetric> webTemplateMetrics = grid.getViewInstance().getTemplate().getTemplateMetrics().elements();

                // Iterates through the collection of metrics
                while (webTemplateMetrics.hasMoreElements()) {
                    WebTemplateMetric metric = webTemplateMetrics.nextElement();

                    // Obtains a collection of HyperLinks associated with a specific metric
                    Iterator<WebHyperLink> HyperLinksOnMetric = metric.getHyperLinks().elements();

                    // for the current metric in the collection, prints all HyperLinks associated with it
                    while (HyperLinksOnMetric.hasNext()) {
                        System.out.println(HyperLinksOnMetric.next().getDisplayText());
                    }
                }
                System.out.println("EOF");
            }
        } catch (WebObjectsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}