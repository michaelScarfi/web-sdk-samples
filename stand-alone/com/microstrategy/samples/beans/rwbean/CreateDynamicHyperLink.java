package com.microstrategy.samples.beans.rwbean;

import java.util.Enumeration;
import java.util.Iterator;

import com.microstrategy.samples.sessions.SessionManager;
import com.microstrategy.web.beans.BeanFactory;
import com.microstrategy.web.beans.FolderBean;
import com.microstrategy.web.beans.RWBean;
import com.microstrategy.web.beans.WebBeanException;
import com.microstrategy.web.objects.EnumLinkAnswerMode;
import com.microstrategy.web.objects.WebElementsPrompt;
import com.microstrategy.web.objects.WebFolder;
import com.microstrategy.web.objects.WebHyperLink;
import com.microstrategy.web.objects.WebHyperLinkAnswer;
import com.microstrategy.web.objects.WebHyperLinkAnswers;
import com.microstrategy.web.objects.WebHyperLinks;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectInfo;
import com.microstrategy.web.objects.WebObjectSource;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebPrompt;
import com.microstrategy.web.objects.WebPrompts;
import com.microstrategy.web.objects.WebTemplateMetric;
import com.microstrategy.web.objects.rw.EnumRWUnitTypes;
import com.microstrategy.web.objects.rw.RWDefinition;
import com.microstrategy.web.objects.rw.RWGridGraphDef;
import com.microstrategy.web.objects.rw.RWInstance;
import com.microstrategy.web.objects.rw.RWUnitDef;
import com.microstrategy.webapi.EnumDSSXMLDocSaveAsFlags;
import com.microstrategy.webapi.EnumDSSXMLObjectFlags;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;

public class CreateDynamicHyperLink {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        // Connectivity for the intelligence server
        final String INTELLIGENCE_SERVER_NAME = "10.23.3.184";
        final String PROJECT_NAME = "MicroStrategy Tutorial";
        final String MICROSTRATEGY_USERNAME = "Administrator";
        final String MICROSTRATEGY_PASSWORD = "";

        // Defining which document and grid to create the hyperlink on, and the target document for that hyperlink
        final String SOURCE_DOCUMENT = "7AA28C4A4F3BAA7CF40C388AD5B2E556";
        final String TARGET_DOCUMENT = "BA1FDA7442C5BC52E0B17B82E28D3EB0";

        // Gridname containing the metrics with links
        final String GRID_NAME = "Category Sales Report";

