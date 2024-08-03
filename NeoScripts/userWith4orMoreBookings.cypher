MATCH (u:User)-[b:BOOKING]->(l:LodgingNeo)
WITH u, count(b) AS numBookings
  WHERE numBookings > 4
RETURN u