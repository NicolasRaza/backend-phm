package unsam.phm.grupo1.repository

import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import unsam.phm.grupo1.entity.User
import java.util.*

@Repository
interface UserRepository :  PagingAndSortingRepository<User, Long>, CrudRepository<User, Long>, JpaRepository<User, Long> {
    fun findByEmail(email: String): User?

    @Query("""
        SELECT f FROM user_info u JOIN u.friends f WHERE u.id = :userId
        """)
    fun getSomeFriends(@Param("userId") userId: Long, pageable: PageRequest): Page<User>

    @Query("""
        SELECT u FROM user_info u WHERE u.id NOT IN (SELECT f.id FROM user_info u JOIN u.friends f WHERE u.id = :userId) AND u.id <> :userId
        """)
    fun getFriendSuggestions(@Param("userId") userId: Long, pageable: PageRequest): Page<User>?


    @Modifying
    @Transactional
    @Query("""
        UPDATE user_info u SET u.balance = :amount WHERE u.id = :userId
        """)
    fun updateBalance(@Param("userId") userId: Long, @Param("amount") amount: Float): Int

}

