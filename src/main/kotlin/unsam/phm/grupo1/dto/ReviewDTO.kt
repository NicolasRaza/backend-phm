package unsam.phm.grupo1.dto

import java.time.LocalDate

data class ReviewDTO (
    val reviewId: Long,
    var comment: String,
    var bookingId : Long,
    var score: Int,
    var publishedComment: LocalDate,
    var lodgingId: String,
)