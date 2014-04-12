exports.get = function(request, response) {
    console.log(JSON.stringify(request.query));
    if(!!request.query.satellite_id){
        console.log(JSON.stringify("will send messages of the satellite"));
        request.service.tables.getTable("Message").where({
            satellite_id: request.query.satellite_id
            , destination_user_id: null
        }).read({
            success: function(results){
                var messages = [];
                results.forEach(function(message){
                    messages.push(message.id);
                })
                response.send(200, messages);
            }
            , error: function(err){
                response.send(500, err);
            }
        });
    } else {
        request.service.tables.getTable("Message").where(function(){
            return this.destination_user_id == null || this.destination_user_id == undefined;
        }).read({
            success: function(results){
                var messages = [];
                results.forEach(function(message){
                    messages.push({
                        id: message.id
                        , satellite_id: message.satellite_id
                    });
                })
                response.send(200, messages);
            }
            , error: function(err){
                response.send(500, err);
            }
        });
    }
};