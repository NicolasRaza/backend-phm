package unsam.phm.grupo1.controller

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import unsam.phm.grupo1.dto.Message
import unsam.phm.grupo1.entity.User
import unsam.phm.grupo1.service.UserFriendService

@RestController
@CrossOrigin("*")
@RequestMapping("api/user-friend")
class UserFriendController {
    @Autowired
    lateinit var userFriendService: UserFriendService;

    @GetMapping("{id}")
    fun getFriends(
        @PathVariable id: Long,
        @RequestParam page: Int?,
        @RequestParam size: Int?
    ): ResponseEntity<Iterable<User>> {
        try {
            this.validatePaginationParams(page, size)

            val friends = this.userFriendService.getSomeFriends(id, page ?: 0, size ?: 10)

            return ResponseEntity.ok(friends)
        } catch (e: NoSuchElementException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e)
        } catch (e: Exception) {
            throw e
        }
    }

    private fun validatePaginationParams(initial: Int?, size: Int?) {
        if (initial != null &&
            size != null &&
            (initial < 0 || size < 0)) {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Initial and size must be greater than 0")
        }
    }

    @PostMapping("{id}/{friend-id}")
    @Transactional
    fun addFriend(@PathVariable("id") userId: Long, @PathVariable("friend-id") friendId: Long): ResponseEntity<Message> {
        try{
            this.userFriendService.addFriend(userId, friendId)

            return ResponseEntity.ok(Message("Friend added successfully"))
        } catch (e: NoSuchElementException ) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e)
        } catch (e: Exception) {
            throw e
        }
    }

    @DeleteMapping("{id}/{friend-id}")
    @Transactional
    fun removeFriend(@PathVariable("id") userId: Long,@PathVariable("friend-id") friendId: Long): ResponseEntity<Message> {
        try {
            this.userFriendService.deleteFriend(userId, friendId)

            return ResponseEntity.ok(Message("Friend with id $friendId removed successfully from user with id $userId"))
        } catch (e: NoSuchElementException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e)
        } catch (e: Exception) {
            throw e
        }
    }
    @GetMapping("/{id}/suggest")
    fun getFriendSuggestions(
        @PathVariable("id") userId: Long,
        @RequestParam page: Int?,
        @RequestParam size: Int?)
    : ResponseEntity<Iterable<User>>{
        try{
            val friendsSuggestions = this.userFriendService.getFriendSuggestions(userId, page ?: 0, size ?: 10)

            return ResponseEntity.ok(friendsSuggestions)
        } catch (e: NoSuchElementException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e)
        } catch (e: Exception) {
            throw e
        }
    }
}