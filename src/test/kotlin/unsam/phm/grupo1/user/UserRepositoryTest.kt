package unsam.phm.grupo1.user

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import unsam.phm.grupo1.entity.User
import unsam.phm.grupo1.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import unsam.phm.grupo1.builder.UserBuilder

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    lateinit var userRepository: UserRepository

    lateinit var pedro: User
    lateinit var juan: User
    lateinit var rodrigo: User
    lateinit var ruben: User

    @BeforeEach
    fun setup() {
        pedro = UserBuilder(User(), userRepository)
                    .withEmail("pedro51@gmail").save()

        juan = UserBuilder(User(), userRepository)
                    .withEmail("juan@gmail.com").save()

        rodrigo = UserBuilder(User(), userRepository)
                    .withEmail("rodrigo@gmail.com").save()

        ruben = UserBuilder(User(), userRepository)
                    .withEmail("ruben@gmail.com").save()
    }

    @Test
    fun `Se obtiene una pagina de 2 amigos de un usuario`() {
        // Arrange
        val martin = this.userRepository.save(User().apply {
            email = "martin@gmail"
            password = "123456"

            this.addFriend(pedro)
            this.addFriend(juan)
            this.addFriend(rodrigo)
        })

        // Act
        val page = this.userRepository.getSomeFriends(martin.id!!,
            PageRequest.of(0, 2, Sort.Direction.ASC, "id")
        )

        //Assert
        assertEquals(2, page.content.size)
        assertFalse(page.content.contains(ruben))
    }

    @Test
    fun `Se obtienen sugerencias de usuarios para ser amigos`(){
        // Arrange
        val martin = this.userRepository.save(User().apply {
            email = "martin@gmail"
            password = "123456"

            this.addFriend(pedro)
        })

        // Act
        val page = this.userRepository.getFriendSuggestions(martin.id!!,
            PageRequest.of(0, 10, Sort.Direction.ASC, "id")
        )

        // Assert
        assertEquals(3, page?.content?.size)
        assertFalse(page?.content?.contains(pedro)!!)
    }

}