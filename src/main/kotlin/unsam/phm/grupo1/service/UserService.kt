package unsam.phm.grupo1.service

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.stereotype.Service
import unsam.phm.grupo1.dto.UserUpdateRequestDTO
import unsam.phm.grupo1.entity.User
import unsam.phm.grupo1.repository.UserRepository
import java.util.Optional

@Service
class UserService {
    @Autowired
    lateinit var userRepository: UserRepository

    fun getById(id: Long): Optional<User>{
        val user = this.userRepository.findById(id)
        userRepository.flush()
        return user
    }
    fun getAll(): Iterable<User> = this.userRepository.findAll()

    fun editUser(userToEdit: User, newUser: UserUpdateRequestDTO): User {
        newUser.userName?.let { userToEdit.userName = it }
        newUser.profileImageURL?.let { userToEdit.profileImageURL = it }
        newUser.nationality?.let { userToEdit.nationality = it }
        newUser.birthDay?.let { userToEdit.birthDay = it }
        newUser.balance?.let { userToEdit.balance = it }

        return userRepository.save(userToEdit)
    }

    fun updateBalance(userId: Long, amount: Float): Int {
        return this.userRepository.updateBalance(userId, amount)
    }
}