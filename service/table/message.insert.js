function insert(item, user, request) {
    item.user_id = user.userId;
    if(!item.ticket_id){
        request.respond(403, "You need ticket_id");
    } else {
        tables.getTable("Ticket").where({
            id: item.ticket_id
        }).read({
            success:function(results){
                request.execute();
            }
            , error: function(err){
                request.respond(500, err);
            }
        })
    }
}