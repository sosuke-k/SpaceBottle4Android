exports.post = function(request, response) {
    // Use "request.service" to access features of your mobile service, e.g.:
    var azure = require('azure');
    var hub = azure.createNotificationHubService('spacebottlehub', 'Endpoint=sb://spacebottlehub-ns.servicebus.windows.net/;SharedAccessKeyName=DefaultFullSharedAccessSignature;SharedAccessKey=NHp5yMEwRZlx8IkteEDyEexTgMdULc0oHnPn4CMlYuU=');
    var data = '{'
        + '"satellite_id": "' + request.body.satelliteId + '"'
        + ', "ticket_id": "' + request.body.ticket_id + '"'
        + ', "message": "' + request.body.message + '"'
        + ', "message_id": "' + request.body.message_id + '"'
        + ', "message_text": "' + request.body.message_text + '"'
    + '}';
    console.log(JSON.stringify(request.body));
    /*hub.gcm.send(null, '{"data":{"message":"Notification Hub test notification"}}', function(error, outcome){
        if(!!error){
            request.respond(500, error);
        } else {
            console.log("Push send:" + JSON.stringify(outcome));
            request.respond(200, "success");
        }
    });*/
    
    
    request.service.tables.getTable("Device").where({
        user_id: request.body.userId
    }).read({
        success: function(results){
            if(results.length == 0){
                request.respond(404);
            } else {
                request.service.push.gcm.send(results[0].registration_id,data,{
                    success:function(){
                        request.respond(200);
                    }
                })
            }
        }
        , error: function(err){
            request.respond(500, err);
        }
    })
    
};
