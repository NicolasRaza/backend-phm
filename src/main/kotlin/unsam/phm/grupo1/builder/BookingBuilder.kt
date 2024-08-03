package unsam.phm.grupo1.builder

import unsam.phm.grupo1.entity.Booking
import unsam.phm.grupo1.entity.Lodging
import unsam.phm.grupo1.entity.User
import unsam.phm.grupo1.repository.BookingRepository
import java.time.LocalDate

class BookingBuilder(val booking: Booking, val bookingRepository: BookingRepository) {

    fun withLodging(lodgingId: String): BookingBuilder {
        booking.lodgingId = lodgingId
        return this
    }

    fun withUser(user: User): BookingBuilder {
        booking.user = user
        return this
    }

    fun withFromDate(fromDate: LocalDate): BookingBuilder {
        booking.fromDate = fromDate
        return this
    }

    fun withToDate(toDate: LocalDate): BookingBuilder {
        booking.toDate = toDate
        return this
    }

    fun withTotalCost(totalCost: Float): BookingBuilder {
        booking.totalCost = totalCost
        return this
    }


    fun withNumberOfGuests(numberOfGuests: Int): BookingBuilder {
        booking.numberOfGuests = numberOfGuests
        return this
    }

    fun build(): Booking {
        return booking
    }

    fun save(): Booking {
        return bookingRepository.save(this.build())
    }
}
