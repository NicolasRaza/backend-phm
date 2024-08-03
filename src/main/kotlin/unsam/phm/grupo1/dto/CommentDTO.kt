package unsam.phm.grupo1.dto

import unsam.phm.grupo1.domain.Lodging.Booking
import java.time.LocalDate

data class CommentDTO(
    var nameLodging: String,
    var bookingId: Long,

    var imageCreatorLodging: String,
    var nameCreatorLodging: String,

    var imageAuthor: String,
    var nameAuthor: String,

    var score: Float,
    var comment: String,
    var publishedComment: LocalDate,
)

//fun Booking.toCommentDTO() = CommentDTO(
//    nameLodging = this.nameLodging,
//    imageCreatorLodging = this.imageCreatorLodging,
//    nameCreatorLodging = this.nameCreatorLodging,
//    score = this.score,
//    comment = this.comment,
//    publishedComment = this.publishedComment,
//)
