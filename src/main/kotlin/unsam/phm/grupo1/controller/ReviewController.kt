package unsam.phm.grupo1.controller
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import unsam.phm.grupo1.dto.ReviewDTO
import unsam.phm.grupo1.entity.Review
import unsam.phm.grupo1.entity.ReviewInfoLodging
import unsam.phm.grupo1.entity.ReviewInfoProfile
import unsam.phm.grupo1.service.LodgingBookingService
import unsam.phm.grupo1.service.ReviewService
import unsam.phm.grupo1.service.LodgingService
import unsam.phm.grupo1.service.UserService
import java.util.NoSuchElementException

@RestController
@RequestMapping("api/review")
@CrossOrigin("*")
class ReviewController {

    @Autowired
    lateinit var reviewService: ReviewService
    @Autowired
    lateinit var lodgingService: LodgingService
    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var bookingService: LodgingBookingService

    @PostMapping("rate")
    fun rate(@RequestBody data: ReviewDTO): ResponseEntity<*> {
        var review: Review? = null
        try {
            review = reviewService.getByBookingId(data.bookingId)

            review.rate(data.comment, data.score, data.publishedComment)
        } catch (e: NoSuchElementException) {
            try {bookingService.getOne(data.bookingId)} catch (e: Exception) { throw e } // Verify if booking exists

            review = Review().apply {
                this.bookingId = data.bookingId
                this.rate(data.comment, data.score, data.publishedComment)
            }
        } catch (e: Exception) {
            throw e
        }

        return ResponseEntity.ok(
            this.reviewService.rate(review!!, data.lodgingId)
        )
    }

    @DeleteMapping("/{id}")
    fun deleteReviewById(@PathVariable id: Long): ResponseEntity<String> {
        val success = reviewService.deleteReviewById(id)
        return if (success) {
            ResponseEntity.ok("Review deleted successfully")
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found")
        }
    }

    @GetMapping("/lodging/{lodgingId}")
    fun getReviewsByLodgingId(@PathVariable lodgingId: String): List<Any> {
        if (lodgingService.getLodgingById(lodgingId) == null){
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "El lodging con ese id no existe")
        }

        return reviewService.getReviewsByLodgingId(lodgingId)
    }

    @GetMapping("/user/{userId}")
    fun getReviewsByUserId(@PathVariable userId: Long): List<Any> {
        if (userService.getById(userId).isEmpty){
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "El user con ese id no existe")
        }

        return reviewService.getReviewsByUserId(userId)
    }

    @GetMapping("/avg/{bookingId}")
    fun getAverageScoreWithBookingId(@PathVariable bookingId: Long): Float? {
        return reviewService.getAverageScoreWithBookingId(bookingId)
    }
}