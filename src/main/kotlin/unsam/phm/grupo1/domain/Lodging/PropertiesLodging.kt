package unsam.phm.grupo1.domain.Lodging

import java.time.LocalDate

data class PropertiesLodging(
    var description: String? = null,
    var aspects: String? = null,
    var capacity: Int = 1,
    var numOfRooms: Int = 0,
    var numOfBathrooms: Int = 0,
    var detailLodging: String? = null,
    var houseKepping: Boolean = false,
    var fromDate: LocalDate? = null,
    var toDate: LocalDate? = null,
) {

}
