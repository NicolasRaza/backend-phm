package unsam.phm.grupo1.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import unsam.phm.grupo1.entity.Review
import unsam.phm.grupo1.entity.ReviewInfoLodging
import unsam.phm.grupo1.entity.ReviewInfoProfile
import unsam.phm.grupo1.repository.ReviewByUserReponseDTO
import unsam.phm.grupo1.repository.ReviewRepository
import java.time.LocalDate

@Service
class ReviewService {

    @Autowired
    lateinit var reviewRepository: ReviewRepository

    @Autowired
    lateinit var lodgingService: LodgingService

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var bookingService: LodgingBookingService

    fun getByBookingId(bookingId: Long): Review {
        return this.reviewRepository.findByBookingId(bookingId).get()
    }

    fun getById(id: Long): Review? {
        return this.reviewRepository.findById(id).orElseThrow{
            ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró ningún review con el ID $id")
        }
    }
    fun rate(review: Review, lodgingId: String): Review {
        val review = this.reviewRepository.save(review)
        val lodgingAvgScore = this.reviewRepository.getAverageScoreWithBookingId(review.bookingId!!)
        val lodging = lodgingService.getLodgingById(lodgingId)
        if (lodgingAvgScore != null) {
            lodging.averageScore = lodgingAvgScore
        }
        lodgingService.updateLodging(lodging)
        return review
    }

    fun deleteReviewById(reviewId: Long): Boolean {
        val result = try {
            reviewRepository.deleteById(reviewId)
            true
        } catch (e: EmptyResultDataAccessException) {
            false
        }
        return result
    }

    fun getReviewsByLodgingId(lodgingId: String): List<ReviewInfoLodging> {
        val results: List<Array<Any>> = reviewRepository.findTop3ByLodgingIdOrderByPublishedCommentDesc(lodgingId)

        return results.map { result ->
            val defaultPhotoUrl = "https://youtube.nicolasvillamonte.com/assets/imgs/avatar.png"
            val photoUrl = result.getOrNull(5) as? String ?: defaultPhotoUrl

            ReviewInfoLodging(
                result.getOrNull(0) as? Long ?: 0L,
                result.getOrNull(1) as? String ?: "",
                result.getOrNull(2) as? Int ?: 0,
                result.getOrNull(3) as? LocalDate ?: LocalDate.now(),
                result.getOrNull(4) as? String ?: "",
                photoUrl
            )
        }
    }


    fun getReviewsByUserId(userId: Long): List<Any> {
        var results: List<ReviewByUserReponseDTO> = emptyList()

        try {
            results = reviewRepository.findByUserId(userId)
            results.map {
                val lodging = lodgingService.getLodgingById(it.lodgingId)
                val user = this.userService.getById(lodging.userId!!).get()
                it.lodgingOwner = user.userName
                it.lodgingOwnerPhotoUrl = user.profileImageURL
                it.lodgingName = lodging.name
                it
            }
        } catch (e: Exception) {
            System.out.println(e)
        }

        return results
    }

    fun getAverageScoreWithBookingId(bookingId: Long): Float? {
        return this.reviewRepository.getAverageScoreWithBookingId(bookingId)
    }
}
