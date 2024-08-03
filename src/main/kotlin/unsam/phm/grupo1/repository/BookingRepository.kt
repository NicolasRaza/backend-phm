package unsam.phm.grupo1.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import unsam.phm.grupo1.entity.Booking
import unsam.phm.grupo1.entity.Lodging
import java.time.LocalDate
import java.util.*

@Repository
interface BookingRepository : PagingAndSortingRepository<Booking, Long>, CrudRepository<Booking, Long>, JpaRepository<Booking, Long> {
    fun findByUserId(userId: Long): Iterable<Booking>

//    fun findByLodgingAndFromDateLessThanEqualAndToDateGreaterThanEqual(lodging: Lodging, toDate: LocalDate, fromDate: LocalDate): List<Booking>
    fun findByFromDateLessThanEqualAndToDateGreaterThanEqual(toDate: LocalDate, fromDate: LocalDate): List<Booking>
}