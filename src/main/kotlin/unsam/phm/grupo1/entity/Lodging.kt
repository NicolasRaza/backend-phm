package unsam.phm.grupo1.entity
import jakarta.persistence.*
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Property
import org.springframework.data.neo4j.core.schema.Relationship
import unsam.phm.grupo1.dto.ClickData
import org.springframework.data.annotation.Id as IdNeo4j
import unsam.phm.grupo1.dto.BookingMongoDTO
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Document(collection = "lodging")
open class Lodging(
    @Id
    open var id: String,
    open var name: String = "",
    open var averageScore: Float = 0.0F,
    open var image: String = "",
    open var country: String = "",
    open var address: String = "",
    open var baseCost: Float = 0.0f,
    open var totalCost: Float = 0.0f,
    open var description: String = "",
    open var aspects: String = "",
    open var capacity: Int = 1,
    open var numOfRooms: Int = 0,
    open var numOfBathrooms: Int = 0,
    open var detailLodging: String = "",
    open var houseKeeping: Boolean = false,
    open var fromDate: LocalDate? = null,
    open var deleteDate: LocalDate? = null,
    open var clickInfo: MutableList<ClickData> = mutableListOf(),
    open var bookings: MutableList<BookingMongoDTO> = mutableListOf()
    ){

    @Field
    open var userId: Long? = null

    @Field
    open var l_type:String =""
    open fun totalCost() = baseCost;

    fun isActive(): Boolean{
        return  deleteDate ==  null || deleteDate!!.isAfter(LocalDate.now())
    }

}

var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

@Node("LodgingNeo")
class LodgingNeo{
    @IdNeo4j @GeneratedValue
    var id: String? = null
    @Property
    var name: String = ""
    @Property
    var averageScore: Float = 0.0F
    @Property
    var image: String = ""
    @Property
    var country: String = ""
    @Property
    var address: String = ""
    @Property
    var baseCost: Float = 0.0f
    @Property
    var totalCost: Float = 0.0f
    @Property
    var capacity: Int = 1
    @Property
    var fromDate: LocalDate? = null
    @Property
    var deleteDate: LocalDate? = null

    @Relationship(type = "BOOKING", direction = Relationship.Direction.INCOMING)
    var bookings: MutableList<BookingNeo> = mutableListOf()

    @Property
    var userId: Long? = null

    @Property
    var l_type:String =""

    fun totalCost() = baseCost;

    companion object {
        fun fromLodging(lodging: Lodging): LodgingNeo {
            val lodgingNeo = LodgingNeo()
            lodgingNeo.id = lodging.id
            lodgingNeo.name = lodging.name
            lodgingNeo.averageScore = lodging.averageScore
            lodgingNeo.image = lodging.image
            lodgingNeo.country = lodging.country
            lodgingNeo.address = lodging.address
            lodgingNeo.baseCost = lodging.baseCost
            lodgingNeo.totalCost = lodging.totalCost
            lodgingNeo.capacity = lodging.capacity
            lodgingNeo.fromDate = lodging.fromDate
            lodgingNeo.deleteDate = lodging.deleteDate
            lodgingNeo.userId = lodging.userId
            lodgingNeo.l_type = lodging::class.simpleName!!
            return lodgingNeo
        }
    }

}

