db.getCollection("lodging").find({
    "l_type" : "Hut"
}).count()