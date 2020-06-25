This customization is utilized to modify an Error popup's title, message, and buttons.
The two examples here are for actions to take when a session expires.
1. Refresh the page
2. Redirect to new page

NOTE: It is assumed that the new document is on the same server in the same project


Within the oivmPage.js JavaScript file, these values can be modified by changing the following variables:

- Refresh Page:
    - title
    - message
    - errorButtonText

- Redirect to New Page:
    - title
    - message
    - errorButtonText
    - newDocumentToRedirectTo
    - authType


To find the Error ID:

In MicroStrategy Web:
1. Inspect the page
2. Go to the "Network" tab in the inspect view
3. Trigger the Error to pop up on the screen
4. Select in the network view the RED value that appears from the error
5. Scroll through object until X-MSTR-TaskErrorCode is found

X-MSTR-TaskErrorCode is the Error ID to use with this customization.
