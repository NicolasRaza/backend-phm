MATCH (user:User)-[:BOOKING]->(lodging:LodgingNeo)
WITH user, lodging.country AS destination, COUNT(*) AS reservations
  WHERE reservations > 5
RETURN user