package unsam.phm.grupo1.user

import io.kotest.core.spec.DisplayName
import jakarta.transaction.Transactional
import org.json.JSONArray
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
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
@DisplayName("User Controller Test")
@Transactional
class UserControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var userRepository: UserRepository

    lateinit var pedro: User
    lateinit var juan: User

    @BeforeEach
    fun setup() {
        pedro = UserBuilder(User(), userRepository)
            .withEmail("pedro51@gmail").save()

        juan = UserBuilder(User(), userRepository)
            .withEmail("juan@gmail.com").save()
    }

    @Test
    fun `Obtener todos los usuarios`(){
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user"))
                        .andExpect(MockMvcResultMatchers.status().isOk)
                        .andExpect(MockMvcResultMatchers.jsonPath("$").isArray)
                        .andExpect(MockMvcResultMatchers.jsonPath("$.length()")
                            .value(2))
    }

    @Test
    fun `Obtener un usuario correctamente`() {
        val user = userRepository.save(User().apply {
            email = "carlos@gmail.com"
            password = "123456789"
        })

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/${user.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(user.id))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user.email))
    }

    @Test
    fun `Al intentar obtener un usuario que no existe arroja un error 404`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/999999999"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `Al actualizar el nombre de usuario de un usuario se actualiza correctamente`() {
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/user/${pedro.id}")
            .content("{\"userName\": \"nicovillamonte\"}")
            .contentType("application/json"))

            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(pedro.id))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("nicovillamonte"))
    }
}