var Zip = function() { }
	
Zip.prototype.getZip = function(file, successCallback, failureCallback) {
	cordova.exec(successCallback, failureCallback, 'Zip', '', [file]);
};

	
if(!window.plugins) {
    window.plugins = {};
}
if (!window.plugins.Zip) {
    window.plugins.Zip = new Zip();
}


