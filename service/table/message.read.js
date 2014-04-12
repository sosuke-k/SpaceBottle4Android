function read(query, user, request) {
    query.where({user_id: user.userId});
    request.execute();

}