package unsam.phm.grupo1.dto

import unsam.phm.grupo1.entity.Lodging
import unsam.phm.grupo1.entity.User
import java.time.LocalDate
import unsam.phm.grupo1.entity.Booking as BookingEntity

data class BookingDTO(
    var id: Long?,
    var userId: Long,
    var lodgingId: String,
    var fromDate: LocalDate,
    var toDate: LocalDate,
    var totalCost: Float,
    var numberOfGuests: Int
) {
}

fun BookingDTO.toJson(): String {

    var json = """
        {
            "userId": ${this.userId},
            "lodgingId": "${this.lodgingId}",
            "fromDate": "${this.fromDate}",
            "toDate": "${this.toDate}",
            "totalCost": ${this.totalCost},
            "numberOfGuests": ${this.numberOfGuests}
        }
    """
    val trimed = json.trimIndent()

    return trimed
}

fun BookingEntity.toDTO() = BookingDTO(
    id = this.id,
    userId = this.user?.id!!,
    lodgingId = this.lodgingId!!,
    fromDate = this.fromDate,
    toDate = this.toDate,
    totalCost = this.totalCost,
    numberOfGuests = this.numberOfGuests
)

fun BookingDTO.toEntity(user: User, lodging: Lodging): BookingEntity = BookingEntity(
    id = this.id,
    user = user,
    lodgingId = lodging.id!!,
    fromDate = this.fromDate,
    toDate = this.toDate,
    totalCost = this.totalCost,
    numberOfGuests = this.numberOfGuests
)