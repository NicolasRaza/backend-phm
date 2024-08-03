package unsam.phm.grupo1.user

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import unsam.phm.grupo1.builder.UserBuilder
import unsam.phm.grupo1.dto.AuthDTO
import unsam.phm.grupo1.entity.User
import unsam.phm.grupo1.repository.UserRepository

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("User Friend Controller Test")
@Transactional
class AuthControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var userRepository: UserRepository

    lateinit var pedro: User

    val defaultPassword = "123456789"

    @BeforeEach
    fun setup() {
        pedro = UserBuilder(User(), userRepository)
            .withEmail("pedro51@gmail")
            .withPassword(defaultPassword)
            .save()
    }

    @Test
    fun `Se registra un usuario correctamente`() {
        // Arrange
        val registerData = AuthDTO(
            email = "nicovillamonte@gmail.com",
            password = "123456789"
        )

        val objectMapper = ObjectMapper()
        val requestBody = objectMapper.writeValueAsString(registerData)

        // Act
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
            )

        // Assert
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(registerData.email))
    }

    @Test
    fun `Se intenta registrar un usuario con un email ya registrado`() {
        // Arrange
        val registerData = AuthDTO(
            email = "pedro51@gmail",
            password = "123456789"
        )

        val objectMapper = ObjectMapper()
        val requestBody = objectMapper.writeValueAsString(registerData)

        // Act
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )

        // Assert
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `Un usuario registrado se loguea correctamente`() {
        // Arrange
        val loginData = AuthDTO(
            email = pedro.email,
            password = defaultPassword
        )

        val objectMapper = ObjectMapper()
        val requestBody = objectMapper.writeValueAsString(loginData)

        // Act
        val res = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )

        // Assert
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        // Obtain the token saved in cookies in the response
        val token = res.response.cookies[0].value

        // Validate if token is not empty or null
        Assertions.assertNotNull(token)
    }

    @Test
    fun `Un usuario intenta loguearse con datos incorrectos`() {
        // Arrange
        val loginData = AuthDTO(
            email = "pedro51@gmail",
            password = "1234567890"
        )

        val objectMapper = ObjectMapper()
        val requestBody = objectMapper.writeValueAsString(loginData)

        // Act
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )

        // Assert
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun `Se obtiene el usuario logueado`() {
        // Arrange
        val res = logInUser()

        // Act
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/auth/user")
                .cookie(res.response.cookies[0])
        )

        // Assert
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(pedro.email))
    }

    @Test
    fun `Se intenta obtener un usuario logueado cuando no lo hay`() {
        // Act
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/auth/user")
        )

        // Assert
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun `Un usuario logueado se desloguea correctamente`() {
        // Arrange
        val res = logInUser()

        // Acts and Asserts
        val logoutRes = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/logout")
                .cookie(res.response.cookies[0])
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val cookie = logoutRes.response.cookies[0]
        println("Cookie: ${cookie.name}=${cookie.value}")

        // Validate if user is logged out
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/auth/user")
                .cookie(logoutRes.response.cookies[0])
        ).andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun `Un usuario cambia su contraseña`() {
        val loginData = AuthDTO(
            email = pedro.email,
            password = "estaEsUnaNuevaContraseña"
        )

        val objectMapper = ObjectMapper()
        val requestBody = objectMapper.writeValueAsString(loginData)

        // Change password
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(MockMvcResultMatchers.status().isOk)

        // Login with old password should throw an error
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(
                        AuthDTO(
                            email = pedro.email,
                            password = defaultPassword
                        )
                    )
                ))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun `Un usuario intenta cambiar su contraseña con datos incorrectos`(){
        // Arrange
        val loginData = AuthDTO(
            email = "anabel@gmail", // Email incorrecto
            password = "estaEsUnaNuevaContraseña"
        )

        val objectMapper = ObjectMapper()
        val requestBody = objectMapper.writeValueAsString(loginData)

        // Act
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))

        // Assert
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    private fun logInUser(): MvcResult {
        // Arrange
        val loginData = AuthDTO(
            email = pedro.email,
            password = defaultPassword
        )

        val objectMapper = ObjectMapper()
        val requestBody = objectMapper.writeValueAsString(loginData)

        // Act
        return mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
    }
}