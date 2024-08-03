MATCH (lodging:LodgingNeo)<-[:BOOKING]-(:User)
WITH lodging, COUNT(*) AS reservationCount
  WHERE reservationCount > 1
RETURN lodging