package unsam.phm.grupo1.neo4j

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import unsam.phm.grupo1.entity.LodgingNeo
import java.time.LocalDate

@Repository
interface NeoLodgingRepository: Neo4jRepository<LodgingNeo, String> {


    @Query("""
    MATCH (u:User)-[:FRIEND_BY]-(friend:User)-[:BOOKING]-(lodging:LodgingNeo)
    WHERE u.id = ?#{[0]}
        AND lodging.deletedate IS NULL
        AND lodging IS NOT NULL
    RETURN lodging
    SKIP ?#{[1]}
    LIMIT ?#{[2]}
""")
    fun findbyFriends(userId: Long, skip: Long, limit: Long): List<LodgingNeo>


    @Query("MATCH (n:LodgingNeo) WHERE n.id = ?#{[0]} SET n.deletedate = ?#{[1]}")
    fun desactivateLodging(lodgingId: String, deleteDate: LocalDate)
}