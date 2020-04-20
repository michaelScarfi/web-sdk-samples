function findAndReplaceErrors() {
	var alertField = document.getElementById('mstrWeb_error');
	var alertMessageFields = alertField.getElementsByClassName("mstrAlertMessage");
	var alertMessage = alertMessageFields.length && alertMessageFields[0];

	console.log('findAndReplaceErrors');
	var errorMessageText = alertMessage.innerHTML;
	if (errorMessageText.contains('#errorLogin1')) {
	  alertMessage.innerHTML = 'Login attempt failed. <a href="http://google.com/">Click here</> to continue.';
	  return;
	}
};

console.log('onready hook setup');
document.addEventListener("DOMContentLoaded", findAndReplaceErrors);
