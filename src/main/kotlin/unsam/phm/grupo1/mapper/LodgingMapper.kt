package unsam.phm.grupo1.mapper

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import unsam.phm.grupo1.domain.Lodging.*

class LodgingMapper {
    /*fun mapLodgingJSONToLodging(lodging: String): /*Lodging*/ {
        val objectMapper = ObjectMapper().apply {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            configure(SerializationFeature.INDENT_OUTPUT, true)
            registerModule(JavaTimeModule())
        }
        val lodgingEntity = objectMapper.readValue(lodging, LodgingMap::class.java)
        return when (lodgingEntity.type) {
            "House" -> objectMapper.readValue(lodging, House::class.java)
            "Building" -> objectMapper.readValue(lodging, Building::class.java)
            "Hut" -> objectMapper.readValue(lodging, Hut::class.java)
            else -> throw IllegalArgumentException("Tipo de alojamiento desconocido: ${lodgingEntity.type}")
        }
    }*/
}


class LodgingMap(
    var id: Int = 0,
    var name: String = "",
    var averageScore: Float = 0f,
    var image: String = "",
    var location: Location = Location(),
    var propertiesLodging: PropertiesLodging = PropertiesLodging(),
    var baseCost: Float = 0f,
    var type: String = "",
    var creatorId: Int = 0
) {
    constructor() : this(0, "", 0f, "", Location(), PropertiesLodging(), 0f, "", 0)

    fun additionalCost(): Float {
        return 0.0F
    }
}
