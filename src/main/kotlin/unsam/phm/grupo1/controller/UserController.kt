package unsam.phm.grupo1.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import unsam.phm.grupo1.dto.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import unsam.phm.grupo1.entity.User
import unsam.phm.grupo1.neo4j.NeoUserRepository
import unsam.phm.grupo1.service.UserService
import java.util.*

@RestController()
@CrossOrigin("*")
@RequestMapping("api/user")
class UserController(){
    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var userNeoUserRepository: NeoUserRepository

    @GetMapping()
    fun getAll(): ResponseEntity<Iterable<User>> {
        try{
            return ResponseEntity.ok(this.userService.getAll())
        } catch (e: NoSuchElementException ) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e)
        } catch (e: Exception) {
            throw e
        }
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Long): ResponseEntity<User> {
        try{
            val user = this.userService.getById(id).get()

            return ResponseEntity.ok(user)
        } catch (e: NoSuchElementException ) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e)
        } catch (e: Exception) {
            throw e
        }
    }

    @PatchMapping("/{id}")
    fun update(@PathVariable("id") userId: Long, @RequestBody user: UserUpdateRequestDTO): ResponseEntity<User> {
        try{

            //actualizar usuario tambien en neo4j


            val userToUpdate = this.userService.getById(userId).get()

            val updatedUser = userService.editUser(userToUpdate, user)
            return ResponseEntity.ok(updatedUser)
        } catch (e: NoSuchElementException ) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e)
        } catch (e: Exception) {
            throw e
        }
    }

    //create user in neo4j
    @PostMapping("/neo4j")
    fun createUserNeo4j(@RequestBody user: User): ResponseEntity<User> {
        try{
            val userResponse = this.userNeoUserRepository.save(user)
            return ResponseEntity.ok(userResponse)
        } catch (e: NoSuchElementException ) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e)
        } catch (e: Exception) {
            throw e
        }
    }

}