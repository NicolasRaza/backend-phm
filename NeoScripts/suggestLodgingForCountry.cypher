MATCH (lodging:LodgingNeo {id: "649b7258c41a0553d9c4020e"})
WITH lodging
MATCH (lodging)<-[:BOOKING]-(bookingUser:User)-[:BOOKING]->(otherLodging:LodgingNeo)
WHERE otherLodging <> lodging AND otherLodging.country = lodging.country
RETURN DISTINCT otherLodging