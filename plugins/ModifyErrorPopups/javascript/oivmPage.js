//Mark: - Variables to Change

var title = "Session Timeout";
var message = "The current session has ended.";
var errorButtonText = "Go to Main Page";
var newDocumentToRedirectTo = "B546031C492F99BEAB6BCCB91635B608";
var authType = 8;


//MARK: - Main Function

mstrmojo.OIVMPage.prototype.onError_ootb = mstrmojo.OIVMPage.onError;
mstrmojo.OIVMPage.prototype.onError = function(passedError) {
  var errorValue = "#666";
  var errorCode = passedError.code;

  if (errorCode) {
    var newButton = mstrmojo.Button.newInteractiveButton;

    //Change the error codes in the switch statement to the ones to overwrite
    switch (errorCode) {
      case -2147468986:
      case -2147467618:
        passedError.title = title;
        passedError.msg = message;
        passedError.btns = [
          newButton(errorButtonText, okButtonCallback, errorValue),
          newButton(mstrmojo.desc(221, "Cancel"), cancelButtonCallback, errorValue)
        ];
        break;
      default:
        passedError.btns = [
          newButton(mstrmojo.desc(212, "Continue"), continueButtonCallback, errorValue)
        ];
    }
  }
}


//MARK: - Callbacks

var okButtonCallback = function() {
  redirectToNewPage();
  //refreshPage();
}

var cancelButtonCallback = function() {
  dismissPopup();
}

var continueButtonCallback = function() {
  continueAction();
}


//Mark: - Button Functions

//Redirect to new document from error message
function redirectToNewPage() {
  sendForm(2048001, mstrApp.name + "." + mstrApp.pageName + ".2048001", newDocumentToRedirectTo);
}

//Triger refresh event for after redirect action. You can also check the auth mode and only trigger it for this - mstrmojo.EnumAuthenticationModes
function refreshPage() {
  sendForm(5005, mstrApp.name + "." + mstrApp.pageName + ".5005");
}

//Called by cancel button to dismiss the popup
function dismissPopup() {
  sendForm(3010, mstrApp.name);
}

//Called by default for all error messages
function continueAction() {
  sendForm(3026, mstrApp.name + "." + mstrApp.pageName + ".3026")
}


//MARK: - Helper Functions

function getIsRWB() {
  return (mstrApp.docModel && mstrApp.docModel.bs) ||
    (mstrApp.rootCtrl && mstrApp.rootCtrl.docCtrl &&
      mstrApp.rootCtrl.docCtrl.model && mstrApp.rootCtrl.docCtrl.model.bs) ||
    (mstrApp.docModelData && mstrApp.docModelData.bs);
}

//Sends action to mstrmojo based on the provided event and source
function sendForm(evt, src) {
  var newEvent = {
    evt: evt,
    src: src
  };

  if (getIsRWB()) {
    newEvent.rwb = getIsRWB();
  }

  mstrmojo.form.send(newEvent);
}

//Overloaded function that takes a new document ID as a parameter to redirect user
function sendForm(evt, src, redirectDocumentID) {
  var newEvent = {
    evt: evt,
    src: src,
    documentID: redirectDocumentID,
    currentViewMedia: 1,
    visMode: 0,
    connmode: authType
  };

  if (getIsRWB()) {
    newEvent.rwb = getIsRWB();
  }

  mstrmojo.form.send(newEvent);
}
