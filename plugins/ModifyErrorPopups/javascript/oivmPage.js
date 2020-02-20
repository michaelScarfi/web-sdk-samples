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
        passedError.btns = [newButton(errorButtonText, function() {
            redirectToNewPage();
            //refreshPage();
        }, errorValue), newButton(mstrmojo.desc(221, "Cancel"), function() {
          mstrmojo.form.send({
            evt: 3010,
            src: mstrApp.name
          });
        }, errorValue)];
        break;
      default:
        passedError.btns = [newButton(mstrmojo.desc(212, "Continue"), function() {
          mstrmojo.form.send({
            evt: 3026,
            src: mstrApp.name + "." + mstrApp.pageName + ".3026"
          });
        }, errorValue)];
    }
  }
}


//MARK: - Helper Functions

function getIsRWB() {
  return (mstrApp.docModel && mstrApp.docModel.bs) ||
  (mstrApp.rootCtrl && mstrApp.rootCtrl.docCtrl &&
    mstrApp.rootCtrl.docCtrl.model && mstrApp.rootCtrl.docCtrl.model.bs) ||
  (mstrApp.docModelData && mstrApp.docModelData.bs);
}


//Mark: - Custom Button Functions

//Redirect to new document from error message
function redirectToNewPage() {
    mstrmojo.form.send({
      evt: 2048001,
      src: mstrApp.name + "." + mstrApp.pageName + ".2048001",
      documentID: newDocumentToRedirectTo,
      currentViewMedia: 1,
      visMode: 0,
      connmode: authType
    });
}

//Triger refresh event for after redirect action. You can also check the auth mode and only trigger it for this - mstrmojo.EnumAuthenticationModes
function refreshPage() {
  var newEvent = {
      evt: 5005,
      src: mstrApp.name + "." + mstrApp.pageName + ".5005"
    },
  if (getIsRWB()) {
    newEvent.rwb = getIsRWB();
  }
  mstrmojo.form.send(newEvent);
}
