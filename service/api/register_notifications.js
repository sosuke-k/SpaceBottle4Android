exports.post = function(request, response) {

    // Create a notification hub instance.
    var azure = require('azure');
    var hub = azure.createNotificationHubService('spacebottlehub', 
        'Endpoint=sb://spacebottlehub-ns.servicebus.windows.net/;SharedAccessKeyName=DefaultFullSharedAccessSignature;SharedAccessKey=NHp5yMEwRZlx8IkteEDyEexTgMdULc0oHnPn4CMlYuU=');

    // Get the registration info that we need from the request. 
    var platform = request.body.platform;
    var userId = request.user.userId;
    var installationId = request.header('X-ZUMO-INSTALLATION-ID');

    // Function called when registration is completed.
    var registrationComplete = function(error, registration) {
        if (!error) {
            // Return the registration.
            response.send(200, registration);
        } else {
            response.send(500, 'Registration failed!');
        }
    }
    // Function called to log errors.
    var logErrors = function(error) {
        if (error) {
            console.error(error)
        }
    }
    // Get existing registrations.
    hub.listRegistrationsByTag(installationId, function(error, existingRegs) {
        var firstRegistration = true;
        if (existingRegs.length > 0) {
             for (var i = 0; i < existingRegs.length; i++) {
                if (firstRegistration) {
                    // Update an existing registration.
                    if (platform === 'win8') {
                        existingRegs[i].ChannelUri = request.body.channelUri;                        
                        hub.updateRegistration(existingRegs[i], registrationComplete);                        
                    } else if (platform === 'ios') {
                        existingRegs[i].DeviceToken = request.body.deviceToken;
                        hub.updateRegistration(existingRegs[i], registrationComplete);
                    } else {
                        response.send(500, 'Unknown client.');
                    }
                    firstRegistration = false;
                } else {
                    // We shouldn't have any extra registrations; delete if we do.
                    hub.deleteRegistration(existingRegs[i].RegistrationId, logErrors);
                }
            }
        } else {
            // Create a new registration.
            var template;
            if (platform === 'win8') {                
                template = { text1: '$(message)' };              
                hub.wns.createToastText01Registration(request.body.channelUri, 
                [userId, installationId], template, registrationComplete);
            } else if (platform === 'ios') {
                template = '{\"aps\":{\"alert\":\"$(message)\"}, \"inAppMessage\":\"$(message)\"}';
                hub.apns.createTemplateRegistration(request.body.deviceToken, 
                [userId, installationId], template, registrationComplete);
            } else {
                response.send(500, 'Unknown client.');
            }
        }
    });
}