class Building(
    id: String = ObjectId().toString(),
    name: String = "",
    averageScore: Float = 0f,
    image: String = "",
    country: String = "",
    address: String = "",
    baseCost: Float = 0f,
    totalCost: Float = 0f,
    description: String = "",
    aspects: String = "",
    capacity: Int = 1,
    numOfRooms: Int = 0,
    numOfBathrooms: Int = 0,
    detailLodging: String = "",
    houseKeeping: Boolean = false,
    fromDate: LocalDate? = LocalDate.now(),
    deleteDate: LocalDate? = null,
    clickInfo: MutableList<ClickData> = mutableListOf(),
    bookings: MutableList<BookingMongoDTO> = mutableListOf()
): Lodging(
    id = id,
    name = name,
    averageScore = averageScore,
    image = image,
    country = country,
    address = address,
    baseCost = baseCost,
    totalCost = totalCost,
    description = description,
    aspects = aspects,
    capacity = capacity,
    numOfRooms = numOfRooms,
    numOfBathrooms = numOfBathrooms,
    detailLodging = detailLodging,
    houseKeeping = houseKeeping,
    fromDate = fromDate,
    deleteDate = deleteDate,
    clickInfo = clickInfo,
    bookings = bookings
) {

    init {
        l_type = "Building"
    }
    companion object {
        const val MIN_ROOMS = 3
        const val COST_PER_ROOM = 1000.0F
        const val COST_PER_MIN_ROOM = 2000.0F
    }

    override fun totalCost(): Float {
        return super.totalCost() + additionalCost()
    }

    private fun additionalCost(): Float {
        return if (numOfRooms < MIN_ROOMS) COST_PER_MIN_ROOM else numOfRooms * COST_PER_ROOM
    }
}


class House(
    id: String = ObjectId().toString(),
    name: String = "",
    averageScore: Float = 0f,
    image: String = "",
    country: String = "",
    address: String = "",
    baseCost: Float = 0f,
    totalCost: Float = 0f,
    description: String = "",
    aspects: String = "",
    capacity: Int = 1,
    numOfRooms: Int = 0,
    numOfBathrooms: Int = 0,
    detailLodging: String = "",
    houseKeeping: Boolean = false,
    fromDate: LocalDate? = LocalDate.now(),
    deleteDate: LocalDate? = null,
    clickInfo: MutableList<ClickData> = mutableListOf(),
    bookings: MutableList<BookingMongoDTO> = mutableListOf()
): Lodging(
    id = id,
    name = name,
    averageScore = averageScore,
    image = image,
    country = country,
    address = address,
    baseCost = baseCost,
    totalCost = totalCost,
    description = description,
    aspects = aspects,
    capacity = capacity,
    numOfRooms = numOfRooms,
    numOfBathrooms = numOfBathrooms,
    detailLodging = detailLodging,
    houseKeeping = houseKeeping,
    fromDate = fromDate,
    deleteDate = deleteDate,
    clickInfo = clickInfo,
    bookings = bookings
) {
    init {
        l_type = "House"
    }

    companion object {
        const val COST_PER_GUEST = 500.0F
    }

    override fun totalCost(): Float {
        return super.totalCost() + additionalCost()
    }

    private fun additionalCost(): Float {
        return capacity * COST_PER_GUEST
    }
}


class Hut(
    id: String = ObjectId().toString(),
    name: String = "",
    averageScore: Float = 0f,
    image: String = "",
    country: String = "",
    address: String = "",
    baseCost: Float = 0f,
    totalCost: Float = 0f,
    description: String = "",
    aspects: String = "",
    capacity: Int = 1,
    numOfRooms: Int = 0,
    numOfBathrooms: Int = 0,
    detailLodging: String = "",
    houseKeeping: Boolean = false,
    fromDate: LocalDate? = LocalDate.now(),
    deleteDate: LocalDate? = null,
    clickInfo: MutableList<ClickData> = mutableListOf(),
    bookings: MutableList<BookingMongoDTO> = mutableListOf()
): Lodging(
    id = id,
    name = name,
    averageScore = averageScore,
    image = image,
    country = country,
    address = address,
    baseCost = baseCost,
    totalCost = totalCost,
    description = description,
    aspects = aspects,
    capacity = capacity,
    numOfRooms = numOfRooms,
    numOfBathrooms = numOfBathrooms,
    detailLodging = detailLodging,
    houseKeeping = houseKeeping,
    fromDate = fromDate,
    deleteDate = deleteDate,
    clickInfo = clickInfo,
    bookings = bookings
) {
    init {
        l_type = "Hut"
    }
    companion object {
        const val COST_PER_HOUSEKEPPING = 10000.0F
    }

    override fun totalCost(): Float {
        return super.totalCost() + additionalCost()
    }
    private fun additionalCost(): Float {
        return if (houseKeeping) COST_PER_HOUSEKEPPING else 0.0F
    }
}
