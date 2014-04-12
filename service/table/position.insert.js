function insert(item, user, request) {
    tables.getTable("Position").where({
        user_id: user.userId
    }).read({
        success: function(results){
            if(results.length == 0){
                item.user_id = user.userId;
                request.execute();
            } else {
                request.respond(403, 'Your position is exists.');
            }
        }
        , error: function(err){
            request.respond(500, err);
        }
    });
}