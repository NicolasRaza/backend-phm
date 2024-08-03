package unsam.phm.grupo1.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
//import unsam.phm.grupo1.domain.Lodging.Lodging
import unsam.phm.grupo1.dto.*
import unsam.phm.grupo1.entity.Lodging
import unsam.phm.grupo1.entity.Search
import unsam.phm.grupo1.service.LodgingService
import unsam.phm.grupo1.service.SearchService
import java.time.LocalDate
import java.util.*
import javax.naming.ServiceUnavailableException

@RestController
@RequestMapping("api/lodging")
@CrossOrigin("*")
class LodgingController() {
    @Autowired
    lateinit var lodgingService: LodgingService

    @Autowired
    lateinit var searchService: SearchService

    @GetMapping("/{id}")
    fun getLodgingById(@PathVariable id: String): Lodging {
        try {
            return lodgingService.getLodgingById(id)
        } catch (e: NotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Loading Not Found", e)
        }

    }

    @GetMapping("/user/{id}")
    fun getLodgingByCreatorId(@PathVariable id: Long): Iterable<Lodging> {
        try {
            return lodgingService.getManyByCreator(id)
        } catch (e: NotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Loading Not Found", e)
        }

    }

    @DeleteMapping("/deactivate/{id}")
    fun deactivateLodgings(@PathVariable id: String): ResponseEntity<Any> {
        try {


            lodgingService.deactivateLodging(id)
            return ResponseEntity.ok(Message("Lodging deactivated successfully"))
        } catch (e: NotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Loading Not Found", e)
        }
    }

    @PostMapping
    fun getInitLodgings(
        @RequestParam("page") pageNumber: Int?,
        @RequestParam("size") pageSize: Int?,
        @RequestParam("minScore", required = false) minScore: Int?,
        @RequestBody request: FilterRequestLodingDTO
    ): Page<LodgingDTO> {
        try {
//            val searchCache = this.getCacheSearch(request)

            val fullRequest = LodgingPageRequestDTO(pageNumber, pageSize, minScore, request)
            val searchCache = this.getCacheSearch(fullRequest)

            return this.getFilteredLodgingPages(searchCache, fullRequest)
        } catch (e: NotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Loading Not Found", e)
        }
    }

    private fun getCacheSearch(request: LodgingPageRequestDTO): Search? {
        val searchCacheRes: Optional<Search> = this.searchService.getSearchByFilter(request)
        var searchCache: Search? = null
        if(searchCacheRes.isPresent){
            searchCache = searchCacheRes.get()
        }
        return searchCache
    }

    private fun getFilteredLodgingPages(
        searchCache: Search?,
        req: LodgingPageRequestDTO
    ): Page<LodgingDTO> {
        val lodgings: Page<LodgingDTO>
        if(searchCache == null) {
            lodgings = //if ((req.filters.destination == null && req.filters.fromdate == null && req.minScore == null && req.filters.capacity == null) && req.filters.userId != null ) {
                //this.lodgingService.getLodgingNeo(req.filters.userId, req.pageNumber, req.pageSize)
            //} else {
                this.lodgingService.getInitLodgings(req.filters, req.pageNumber, req.pageSize, req.minScore)
            //}
            val result = lodgings.content.map {
                it.toEntity(LodgingMap[it.type]!!)
            }
            this.searchService.createSearch(data = req, result = Search(result = result))
        } else lodgings = PageImpl(if(searchCache.result != null) searchCache.result!!.map{ it?.toDTO() } else listOf())

        return lodgings
    }

    @PostMapping("/recommendations")
    private fun getLodgingRecommendations(
        @RequestParam("page") pageNumber: Int?,
        @RequestParam("size") pageSize: Int?,
        @RequestBody req:  FilterRequestLodingDTO
    ): Page<LodgingDTO> {

        return this.lodgingService.getLodgingNeo(req.userId!!, pageNumber, pageSize)
    }

//    @PostMapping("/prueba")
//    fun prueba(
//        @RequestBody request: FilterRequestLodingDTO
//    ): ResponseEntity<Message> {
//          this.searchService.createSearch(data = request, result = Search(listOf<Lodging>(
//              LodgingBuilder(House())
//                  .withMandatoryData(
//                      name = "Prueba",
//                      description = "Prueba de descripcion",
//                      country = "Argentina",
//                  )
//                  .build()
//          )))
//
//        return ResponseEntity.ok(Message(
//            message = "ok"
//        ))
//    }
//
//    @PostMapping("/getprueba")
//    fun getprueba(
//        @RequestBody request: FilterRequestLodingDTO
//    ): Any {
//        // TODO
//        try {
//            return this.searchService.getSearchByFilter(request)
//        } catch (err: Exception) {
//            return ResponseEntity.badRequest()
//        }
//    }


    //Post lodging
    @PostMapping("/")
    fun postLodging(@RequestBody lodging: LodgingDTO): LodgingDTO {
        try {
            return lodgingService.postLodging(lodging).toDTO()
        } catch (e: ServiceUnavailableException) {
            throw ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Service Unavailable", e)
        }
    }

    @RequestMapping(
        value = ["/search/country/", "/search/country/{country}"],
        method = [RequestMethod.GET]
    )
    fun searchLodgingsCountrys(@PathVariable(required = false) country: String?): Iterable<String?> {
        return lodgingService.searchLodgingsCountrys(country)
    }

    @PostMapping("/{id}/contador")
    fun incrementarContador(@PathVariable id: String, @RequestBody req: RequestClick): ResponseEntity<Any> {
        try {
            val lodging = lodgingService.getLodgingById(id)
            lodging.clickInfo.add(ClickData(req.userId,LocalDate.now()))
            lodgingService.save(lodging)
            return ResponseEntity.ok(Message("Click registrado con éxito"))
        } catch (e: NotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Alojamiento no encontrado", e)
        }
    }



}


