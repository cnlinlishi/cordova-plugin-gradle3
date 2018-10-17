var exec = require('cordova/exec');


module.exports = {
    getInfo: function(absPath,onSuccess,onError){
        exec(onSuccess, onError, "ApkInfo", "getInfo", [absPath]);
    }
};