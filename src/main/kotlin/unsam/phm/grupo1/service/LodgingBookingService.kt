package unsam.phm.grupo1.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import unsam.phm.grupo1.dto.BookingDTO
import unsam.phm.grupo1.dto.toDTO
import unsam.phm.grupo1.dto.toDTOMongo
import unsam.phm.grupo1.dto.toEntity
import unsam.phm.grupo1.entity.*
import unsam.phm.grupo1.repository.BookingRepository
import unsam.phm.grupo1.exception.InvalidRangeOfDateException
import unsam.phm.grupo1.mongo.LodgingRepository
import unsam.phm.grupo1.neo4j.NeoLodgingRepository
import unsam.phm.grupo1.neo4j.NeoUserRepository
import java.util.*

@Service
class LodgingBookingService {
    @Autowired
    lateinit var lodgingBookingRepository: BookingRepository

    @Autowired
    lateinit var lodgingService: LodgingService

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var lodgingRepository: LodgingRepository


    fun getByUserId(user: Optional<User>): Iterable<BookingDTO> {
        return this.lodgingBookingRepository.findByUserId(user.get().id!!).map { it.toDTO() }
    }

    fun createBooking(booking: BookingDTO): BookingDTO { //BookingDTO
        if( booking.fromDate == booking.toDate) throw InvalidRangeOfDateException("Invalid date range")
        val user = this.userService.getById(booking.userId).orElseThrow{NoSuchElementException("User not found")}
        val lodging = this.lodgingService.getLodgingById(booking.lodgingId)
        if (!lodging.isActive()) throw NoSuchElementException("Lodging is not active")
        if(booking.totalCost > user.balance) throw NoSuchElementException("User does not have enough balance")

//        // Validar si hay reservas existentes que se solapen con la nueva reserva
        val existingBookings = lodgingService.searchBookingsInLodging(lodging.id!!,booking.fromDate,booking.toDate)
        if (!existingBookings.iterator().hasNext()) {
            throw InvalidRangeOfDateException("Date range already booked")
        }

        // Save and Flush Booking in postgres
        val bookingEntity = this.lodgingBookingRepository.saveAndFlush(booking.toEntity(user, lodging))
        val newBalance = user.balance - booking.totalCost
        userService.updateBalance(user.id!!, newBalance)

        // Format and save Booking in mongo
        lodging.bookings.add(bookingEntity.toDTOMongo())
        lodgingRepository.save(lodging)

        // Save Lodging with booking relation in neo4j
        saveInNeo4j(lodging, bookingEntity)
        return bookingEntity.toDTO()
    }

    fun getOne(id: Long): BookingDTO {
        val booking = lodgingBookingRepository.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "No existe una reserva con el id $id") }
        return booking.toDTO()
    }

    fun saveInNeo4j(lodging: Lodging, booking: Booking) {
        this.lodgingService.saveInNeo4j(lodging, booking)
    }
}