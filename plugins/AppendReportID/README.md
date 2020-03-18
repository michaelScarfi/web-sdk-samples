# AppendReportID Plugin

This folder is a plugin. It depends on the provided Java class. The source of the code can be found within the "src" folder.

## Requirement

When creating a new file subscription for a report via MicroStrategy Web, the reportID should be appended to the name.

## Method
This customization uses a custom event handler to:
- Intercept the action that should be customized (new file subscription is being created via MicroStrategy Web, e.g from the "file" menu in a report), by intercepting the HTTP request associated with a specific event number.
- Intercept an existing parameter in the HTTP request that already sets the name of the file.
- Change this parameter with the new file name.
- Pass the rest of the request to built-in event handler, after all desired changes have been made.

Event handlers are written in Java using the Web API and connected to Beans on one or more pages, using the pageConfig.xml file in the plugin. Java classes such as this must be compiled before deployment.

It is recommended that these kind of customizations are created using the Web Customization Editor, ensuring these dependencies are handled correctly and without error.

## Further Reading & References
- [SDK Documentation @ developer.microstrategy.com](https://developer.microstrategy.com).
- [Web Customization Editor](https://lw.microstrategy.com/msdz/MSDL/GARelease_Current/docs/projects/WebSDK/Content/topics/webcusteditor/WCE_Deployment_Instructions.htm) for simpler plugin creation.
- [Customizing Events](https://lw.microstrategy.com/msdz/MSDL/GARelease_Current/docs/projects/WebSDK/Content/topics/events/Customizing_Events.htm)
