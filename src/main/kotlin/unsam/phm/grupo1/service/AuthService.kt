package unsam.phm.grupo1.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import unsam.phm.grupo1.entity.User
import unsam.phm.grupo1.neo4j.NeoUserRepository
import unsam.phm.grupo1.repository.UserRepository

@Service
class AuthService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var neoUserRepository: NeoUserRepository

    fun register(user: User): User {
        //return this.userRepository.save(user)
        val userEntity = userRepository.saveAndFlush(user)
        neoUserRepository.save(userEntity)
        return userEntity
    }

    fun getByEmail(email: String): User? {
        return this.userRepository.findByEmail(email)
    }

    fun getById(id: Long): User? {
        return this.userRepository.findById(id).orElse(null)
    }

    fun changePassword(user: User, newPassword: String): User? {
        user.password = newPassword
        userRepository.save(user)

        return user
    }

}