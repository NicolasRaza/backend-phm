package unsam.phm.grupo1.domain.Lodging

import java.time.LocalDate

class Booking(
    var lodgingId: Long = 0,
    var userId: Long = 0,
    var fromDate: LocalDate = LocalDate.now(),
    var toDate: LocalDate = LocalDate.now(),
    var totalCost: Float = 0f,
) {
    var id: Long = -1

    var score: Float? = null
    var comment: String? = null
    var publishedComment: LocalDate? = null

    fun score(score: Float, comment: String, publishedComment: LocalDate) {
        this.score = score
        this.comment = comment
        this.publishedComment = publishedComment
    }


    fun deleteScore(){
        this.score= null;
        this.comment = null;
        this.publishedComment = null
    }



}