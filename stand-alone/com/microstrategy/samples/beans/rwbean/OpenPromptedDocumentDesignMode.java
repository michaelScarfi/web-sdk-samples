package com.microstrategy.samples.beans.rwbean;

import com.microstrategy.samples.sessions.SessionManager;
import com.microstrategy.web.beans.BeanFactory;
import com.microstrategy.web.beans.RWBean;
import com.microstrategy.web.beans.WebBeanException;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebPrompt;
import com.microstrategy.web.objects.WebPrompts;
import com.microstrategy.web.objects.rw.EnumRWExecutionModes;
import com.microstrategy.web.objects.rw.RWInstance;
import com.microstrategy.webapi.EnumDSSXMLStatus;

public class OpenPromptedDocumentDesignMode {


    public static void main(String[] args) {


        // Connectivity for the intelligence server
        final String INTELLIGENCE_SERVER_NAME = "10.23.3.184";
        final String PROJECT_NAME = "MicroStrategy Tutorial";
        final String MICROSTRATEGY_USERNAME = "Administrator";
        final String MICROSTRATEGY_PASSWORD = "";

        // Defining which prompted document to open in design mode
        final String SOURCE_DOCUMENT = "7AA28C4A4F3BAA7CF40C388AD5B2E556";

        // Establish a session
        WebIServerSession session = SessionManager.getSessionWithDetails(INTELLIGENCE_SERVER_NAME, PROJECT_NAME,
            MICROSTRATEGY_USERNAME, MICROSTRATEGY_PASSWORD);

        try {
            // Obtains Instance of Prompted Document that has had prompts answered
            RWBean rwb = (RWBean) BeanFactory.getInstance().newBean("RWBean");
            RWInstance rwi = openDocumentInstance(rwb, SOURCE_DOCUMENT, session);

            // Prints the name of the Document
            System.out.println(rwi.getDefinition().getName());
        } catch (WebBeanException | WebObjectsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * This method opens a Prompted Document in design mode using a RWBean, the documentID and an existing server session
     * 
     * @param rwb
     * @param objectID
     * @param session
     * @return
     * @throws WebBeanException
     */
    public static RWInstance openDocumentInstance(RWBean rwb, String objectID, WebIServerSession session)
        throws WebBeanException {
        rwb.setObjectID(objectID);
        rwb.setSessionInfo(session);
        rwb.setExecutionMode(EnumRWExecutionModes.RW_MODE_DESIGN);
        RWInstance rwi = rwb.getRWInstance();

        // Attempts to Answer Prompts of Document
        tryToAnswerPromptsSilently(rwi);
        return rwi;
    }

    /**
     * Takes an RWInstance, attempts to resolve the prompt answers and returns the Instance once the prompts have been answered
     * 
     * @param rwi
     * @return
     */
    private static void tryToAnswerPromptsSilently(RWInstance rwi) {
        try {
            int documentStatus = rwi.pollStatusOnly();
            while (documentStatus == EnumDSSXMLStatus.DssXmlStatusPromptXML) {
                WebPrompts webPrompts = rwi.getPrompts();
                for (int i = 0; i < webPrompts.size(); i++) {
                    WebPrompt webPrompt = webPrompts.get(i);

                    // If prompts are required, attempt to answer
                    if (webPrompt.isRequired()) {
                        System.out.println("Prompt + " + webPrompt.getName() + " is required.");

                        // Use default answer if available
                        if (webPrompt.hasDefaultAnswer()) {
                            System.out.println("This prompt contains a default answer. Attempting to answer using the default answer");
                            webPrompt.resetToDefault();

                        } // If unable to utilize default answer, then use current answer
                        else if (webPrompt.hasAnswer()) {
                            System.out.println("Attempting to answer the prompt using the current answer");

                        } // If unable to use current or default answer, cancel the prompts and do not send a response to the Intelligence Server
                        else {
                            System.out.println("The prompt was not answered. Cancelling prompt. This document still has unanswered prompts.");
                            webPrompt.setCanceled();
                        }
                    } // Prompts are not required. Do not send a response to Intelligence Server for prompts
                    else {
                        System.out.println(
                            "Prompts are not required to be answered. Attempting to close the prompt and not send a response to the I-Server.");
                        webPrompt.setClosed(true);
                    }
                }
                webPrompts.validate();
                webPrompts.answerPrompts();
                documentStatus = rwi.pollStatusOnly();
            }
        } catch (WebObjectsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}


