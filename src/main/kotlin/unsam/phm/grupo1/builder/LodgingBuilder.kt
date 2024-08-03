package unsam.phm.grupo1.builder

import unsam.phm.grupo1.entity.Lodging
import unsam.phm.grupo1.entity.User
import unsam.phm.grupo1.mongo.LodgingRepository
import java.time.LocalDate

class LodgingBuilder(val lodging: Lodging, val lodgingRepository: LodgingRepository? = null) {

    fun withMandatoryData(name: String, description: String, country: String, address: String = "Pirulin 123", baseCost: Float = 5300.toFloat(),
                          capacity: Int = 4, rooms: Int = 2, baths: Int = 1, detailLodging: String = "Descripcion", aspects: String = "Aspectos", owner: User = User())
    : LodgingBuilder {
        lodging.name = name
        lodging.description = description
        lodging.address = address
        lodging.country = country
        lodging.baseCost = baseCost
        lodging.capacity = capacity
        lodging.numOfRooms = rooms
        lodging.numOfBathrooms = baths
        lodging.detailLodging = detailLodging
        lodging.aspects = aspects
        lodging.userId = owner.id
        return this
    }

    fun withImage(image: String): LodgingBuilder {
        lodging.image = image
        return this
    }

    fun withHouseKeeping(houseKeeping: Boolean): LodgingBuilder {
        lodging.houseKeeping = houseKeeping
        return this
    }

    fun build(): Lodging {
        System.out.println("build " + lodging.name)
        lodging.fromDate = LocalDate.now()
        return lodging
    }

    fun save(): Lodging {
        System.out.println("LodgingBuilder.save()")
        return lodgingRepository!!.save(this.build())
    }



}