package unsam.phm.grupo1.controller

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import unsam.phm.grupo1.dto.AuthDTO
import unsam.phm.grupo1.dto.Message
import unsam.phm.grupo1.entity.User
import unsam.phm.grupo1.neo4j.NeoUserRepository
import unsam.phm.grupo1.service.AuthService
import java.lang.Exception
import java.util.Date

@RestController
@RequestMapping("api/auth")
@CrossOrigin(origins = ["http://localhost:4200"], allowCredentials = "true")
class AuthController {
    @Value("\${jwt.secret}")
    lateinit var secret: String

    @Autowired
    lateinit var authService: AuthService

    @Autowired
    lateinit var neoUserRepository: NeoUserRepository

    @PostMapping("register")
    @Operation(summary = "Registra un usuario con email y contraseña")
    fun register(@RequestBody data: AuthDTO): ResponseEntity<User> {
        val user = User().apply {
            this.email = data.email
            this.password = data.password
        }

        if(authService.getByEmail(data.email) != null){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario ya registrado")
        }
        //traer al usuario con el id mas alto
        /*val lastUser = this.neoUserRepository.findTopByOrderByIdDesc()
        println(lastUser)
        user.id = lastUser.id!! + 1
        println(user)*/
       // this.neoUserRepository.save(user)
        return ResponseEntity.ok(this.authService.register(user))
    }

    @PostMapping("login")
    fun login(@RequestBody data: AuthDTO, response: HttpServletResponse): ResponseEntity<Message>{
        val user = authService.getByEmail(data.email)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario con este mail no existe")

        if(!user.comparePassword(data.password)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Contraseña invalida")
        }

        val issuer = user.id.toString()
        val jwt = Jwts.builder()
            .setIssuer(issuer)
            .setExpiration(Date(System.currentTimeMillis() + 60 * 35 * 1000)) // 1 dia
            .signWith(SignatureAlgorithm.HS256, secret) //Poner el segundo valor en environments
            .compact()

        var cookie = Cookie("jwt", jwt)
        cookie.isHttpOnly = true

        response.addCookie(cookie)

        return ResponseEntity.ok(Message("Success"))
    }

    @GetMapping("user")
    fun user(@CookieValue("jwt") jwt: String?): ResponseEntity<User> {
        try {
            if (jwt == null || jwt == "") {
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado")
            }

            val body = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwt).body

            return ResponseEntity.ok(this.authService.getById(body.issuer.toLong()))
        } catch (e: Exception) {
            throw e
        }
    }

    @PostMapping("logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Message> {
        var cookie = Cookie("jwt", "")
        cookie.maxAge = 0

        response.addCookie(cookie)

        return ResponseEntity.ok(Message("Success"))
    }

    @PostMapping("change-password")
    fun changePassword(@RequestBody data: AuthDTO): ResponseEntity<User> {
        val user = authService.getByEmail(data.email)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario con ese mail no existe")

        val userUpdated = authService.changePassword(user, data.password)
            ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al cambiar la contraseña")

        return ResponseEntity.ok(userUpdated)
    }
}