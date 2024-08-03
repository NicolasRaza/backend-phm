package unsam.phm.grupo1.builder

import unsam.phm.grupo1.entity.User
import unsam.phm.grupo1.neo4j.NeoUserRepository
import unsam.phm.grupo1.repository.UserRepository

class UserBuilder(val user: User, val userRepository: UserRepository, val neoUserRepository: NeoUserRepository) {

    fun withEmail(email: String): UserBuilder {
        user.email = email
        return this
    }

    fun withPassword(password: String = "123456"): UserBuilder {
        user.password = password
        return this
    }

    fun withUsername(userName: String): UserBuilder {
        user.userName = userName
        return this
    }

    fun withFriends(friends: MutableSet<User>): UserBuilder {
        user.friends = friends
        return this
    }

    fun build(): User {
        if(user.password == null) user.password = "123456"
        return user
    }

    fun withBalance(balance: Float): UserBuilder {
        user.balance = balance.toInt()
        return this
    }

    fun save(): User {
        val userEntity = userRepository.saveAndFlush(this.build())
        neoUserRepository.save(userEntity)
        return userEntity
    }
}