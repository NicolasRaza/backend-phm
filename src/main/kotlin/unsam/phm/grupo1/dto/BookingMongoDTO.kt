package unsam.phm.grupo1.dto

import unsam.phm.grupo1.entity.Booking
import java.time.LocalDate

data class BookingMongoDTO(
    var fromDate: LocalDate,
    var toDate: LocalDate
)


fun Booking.toDTOMongo() = BookingMongoDTO(
    fromDate = this.fromDate,
    toDate = this.toDate
)