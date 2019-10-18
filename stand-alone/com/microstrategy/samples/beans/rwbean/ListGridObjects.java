package com.microstrategy.samples.beans.rwbean;

import java.util.Enumeration;
import java.util.Iterator;

import com.microstrategy.samples.sessions.SessionManager;
import com.microstrategy.web.beans.BeanFactory;
import com.microstrategy.web.beans.RWBean;
import com.microstrategy.web.beans.WebBeanException;
import com.microstrategy.web.objects.WebHyperLink;
import com.microstrategy.web.objects.WebHyperLinks;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebTemplate;
import com.microstrategy.web.objects.WebTemplateAttribute;
import com.microstrategy.web.objects.WebTemplateAttributes;
import com.microstrategy.web.objects.WebTemplateMetric;
import com.microstrategy.web.objects.WebTemplateMetrics;
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
            RWDefinition rwd = rwi.getDefinition();
            // Obtains an array of all grid graphs associated with the requested document using the RWDefinition
            RWUnitDef[] allGrids = FindAllGrids(rwd);
            PrintAllMetrics(allGrids);
            PrintAllAttributes(allGrids);
            PrintMetricHyperlinks(allGrids);
        } catch (WebBeanException | WebObjectsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Takes a documentID, RWBean, and a session and provides the instance of the specified document in DESIGN mode
    public static RWInstance getDesignDocumentInstance(String objectID, WebIServerSession session)
        throws WebBeanException {
        // Instantiates RWBean which is the first step in obtaining the RWInstance of this Document
        RWBean rwb = (RWBean) BeanFactory.getInstance().newBean("RWBean");
        rwb.setObjectID(objectID);
        rwb.setSessionInfo(session);
        rwb.setExecutionMode(EnumRWExecutionModes.RW_MODE_DESIGN);

        return rwb.getRWInstance();
    }

    // Returns an array of RWUnitDef of type GridGraphs for a specific Document
    public static RWUnitDef[] FindAllGrids(RWDefinition rwd) {
        RWUnitDef[] AllGrids = rwd.findUnitsByType(EnumRWUnitTypes.RWUNIT_GRIDGRAPH);
        return AllGrids;
    }

    // This takes an array of RWUnitDef with type GridGraph and prints out all metrics on the template
    public static void PrintAllMetrics(RWUnitDef[] allGrids) {
        System.out.println("Printing all metrics on all grids");
        for (int i = 0; i < allGrids.length; i++) {
            try {
                RWGridGraphDef grid = (RWGridGraphDef) allGrids[0];
                WebTemplate gridTemplate = grid.getViewInstance().getTemplate();
                WebTemplateMetrics metrics = gridTemplate.getTemplateMetrics();
                Enumeration<WebTemplateMetric> metricsIterator = metrics.elements();
                while (metricsIterator.hasMoreElements()) {
                    System.out.println(metricsIterator.nextElement().getName());
                }
                System.out.println("EOF");
            } catch (WebObjectsException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    // This takes an array of RWUnitDef with type GridGraph and prints out all attribute names on the template
    public static void PrintAllAttributes(RWUnitDef[] allGrids) {
        System.out.println("Printing all attributes on all grids");
        for (int i = 0; i < allGrids.length; i++) {
            try {
                RWGridGraphDef grid = (RWGridGraphDef) allGrids[0];
                WebTemplate gridTemplate = grid.getViewInstance().getTemplate();
                WebTemplateAttributes attributes = gridTemplate.getTemplateAttributesCollection();
                Enumeration<WebTemplateAttribute> allAttributes = attributes.elements();
                while (allAttributes.hasMoreElements()) {
                    System.out.println(allAttributes.nextElement().getName());
                }
                System.out.println("EOF");
            } catch (WebObjectsException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    // This takes an array of RWUnitDef with type GridGraph and prints out all HyperLinks associated for each metric on the template
    public static void PrintMetricHyperlinks(RWUnitDef[] allGrids) {
        System.out.println("Printing all hyperlinks associated with metrics on all grids");
        for (int i = 0; i < allGrids.length; i++) {
            try {
                RWGridGraphDef grid = (RWGridGraphDef) allGrids[0];
                WebTemplate gridTemplate = grid.getViewInstance().getTemplate();
                WebTemplateMetrics metrics = gridTemplate.getTemplateMetrics();
                // The below workflow goes iterates through every HyperLink associated to every metric on every GridGraph in the entire document
                Enumeration<WebTemplateMetric> metricsIterator = metrics.elements();
                while (metricsIterator.hasMoreElements()) {
                    WebTemplateMetric metric = metricsIterator.nextElement();
                    // Each metric may have more than one hyperlink
                    WebHyperLinks links = metric.getHyperLinks();
                    // This iterates through every possible hyperlink on a specific metric
                    Iterator<WebHyperLink> HyperLinksOnMetric = links.elements();
                    while (HyperLinksOnMetric.hasNext()) {
                        System.out.println(HyperLinksOnMetric.next().getDisplayText());
                    }
                }
                System.out.println("EOF");
            } catch (WebObjectsException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}