package unsam.phm.grupo1.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import unsam.phm.grupo1.dto.*
import unsam.phm.grupo1.entity.*
import unsam.phm.grupo1.mongo.LodgingRepository
import unsam.phm.grupo1.neo4j.NeoLodgingRepository
import unsam.phm.grupo1.neo4j.NeoUserRepository
import java.time.LocalDate
import java.util.*


@Service
class LodgingService{

    @Autowired
    lateinit var lodgingRepository: LodgingRepository

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var neoLodgingRepository: NeoLodgingRepository

    @Autowired
    lateinit var neoUserRepository: NeoUserRepository


    fun postLodging(lodgingDTO: LodgingDTO): Lodging{
        this.userService.getById(lodgingDTO.creatorId).orElseThrow{NoSuchElementException("User not found")}
        val lodging = lodgingDTO.toEntity(LodgingMap[lodgingDTO.type]!!)
        lodging.totalCost= lodging.totalCost()
        return lodgingRepository.save(lodging)
    }
    fun getLodgingById(id: String): Lodging {
        return lodgingRepository.findById(id)
            .orElseThrow { NoSuchElementException("Lodging not found") }
    }

    fun getManyByCreator(creatorId:Long): Iterable<Lodging>{
        var user = userService.getById(creatorId)

        return lodgingRepository.findByUserId(creatorId)
    }


    fun getInitLodgings(
        request: FilterRequestLodingDTO,
        pageNumber: Int?,
        pageSize: Int?,
        minScore: Int?
    ): Page<LodgingDTO> {
        val pageRequest = PageRequest.of(pageNumber ?: 0, pageSize ?: 12)
        val destination = request.destination ?: "^"
        val capacity = request.capacity ?: 0
        val minScore = minScore ?: 0
        val fromdate = request.fromdate?.let { formatDate( request.fromdate) } ?: formatDate("1/1/2000")
        val todate = request.todate?.let { formatDate( request.todate) } ?: formatDate("1/1/2000")
        val lodgingsPage = lodgingRepository.findByFilters(destination,fromdate,todate,capacity, minScore,pageRequest)
        return PageImpl(lodgingsPage.content.map { lodging -> lodging.toDTO() }, pageRequest, lodgingsPage.totalElements)
    }
    fun getLodgingNeo(id: Long, pageNumber: Int?, pageSize: Int?): Page<LodgingDTO> {
            val pageRequest = PageRequest.of(pageNumber ?: 0, pageSize ?: 12)
            val skip = pageRequest.pageNumber.toLong() * pageRequest.pageSize.toLong()
            val limit = pageRequest.pageSize.toLong()
            val lodgingNeo = neoLodgingRepository.findbyFriends(id, skip, limit)
            println(pageRequest)
            println(skip)
            println(pageRequest)
            return PageImpl(lodgingNeo.map { lodging -> lodging.toDTO() }, pageRequest, lodgingNeo.size.toLong())

        }


    @Transactional
    fun deactivateLodging(id: String) {
        val lodging = lodgingRepository.findById(id)
            .orElseThrow { NoSuchElementException("Lodging not found") }
        lodging.deleteDate = LocalDate.now()
        lodgingRepository.save(lodging)
        neoLodgingRepository.desactivateLodging(id, LocalDate.now())
    }


    fun searchBookingsInLodging(lodgingid: String,fromdate: LocalDate, todate : LocalDate): Iterable<Lodging> {
        var bookigns = lodgingRepository.findByBookings(lodgingid,fromdate,todate)
        return bookigns
    }


   fun searchLodgingsCountrys(country: String?): List<String?> {

       val distinctCountries = country?.let { lodgingRepository.findDistinctCountries(it).distinct() } ?: lodgingRepository.findDistinctCountries("^").distinct()
       val formattedCountries = distinctCountries.map { it.country}
       return formattedCountries
   }


    fun updateLodging(lodging: Lodging): Lodging {
        val mongoLodging = lodgingRepository.save(lodging)
        val lodgingNode = LodgingNeo.fromLodging(lodging)
        neoLodgingRepository.save(lodgingNode)
        return mongoLodging
    }

    fun save(lodging: Lodging) {
        lodgingRepository.save(lodging)
    }

    fun saveInNeo4j(lodging: Lodging, booking: Booking) {
        try {
            //search userId in neo if not exist throw exception
            neoUserRepository.findById(booking.user!!.id!!).orElseThrow { NoSuchElementException("User not found in Neo4j") }

            val lodgingNode = LodgingNeo.fromLodging(lodging)
            val bookingNeo = BookingNeo.createFromBooking(booking)
            lodgingNode.bookings.add(bookingNeo)
            neoLodgingRepository.save(lodgingNode)
        } catch (e: Exception) {
            println("Error al guardar en Neo4j ${e.message}")
        }
    }

}