        try {
            WebIServerSession session = SessionManager.getSessionWithDetails(INTELLIGENCE_SERVER_NAME, PROJECT_NAME,
                MICROSTRATEGY_USERNAME, MICROSTRATEGY_PASSWORD);

            // The below two lines are used to obtain an instance of a prompted document
            RWBean rwb = (RWBean) BeanFactory.getInstance().newBean("RWBean");
            RWInstance rwi = OpenPromptedDocumentDesignMode.openDocumentInstance(rwb, SOURCE_DOCUMENT, session);

            // Comment out whichever method you are not going to use.
            // Method One: This will answer the prompt answers dynamically from the source
            writeDynamicMetricLinksForPromptedDocument(session, rwi, rwb, SOURCE_DOCUMENT, TARGET_DOCUMENT, GRID_NAME);

            // Method Two: This will create a HyperLink on a single metric that will use the default promptanswers
            // createSimpleHyperLinkForSingleMetric(session, rwi, rwb, SOURCE_DOCUMENT, TARGET_DOCUMENT, GRID_NAME);

        } catch (WebBeanException | WebObjectsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Creates a Hyperlink which points to a prompted document for all metrics on a particular Grid. The prompt is assumed to be of type
     * WebElementPrompt. Please modify the element prompt type as required
     * 
     * @param session
     * @param rwi
     * @param rwb
     * @param documentID
     * @param documentToLinkTo
     * @param gridName
     * @throws WebBeanException
     * @throws WebObjectsException
     */
    public static void writeDynamicMetricLinksForPromptedDocument(WebIServerSession session, RWInstance rwi, RWBean rwb, String documentID,
        String documentToLinkTo, String gridName)
            throws WebBeanException, WebObjectsException {
        System.out.println("Document status: " + rwi.pollStatusOnly());

        Enumeration<WebTemplateMetric> webTemplateMetrics = obtainMetrics(rwi, gridName);

        // Iterates through all metrics on this grid, and attempts to access the existing HyperLinks or creates a new one
        while (webTemplateMetrics.hasMoreElements()) {
            WebTemplateMetric metric = webTemplateMetrics.nextElement();

            // one metric may have many links
            WebHyperLinks links = metric.getHyperLinks();

            // If there are no HyperLinks on this metric, create one
            if (links.size() <= 0) {
                links.add();
            }

            /*
             * This loops through all hyperlinks for the current metric, and then either modifies the first HyperLinkAnswer or creates a
             * HyperLinkAnswer
             */
            Iterator<WebHyperLink> linkIterator = links.elements();
            while (linkIterator.hasNext()) {
                WebHyperLink link = linkIterator.next();

                // Setting up all objects to set the Hyperlink to point to the correct object
                WebObjectSource wos = session.getFactory().getObjectSource();
                wos.setFlags(wos.getFlags() | EnumDSSXMLObjectFlags.DssXmlObjectDefn);
                WebObjectInfo targetObject = wos.getObject(documentToLinkTo, EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition, true);

                // Setting the HyperLink information
                setHyperLink(link, targetObject, EnumLinkAnswerMode.DYNAMIC);

                // For the first HyperLinkAnswer on this particular HyperLink, set the prompt information and answermode
                setHyperLinkAnswer(rwi, link);

                // Saves a copy of the document in the same folder as the original, which is why the RWBean rwb is passed
                saveDocument(rwb, rwi);
            }
        }
        System.out.println("EOF");
    }

    /**
     * 
     * @param session
     * @param rwi
     * @param rwb
     * @param documentID
     * @param documentToLinkTo
     * @param gridName
     * @throws WebObjectsException
     */
    public static void createSimpleHyperLinkForSingleMetric(WebIServerSession session, RWInstance rwi, RWBean rwb, String documentID,
        String documentToLinkTo, String gridName) throws WebObjectsException {
        System.out.println("Document status: " + rwi.pollStatusOnly());

        Enumeration<WebTemplateMetric> webTemplateMetrics = obtainMetrics(rwi, gridName);
        while (webTemplateMetrics.hasMoreElements()) {
            WebTemplateMetric metric = webTemplateMetrics.nextElement();
            WebHyperLinks links = metric.getHyperLinks();
            // If there are no HyperLinks on this metric, create one
            if (links.size() <= 0)
                links.add();

            // Modify only the first HyperLink for the current selected metric
            WebHyperLink link = links.get(0);
            WebObjectSource wos = session.getFactory().getObjectSource();
            WebObjectInfo targetObject = wos.getObject(documentToLinkTo, EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition, true);

            // Setting the HyperLink information using setLinkAnswer. If the AnswerMode should be to use a different method, change the value of
            // EnumLinkAnswerMode
            setHyperLink(link, targetObject, EnumLinkAnswerMode.USE_DEFAULT_ANSWER);

        }
        // Saves a copy of the document in the same folder as the original
        saveDocument(rwb, rwi);
    }

    /**
     * Takes a given WebHyperLink, target document, and answer mode and applies all required changes to the link
     * 
     * @param link
     * @param targetObject
     * @param answerMode
     */
    private static void setHyperLink(WebHyperLink link, WebObjectInfo targetObject, int answerMode) { // Setting up all objects to set the
        String targetName = targetObject.getName(); // Setting the HyperLink information
        link.setLinkType(WebHyperLink.LINK_TYPE_EXECUTE);
        link.setDefault(true);
        link.setDefaultAnswerMode(answerMode);
        link.setTargetObject(targetObject);
        link.setDisplayText(targetName);
        link.setSelectorOptions("1");
    }

    /**
     * Given WebHyperLinkAnswers for a specific WebHyperLink, answer the prompts if any exist, and set the first HyperLinkAnswers.
     * 
     * @param rwi
     * @param answers
     * @throws WebObjectsException
     */
    private static void setHyperLinkAnswer(RWInstance rwi, WebHyperLink link) throws WebObjectsException {
        // WebHyperLinkAnswers denotes the way that the prompt is answered. Example: Dynamically, answered from source, etc.
        WebHyperLinkAnswers answers = link.getAnswers();

        // If there are no HyperLinkAnswers, creates a HyperLinkAnswer
        if (answers.size() <= 0) {
            answers.add();
        }

        // Modifies only the first HyperlinkAnswer
        WebHyperLinkAnswer answer = answers.get(0);
        answer.setAnswerMode(EnumLinkAnswerMode.DYNAMIC);

        // Obtains the available prompts for this document
        WebPrompts getPrompts = rwi.getPrompts();

        /*
         * If there are prompts, apply the prompt information to the HyperLinkAnswer If there are no prompts, the HyperLink will still function but no
         * prompt answers will be applied
         */
        if (!getPrompts.isEmpty()) {

            // Gets the first prompt
            // In this sample, we assume that the prompt is the WebElementsPrompt (Element Prompt)
            WebPrompt getPrompt = getPrompts.get(0);
            WebElementsPrompt myWebElementPrompt = (WebElementsPrompt) getPrompt;

            // Obtains information from the prompt to answer the HyperLinkAnswer
            String promptID = myWebElementPrompt.getID();
            String originID = myWebElementPrompt.getOrigin().getID();
            int promptType = myWebElementPrompt.getType();
            int originType = myWebElementPrompt.getOrigin().getType();

            // Configures the HyperLinkAnswer to pass the prompts correctly
            answer.setPrompt(promptID, promptType);
            answer.setPromptOrigin(originID, originType);
        }
    }

    /**
     * Provides a collection of type WebTemplateMetric given a specific Instance and gridname
     * 
     * @param rwi
     * @param gridName
     * @return
     * @throws WebObjectsException
     */
    private static Enumeration<WebTemplateMetric> obtainMetrics(RWInstance rwi, String gridName) throws WebObjectsException {
        // RWDefinition is required to obtain the specific grid
        RWDefinition rwd = rwi.getDefinition();
        System.out.println("Document Name: " + rwd.getName());

        // Obtain all grids on the objects based on the RWDefinition
        RWUnitDef[] allGrids = rwd.findUnits(EnumRWUnitTypes.RWUNIT_GRIDGRAPH, gridName);

        if (allGrids[0] == null) {
            System.out.println("There is no grid found matching the given name: " + gridName
                + ".\nPlease check that the correct gridName was provided for the given source document.\nExiting program.");
            System.exit(0);
        }
        RWGridGraphDef grid = (RWGridGraphDef) allGrids[0];

        // For the specific grid, obtain the collection of metrics
        @SuppressWarnings("unchecked")
        Enumeration<WebTemplateMetric> webTemplateMetrics = grid.getViewInstance().getTemplate().getTemplateMetrics().elements();
        return webTemplateMetrics;
    }

    /**
     * Using the RWBean and RWI, saves a copy of the document in the same folder location as the original document
     * 
     * @param rwb
     * @param rwi
     * @throws WebObjectsException
     */
    public static void saveDocument(RWBean rwb, RWInstance rwi) throws WebObjectsException {
        try {

            FolderBean fb;
            fb = rwb.getParentFolderBean("fb");
            WebFolder parentFolder = fb.getFolderObject();
            RWInstance newInst = rwi.getRWManipulator().applyChanges();
            newInst.setSaveAsFlags(EnumDSSXMLDocSaveAsFlags.DssXmlDocSaveAsOverwrite);
            int status = newInst.getStatus();
            while (status != 1) {
                status = newInst.pollStatus();
            }
            System.out.println("Current Status= " + status);
            System.out.println("Parent Folder = " + parentFolder.getName());
            newInst.saveAs(parentFolder, "ModifiedDocument3");

        } catch (WebBeanException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



}
