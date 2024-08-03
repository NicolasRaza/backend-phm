package unsam.phm.grupo1.user

import jakarta.transaction.Transactional
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import unsam.phm.grupo1.builder.UserBuilder
import unsam.phm.grupo1.entity.User
import unsam.phm.grupo1.repository.UserRepository

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("User Friend Controller Test")
@Transactional
class UserFriendControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

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
            .withEmail("ruben@gmail.com")
            .withFriends(mutableSetOf(rodrigo,juan))
            .save()
    }

    @Test
    fun `Un usuario obtiene todos sus amigos`() {
        val nico = UserBuilder(User(), userRepository)
            .withEmail("nicovillamonte@gmail")
            .withFriends(mutableSetOf(pedro, juan, rodrigo))
            .save()

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user-friend/${nico.id}"))
                        .andExpect(MockMvcResultMatchers.status().isOk)
                        .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray)
                        .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()")
                            .value(3))
    }

    @Test
    fun `Si un usuario no tiene amigos se devuelve una lista vacia`(){
        val nico = UserBuilder(User(), userRepository)
            .withEmail("nicovillamonte@gmail")
            .save()

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user-friend/${nico.id}"))
                        .andExpect(MockMvcResultMatchers.status().isOk)
                        .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray)
                        .andExpect(MockMvcResultMatchers.jsonPath("$.content").isEmpty)
    }

    @Test
    fun `Un usuario elimina a otro de amigos`(){
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user-friend/${ruben.id}/${rodrigo.id}"))
                        .andExpect(MockMvcResultMatchers.status().isOk)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user-friend/${ruben.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(1))
    }

    @Test
    fun `Un usuario intenta eliminar a un usuario inexistente de sus amigos`(){
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user-friend/${ruben.id}/999999999"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `Se obtienen sugerencias de amigos para el usuario ruben`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user-friend/${ruben.id}/suggest")
            .param("page", "0")
            .param("size", "10")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(1))
    }
}