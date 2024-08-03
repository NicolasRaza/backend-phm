package unsam.phm.grupo1.neo4j

import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.stereotype.Repository
import unsam.phm.grupo1.entity.User
import java.util.*

@Repository
interface NeoUserRepository: Neo4jRepository<User, Long>{


    @Query("MATCH (u:User {email: \$email}) RETURN u")
    fun findByEmail(email: String): User

    @Query("MATCH (u:User) RETURN u ORDER BY u.id DESC LIMIT 1")
    fun findTopByOrderByIdDesc(): User
}