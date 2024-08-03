package unsam.phm.grupo1.service

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import unsam.phm.grupo1.entity.User
import unsam.phm.grupo1.neo4j.NeoUserRepository
import unsam.phm.grupo1.repository.UserRepository

@Service
class UserFriendService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var neoUserRepository: NeoUserRepository

    fun addFriend(userId: Long, friendId: Long) {
        if(userId == friendId) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede agregar como amigo a uno mismo.")
        }



        val user = this.userRepository.findById(userId).get()
        val friend = this.userRepository.findById(friendId).get()

        user.addFriend(friend)
        println(user.email)
        val neoUser = neoUserRepository.findByEmail(user.email)
        val neoFriend = neoUserRepository.findByEmail(friend.email)
        println(neoUser.email)
        println(neoFriend.email)
        userRepository.save(user)
        neoUser.addFriend(neoFriend)
        neoUserRepository.save(neoUser)
    }

    fun deleteFriend(userId: Long, friendId: Long) {
        val user = this.userRepository.findById(userId).get()
        val friend = this.userRepository.findById(friendId).get()

        user.deleteFriend(friend)
        userRepository.save(user)
    }

    fun getSomeFriends(id: Long, page: Int, size: Int): Page<User> {
        val user = this.userRepository.findById(id).get()
        return this.userRepository.getSomeFriends(user.id!!,
            PageRequest.of(page, size, Sort.Direction.ASC, "id")
        )
    }

    fun getFriendSuggestions(userId: Long, page: Int, size: Int): Iterable<User>? {
        val user = this.userRepository.findById(userId).get()

        return this.userRepository.getFriendSuggestions(user.id!!,
            PageRequest.of(page, size, Sort.Direction.ASC, "id")
        )
    }
}