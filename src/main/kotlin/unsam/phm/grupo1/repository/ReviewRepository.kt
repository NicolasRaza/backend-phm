package unsam.phm.grupo1.repository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import unsam.phm.grupo1.entity.Review
import java.time.LocalDate
import java.util.*


@Repository
interface ReviewRepository : JpaRepository<Review, Long> {

    fun findByBookingId(bookingId: Long): Optional<Review>

    //from review join with booking and with lodgingid calculate average score
    @Query("""
        SELECT AVG(r.score) FROM review r JOIN booking b ON r.bookingId = b.id WHERE b.lodgingId = :lodgingId
        """)
    fun getAverageScore(@Param("lodgingId") lodgingId: String): Float?

    @Query("""
        SELECT AVG(r.score)
        FROM review r
        JOIN booking b ON r.bookingId = b.id
        WHERE EXISTS (
            SELECT 1
            FROM booking
            WHERE id = :bookingId AND lodgingId = b.lodgingId
        )
        """)
    fun getAverageScoreWithBookingId(bookingId: Long): Float?

    @Query("""
        SELECT r.id AS review_id, r.comment, r.score, r.publishedComment, u.userName, u.profileImageURL
        FROM review r
        JOIN booking b ON r.bookingId = b.id
        JOIN user_info u ON b.user.id = u.id
        WHERE b.lodgingId = :lodgingId
        ORDER BY r.id DESC
        LIMIT 3
    """)
    fun findTop3ByLodgingIdOrderByPublishedCommentDesc(@Param("lodgingId") lodgingId: String, top: Pageable = PageRequest.of(0, 3)): List<Array<Any>>


//    SELECT r.*, u.profile_imageurl as lodging_owner_photo_url, u.user_name as lodging_owner, b.user_id
//    FROM review as r
//    JOIN booking as b ON r.booking_id = b.id
//    JOIN user_info as u ON b.user_id = u.id
//    WHERE u.id = ?1;
//    @Query(
//        """
//            SELECT r.id, r.bookingId, r.comment, r.publishedComment, r.score, u.profileImageURL as lodging_owner_photo_url, u.userName as lodging_owner
//            FROM review r
//            JOIN booking b ON r.bookingId = b.id
//            JOIN user_info u ON b.user.id = u.id
//            WHERE u.id = :id
//        """
//    )
//    fun findByUserId(@Param("id") userId: Long): List<PruebaDTO>

    @Query(
        """
            SELECT new unsam.phm.grupo1.repository.ReviewByUserReponseDTO(r.id, r.bookingId, r.comment, r.publishedComment, r.score, b.lodgingId)
            FROM review r 
            JOIN booking b ON r.bookingId = b.id 
            JOIN user_info u ON b.user.id = u.id 
            WHERE u.id = :id
        """
    )
    fun findByUserId(@Param("id") userId: Long): List<ReviewByUserReponseDTO>
}


data class ReviewByUserReponseDTO(
    val id: Long,
    val bookingId: Long,
    val comment: String,
    val publishedComment: LocalDate,
    val score: Int,
    val lodgingId: String,
) {
    var lodgingOwnerPhotoUrl: String? = null
    var lodgingOwner: String? = null
    var lodgingName: String? = null
//    constructor(
//        id: Long,
//        bookingId: Long,
//        comment: String,
//        publishedComment: LocalDate,
//        score: Int,
//        lodgingId: String,
////        profileImageUrl: String?,
////        userName: String
//    ) : this(id, bookingId, comment, publishedComment, score, lodgingId) {
////        this.lodgingOwnerPhotoUrl = profileImageUrl
////        this.lodgingOwner = userName
//    }
}