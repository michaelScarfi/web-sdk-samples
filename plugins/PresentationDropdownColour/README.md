# CSS Customization

This folder is a plugin.

## Requirement

Change the background colour of dropdown selectors in report services documents to white, when executed in presentation mode.

This customization affects all drop down selectors in all documents on the web server.

## Method
This customization is made possible using CSS. The CSS selectors & properties are found using developer tools in a web browser.

Once the desired CSS has been found, the CSS can be made into a customization either via:
- (Recommended) A page-specific CSS file. This makes this CSS available only in one page (e.g only when a document is executed in presentation mode).
- A global CSS file. This makes the CSS available on all pages in MicroStrategy Web, although the changes only apply when matching CSS selectors are found on the web page.

Since this CSS change is only useful in documents executed in presentation mode, the changes have been applied using the page-specific CSS file.

Report services documents executed in presentation mode use the "OIVM" page in the MicroStrategy Web architecture.

When the page loads, the browser will try to load any oivmPage.css files found in `plugins/anyplugin/style/`

## Deployment
- Copy the "PresentationDropdownColour" folder to the MicroStrategyWeb "plugins" folder.
- Restart the web application server that hosts MicroStrategy Web (e.g Tomcat).
- Clear browser caches to confirm the changes have taken effect.

## Further Reading & References
- [SDK Documentation @ developer.microstrategy.com](https://developer.microstrategy.com).
- [Web Customization Editor](https://lw.microstrategy.com/msdz/MSDL/GARelease_Current/docs/projects/WebSDK/Content/topics/webcusteditor/WCE_Deployment_Instructions.htm) for simpler plugin creation.
- [Customizing CSS](https://lw.microstrategy.com/msdz/MSDL/GARelease_Current/docs/projects/WebSDK/Content/topics/datapresnt/CU_CSS_customizing_CSS.htm)