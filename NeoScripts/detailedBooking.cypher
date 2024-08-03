MATCH (u:User)-[b:BOOKING]->(l:LodgingNeo)
  WHERE b.lodgingId = "649b7258c41a0553d9c4020e" AND b.fromDate >= date("2023-09-01") AND b.toDate <= date("2023-09-07")
RETURN u