package unsam.phm.grupo1.dto

//import unsam.phm.grupo1.domain.Lodging.Lodging
import unsam.phm.grupo1.entity.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.reflect.KClass

data class LodgingDTO(
    var id: String?,
    var name: String,
    var averageScore: Float,
    var image: String,
    var country: String,
    var address: String,
    var baseCost: Float,
    var creatorId: Long,
    var type:String?,
    var totalCost: Float,
    var description: String = "",
    var aspects : String = "",
    var capacity: Int,
    var numOfRooms:Int = 0,
    var numOfBathrooms: Int = 0,
    var detailLodging: String = "",
    var houseKeeping: Boolean = false,
    var fromDate: LocalDate?,
    var deleteDate: LocalDate?
)


fun Lodging.toDTO() = LodgingDTO(
    id = this.id,
    name = this.name,
    averageScore = this.averageScore,
    image = this.image,
    country = this.country,
    address = this.address,
    baseCost = this.baseCost,
    creatorId = this.userId!!,
    type = this.l_type,
    totalCost = this.totalCost,
    description = this.description,
    aspects = this.aspects,
    capacity = this.capacity,
    numOfRooms = this.numOfRooms,
    numOfBathrooms = this.numOfBathrooms,
    detailLodging = this.detailLodging,
    houseKeeping = this.houseKeeping,
    fromDate = this.fromDate,
    deleteDate = this.deleteDate
)

fun LodgingNeo.toDTO() = LodgingDTO(
    id = this.id,
    name = this.name,
    averageScore = this.averageScore,
    image = this.image,
    country = this.country,
    address = this.address,
    baseCost = this.baseCost,
    creatorId = this.userId!!,
    type = this.l_type,
    totalCost = this.totalCost,
    capacity = this.capacity,
    fromDate = this.fromDate,
    deleteDate = this.deleteDate
)

fun LodgingDTO.toEntity(type: KClass<out Lodging>) = type.java.getDeclaredConstructor().newInstance().also {
    it.id = this.id!!
    it.name = this.name
    it.averageScore = this.averageScore
    it.image = this.image
    it.country = this.country
    it.address = this.address
    it.baseCost = this.baseCost
    it.totalCost = this.totalCost
    it.description = this.description
    it.userId = this.creatorId
    it.aspects = this.aspects
    it.capacity = this.capacity
    it.numOfRooms = this.numOfRooms
    it.numOfBathrooms = this.numOfBathrooms
    it.detailLodging = this.detailLodging
    it.houseKeeping = this.houseKeeping
    it.fromDate = LocalDate.now()
    it.deleteDate = null
}



data class FilterRequestLodingDTO(
    val destination: String?,
    val fromdate: String?,
    val todate: String?,
    val capacity: Int?,
    val userId : Long?,
)

data class LodgingPageRequestDTO(
    val pageNumber: Int?,
    val pageSize: Int?,
    val minScore: Int?,
    val filters: FilterRequestLodingDTO
)

fun formatDate(date: String): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
    return LocalDate.parse(date, formatter)
}

data class RequestClick(val userId:Long?)


data class ClickData(var userId: Long?, var timeClick: LocalDate){

}

val LodgingMap: Map<String, KClass<out Lodging>> = mapOf(
    "House" to House::class,
    "Building" to Building::class,
    "Hut" to Hut::class
)