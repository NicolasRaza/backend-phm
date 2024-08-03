package unsam.phm.grupo1.entity

import jakarta.persistence.*
import org.springframework.data.neo4j.core.schema.Property
import org.springframework.data.neo4j.core.schema.RelationshipProperties
import org.springframework.data.neo4j.core.schema.TargetNode
import java.time.LocalDate
import org.springframework.data.annotation.Id as Neo4jId
import org.springframework.data.neo4j.core.schema.GeneratedValue as Neo4jGeneratedValue

@Entity(name = "booking")
open class Booking(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open var id: Long? = null,

    @Column
    open var lodgingId: String? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    @JoinColumn(name = "user_id")
    open var user: User? = null,

    @Column
    open var fromDate: LocalDate = LocalDate.now(),

    @Column
    open var toDate: LocalDate = LocalDate.now(),

    @Column
    open var totalCost: Float = 0f,

    @Column
    open var numberOfGuests: Int = 0
){

}


@RelationshipProperties
class BookingNeo{
    @Neo4jId @Neo4jGeneratedValue
    var id: Long? = null

    @Property
    var bookingId: Long? = null

    @Property
    var lodgingId: String? = null

    @TargetNode
    var user: User? = null

    @Property
    var fromDate: LocalDate = LocalDate.now()
    @Property
    var toDate: LocalDate = LocalDate.now()

    @Property
    var totalCost: Float = 0f

    @Property
    var numberOfGuests: Int = 0

    companion object {
        fun createFromBooking(booking: Booking): BookingNeo {
            val bookingNeo = BookingNeo()
            bookingNeo.bookingId = booking.id
            bookingNeo.lodgingId = booking.lodgingId
            bookingNeo.user = booking.user
            bookingNeo.fromDate = booking.fromDate
            bookingNeo.toDate = booking.toDate
            bookingNeo.totalCost = booking.totalCost
            bookingNeo.numberOfGuests = booking.numberOfGuests
            return bookingNeo
        }
    }
}