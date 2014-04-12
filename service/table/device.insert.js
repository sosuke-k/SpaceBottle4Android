function insert(item, user, request) {
    if(!item.registration_id){
        request.respond(400, 'Your must send registration id.');
    } else {
        tables.getTable("Device").where({
            user_id: user.userId
            , registration_id: item.registration_id
        }).read({
            success: function(results){
                if(results.length == 0){
                    item.user_id = user.userId;
                    request.execute();
                } else {
                    request.respond(403, 'Your registration id is exists.');
                }
            }
        })
    }
}