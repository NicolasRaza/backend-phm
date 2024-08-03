package unsam.phm.grupo1.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany
import jakarta.transaction.Transactional
import org.springframework.data.neo4j.core.schema.IdGenerator
import org.springframework.data.neo4j.core.schema.GeneratedValue as GeneratedValueNeo4j
import org.springframework.data.annotation.Id as IdNeo4j
//import org.springframework.data.neo4j.core.schema.Id as IdNeo4j
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Property
import org.springframework.data.neo4j.core.schema.Relationship
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.concurrent.atomic.AtomicLong
import unsam.phm.grupo1.dto.UserDTO
import java.time.LocalDate

@Entity(name = "user_info")
@Node("User")
open class User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @IdNeo4j
    //@GeneratedValueNeo4j(AutoIncrementIdGenerator::class)
    var id: Long? = null

    //@IdNeo4j @GeneratedValueNeo4j(AutoIncrementIdGenerator::class)
    //var neoId: Long? = null

    @Property("email")
    @Column(unique = true)
    var email: String = ""

    @Column
    var password: String = ""
        @JsonIgnore
        get() = field
        set(value) {
            val passwordEncoder = BCryptPasswordEncoder()
            field = passwordEncoder.encode(value)
        }

    @Column(length = 30)
    var userName: String = this.createNewUsername()

    @Column
    var balance: Int = 0

    @Column
    var nationality: String = ""

    @Column
    var birthDay: LocalDate = LocalDate.now().withYear(LocalDate.now().year - 18)

    @Column(nullable = true, length = 2048)
    var profileImageURL: String? = null

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @Relationship(type = "FRIEND_BY", direction = Relationship.Direction.OUTGOING)
    var friends: MutableSet<User> = mutableSetOf()



    fun comparePassword(password: String): Boolean {
        return BCryptPasswordEncoder().matches(password, this.password)
    }

    private fun createNewUsername(): String {
        return "user" + (0..999999).random().toString()
    }

    fun addFriend(user: User) {
        this.friends.add(user)
    }

    fun deleteFriend(friend: User) {
        this.friends.removeIf { it.id == friend.id }
    }
}

class AutoIncrementIdGenerator : IdGenerator<Long> {

    companion object {
        private val counter = AtomicLong(0)
    }


    override fun generateId(primaryLabel: String, entity: Any): Long {
        return counter.incrementAndGet()
    }
}
