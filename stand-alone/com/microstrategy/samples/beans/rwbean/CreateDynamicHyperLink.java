package com.microstrategy.samples.beans.rwbean;

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
import com.microstrategy.web.objects.WebTemplate;
import com.microstrategy.web.objects.WebTemplateMetric;
import com.microstrategy.web.objects.WebTemplateMetrics;
import com.microstrategy.web.objects.rw.EnumRWExecutionModes;
import com.microstrategy.web.objects.rw.EnumRWUnitTypes;
import com.microstrategy.web.objects.rw.RWDefinition;
import com.microstrategy.web.objects.rw.RWGridGraphDef;
import com.microstrategy.web.objects.rw.RWInstance;
import com.microstrategy.web.objects.rw.RWUnitDef;
import com.microstrategy.webapi.EnumDSSXMLDocSaveAsFlags;
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
            RWBean rwb = (RWBean) BeanFactory.getInstance().newBean("RWBean");
            RWInstance rwi = GetDesignDocumentInstancePromptedDocument(rwb, SOURCE_DOCUMENT, session);
            //Comment out whichever method you are not going to use.
            //This will answer the prompt answers dynamically from the source
            WriteDynamicMetricLinksForPromptedDocument(session, rwi, rwb, SOURCE_DOCUMENT, TARGET_DOCUMENT, GRID_NAME);
            //This will create a HyperLink on a single metric that will use the default promptanswers
            //CreateSimpleHyperLinkForSingleMetric(session, rwi, rwb, SOURCE_DOCUMENT, TARGET_DOCUMENT, GRID_NAME);
        
        } catch (WebBeanException | WebObjectsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Opens a prompted document in design mode, answers the prompts, and returns the instance for further use
    public static RWInstance GetDesignDocumentInstancePromptedDocument(RWBean rwb, String objectID, WebIServerSession session)
        throws WebBeanException {
        rwb.setObjectID(objectID);
        rwb.setSessionInfo(session);
        rwb.setExecutionMode(EnumRWExecutionModes.RW_MODE_DESIGN);
        RWInstance rwi = rwb.getRWInstance();

        // answer prompt answers
        WebPrompts prompts;
        try {
            prompts = rwi.getPrompts();
            System.out.println("Prompt size " + prompts.size());
            for (int i = 0; i < prompts.size(); i++) {
                WebPrompt prompt = prompts.get(i);
                if (!prompt.isClosed()) {
                    prompt.setClosed(false);
                }
            }
            prompts.answerPrompts();
        } catch (WebObjectsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rwi;
    }
    
    //Creates a Hyperlink which points to a prompted document for all metrics on a particular Grid
    public static void WriteDynamicMetricLinksForPromptedDocument(WebIServerSession session, RWInstance rwi, RWBean rwb, String documentID, String documentToLinkTo, String gridName)
        throws WebBeanException, WebObjectsException {
        System.out.println("Document status: " + rwi.pollStatusOnly());

        // loop grid metrics
        RWDefinition rwd = rwi.getDefinition();
        System.out.println("Document Name: " + rwd.getName());

        RWUnitDef[] units = rwd.findUnits(EnumRWUnitTypes.RWUNIT_GRIDGRAPH, gridName);
        RWGridGraphDef grid = (RWGridGraphDef) units[0];

        WebTemplate gridTemplate = grid.getViewInstance().getTemplate();
        WebTemplateMetrics metrics = gridTemplate.getTemplateMetrics();

        for (int i = 0; i < metrics.size(); i++) {
            WebTemplateMetric metric = metrics.get(i);
            System.out.println("Metric Name= " + metric.getName());

            // one metric may have many links
            WebHyperLinks links = metric.getHyperLinks();
            //If there are no HyperLinks on this metric, create one
            if (links.size() <= 0)
                links.add();
            Iterator<WebHyperLink> linkIterator = links.elements();
            
            //This loops through all hyperlinks for the current metric, and then either modifies the first HyperLinkAnswer or creates a HyperLinkAnswer
            while (linkIterator.hasNext()) {
                WebHyperLink link = linkIterator.next();            
                //Setting up all objects to set the Hyperlink to point to the correct object
                WebObjectSource wos = session.getFactory().getObjectSource();
                WebObjectInfo targetObject = wos.getObject(documentToLinkTo, EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition, true);
                String targetName = targetObject.getName();
                //Setting the HyperLink information
                link.setLinkType(WebHyperLink.LINK_TYPE_EXECUTE);
                link.setDefault(true);
                link.setDefaultAnswerMode(EnumLinkAnswerMode.DYNAMIC);
                link.setTargetObject(targetObject);
                link.setDisplayText(targetName);
                link.setSelectorOptions("1");
                

                /*
                * The below statements can be used for debugging the HyperLink
                */
                // System.out.println("-> Link getAnswerXML: " + link.getAnswerXML());
                // System.out.println("-> Link getDefaultAnswerMode: " + link.getDefaultAnswerMode());
                // System.out.println("-> Link getDisplayText: " + link.getDisplayText());
                // System.out.println("-> Link getLinkType: " + link.getLinkType());
                // System.out.println("-> Link getURL: " + link.getURL());
                // System.out.println("-> Link getSelectorOptions: " + link.getSelectorOptions());
                // System.out.println("-> Link toString: " + link.toString());
                // System.out.println("-> Link isDefault: " + link.isDefault());
                
                WebPrompts getPrompts = rwi.getPrompts();
                // Gets the first prompt
                // In this sample, we assume that the prompt is the WebElementsPrompt (Element Prompt)
                WebPrompt getPrompt = getPrompts.get(0);
                WebElementsPrompt myWebElementPrompt = (WebElementsPrompt) getPrompt;
                // WebHyperLinkAnswers denotes the way that the prompt is answered. Example: Dynamically, answered from source, etc.
         
                WebHyperLinkAnswers answers = link.getAnswers();
                // If there are no HyperLinkAnswers, creates a HyperLinkAnswer
                if (answers.size() <= 0) {
                    answers.add();
                }
                    //To the first HyperLinkAnswer on this particular HyperLink, set the prompt information and answermode
                    WebHyperLinkAnswer answer = answers.get(0);
                    String promptID = myWebElementPrompt.getID();
                    String originID = myWebElementPrompt.getOrigin().getID();
                    int promptType = myWebElementPrompt.getType();
                    int originType = myWebElementPrompt.getOrigin().getType();
                    
                    answer.setAnswerMode(4);
                    answer.setPrompt(promptID, promptType);
                    answer.setPromptOrigin(originID, originType);
                    
                System.out.println(answers.size());
                // This particular method saves a copy of the document in the same folder as the original, which is why the rwb is passed
                ApplyChanges(rwb, rwi);
                /* 
                 * The following lines of code can be used for debugging the HyperLinkAnswers
                */
                // Iterator<WebHyperLinkAnswer> answerIterator = answers.elements();
                // while (answerIterator.hasNext()) {
                //    WebHyperLinkAnswer answer = answerIterator.next();
                //    System.out.println("--> Link answer getAnswerMode: " + answer.getAnswerMode());
                //    System.out.println("--> Link answer getFormID: " + answer.getFormID());
                //    System.out.println("--> Link answer getPromptId: " + answer.getPromptId());
                //    System.out.println("--> Link answer getPromptOriginId: " + answer.getPromptOriginId());
                //    System.out.println("--> Link answer getPromptOriginType: " + answer.getPromptOriginType());
                //    System.out.println("--> Link answer getPromptType: " + answer.getPromptType());
                // }
            }
        }
        System.out.println("EOF");
    }
    
    public static void CreateSimpleHyperLinkForSingleMetric(WebIServerSession session, RWInstance rwi, RWBean rwb, String documentID, String documentToLinkTo, String gridName) throws WebObjectsException
    {
        System.out.println("Document status: " + rwi.pollStatusOnly());

        // loop grid metrics
        RWDefinition rwd = rwi.getDefinition();
        System.out.println("Document Name: " + rwd.getName());

        RWUnitDef[] units = rwd.findUnits(EnumRWUnitTypes.RWUNIT_GRIDGRAPH, gridName);
        RWGridGraphDef grid = (RWGridGraphDef) units[0];

        WebTemplate gridTemplate = grid.getViewInstance().getTemplate();
        WebTemplateMetrics metrics = gridTemplate.getTemplateMetrics();
        //Sets simple HyperLink on first metric
        WebTemplateMetric metric = metrics.get(0);
        WebHyperLinks links = metric.getHyperLinks();
        //If there are no HyperLinks on this metric, create one
        if (links.size() <= 0)
            links.add();
        //Modify only the first HyperLink for the current selected metric
        WebHyperLink link = links.get(0);
        
        //Setting up all objects to set the Hyperlink to point to the correct object
        WebObjectSource wos = session.getFactory().getObjectSource();
        WebObjectInfo targetObject = wos.getObject(documentToLinkTo, EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition, true);
        String targetName = targetObject.getName();
        //Setting the HyperLink information
        link.setLinkType(WebHyperLink.LINK_TYPE_EXECUTE);
        link.setDefault(true);
        link.setDefaultAnswerMode(EnumLinkAnswerMode.USE_DEFAULT_ANSWER);
        link.setTargetObject(targetObject);
        link.setDisplayText(targetName);
        link.setSelectorOptions("1");
        // This particular method saves a copy of the document in the same folder as the original, which is why the rwb is passed
        ApplyChanges(rwb, rwi);
    }
    //Using the RWBean and RWI, saves a copy of the document in the same folder location as the original document 
    public static void ApplyChanges(RWBean rwb, RWInstance rwi) throws WebObjectsException {
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
            newInst.saveAs(parentFolder, "ModifiedDocument2");

        } catch (WebBeanException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
