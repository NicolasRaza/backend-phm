package unsam.phm.grupo1.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

import unsam.phm.grupo1.dto.*
import unsam.phm.grupo1.exception.InvalidRangeOfDateException
import unsam.phm.grupo1.service.LodgingBookingService
import unsam.phm.grupo1.service.UserService
import java.util.*


@RestController
@RequestMapping("api/booking")
@CrossOrigin(origins = ["*"])
class LodgingBookingController {
    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var lodgingBookingService: LodgingBookingService


    @GetMapping("{bookingId}")
    fun getOne(@PathVariable("bookingId") bookingId: Long): ResponseEntity<BookingDTO> {
        val booking = this.lodgingBookingService.getOne(bookingId)
        return ResponseEntity.ok(booking)
    }

    @GetMapping("/user/{userId}")
    fun getAllUserBooking(@PathVariable("userId") userId: Long): ResponseEntity<Iterable<BookingDTO>> {
        // TODO
        try {
            val user = this.userService.getById(userId)
            val bookings = this.lodgingBookingService.getByUserId(user)
            if(bookings == null) {
                throw ResponseStatusException(HttpStatus.NOT_FOUND, "Bookings Not Found")
            }
            return ResponseEntity.ok(bookings)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found", e)
        }
    }

    @PostMapping
    fun createBooking(
        @RequestBody booking: BookingDTO
    ): ResponseEntity<BookingDTO> {
        try {
            val bookingResponse = this.lodgingBookingService.createBooking(booking)

            return ResponseEntity.ok(bookingResponse)
        }catch (e: DataIntegrityViolationException){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking already exists", e)
        }catch (e: NoSuchElementException){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }catch (e: InvalidRangeOfDateException){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }catch (e: Exception){
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Booking not created", e)
        }

    }
}