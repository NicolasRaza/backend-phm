package unsam.phm.grupo1.dto

import unsam.phm.grupo1.domain.Lodging.Location
//import unsam.phm.grupo1.domain.Lodging.Lodging
import unsam.phm.grupo1.domain.Lodging.PropertiesLodging

data class DetailDTO(
    var id: Int,
    var name: String,
    var averageScore: Float,
    var image: String?,
    var address: String,
    var country: String,
    var description: String?,
    var detailLodging: String?,
    var aspects: String?,
    var baseCost: Float,
    var totalCost: Float,
    var capacity: Int,
    var houseKepping: Boolean,
    var numOfRooms: Int,
    var numOfBathrooms: Int,
)
/*
fun Lodging.toDetailDTO() = DetailDTO(/*
    id = this.id,
    name = this.name,
    averageScore = this.averageScore,
    image = this.image,
    address = "${this.location.address}",
    country = "${this.location.country}",
    description = this.propertiesLodging.description,
    detailLodging = this.propertiesLodging.detailLodging,
    aspects = this.propertiesLodging.aspects,
    baseCost = this.baseCost,
    totalCost = this.totalCost(),
    capacity = this.propertiesLodging.capacity,
    houseKepping = this.propertiesLodging.houseKepping,
    numOfRooms = this.propertiesLodging.numOfRooms,
    numOfBathrooms = this.propertiesLodging.numOfBathrooms,*/
)
*/