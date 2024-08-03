package unsam.phm.grupo1.entity
import jakarta.persistence.*
import java.time.LocalDate

@Entity(name = "review")
class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    @Column(name = "comment")
    var comment: String = ""

    @Column(name = "bookingId")
    var bookingId: Long? = null

    @Column(name = "score")
    var score: Int = 0

    @Column(name = "publishedComment")
    var publishedComment: LocalDate = LocalDate.now()

    fun rate(comment: String, score: Int, publishedComment: LocalDate) {
        this.comment = comment
        this.score = score
        this.publishedComment = publishedComment
    }

}

data class ReviewInfoLodging(
    val reviewId: Long,
    val comment: String,
    val score: Int,
    val publishedComment: LocalDate,
    val username: String,
    val photoUrl: String
)

data class ReviewInfoProfile(
    val reviewId: Long,
    val comment: String,
    val score: Int,
    val publishedComment: LocalDate,
    val lodgingOwnerPhotoUrl: String,
    val lodgingOwner: String,
    val lodgingName: String,
    val lodgingLocation: String
)
