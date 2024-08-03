//package unsam.phm.grupo1.booking
//
//import io.kotest.core.annotation.DisplayName
//import jakarta.transaction.Transactional
//import org.hamcrest.CoreMatchers.containsString
//import org.hamcrest.Matchers
//import org.junit.jupiter.api.AfterEach
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.http.HttpMethod
//import org.springframework.http.MediaType
//import org.springframework.test.context.ActiveProfiles
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers
//import unsam.phm.grupo1.builder.BookingBuilder
//import unsam.phm.grupo1.builder.UserBuilder
//import unsam.phm.grupo1.dto.BookingDTO
//import unsam.phm.grupo1.dto.LodgingDTO
//import unsam.phm.grupo1.dto.toJson
//import unsam.phm.grupo1.entity.Booking
//import unsam.phm.grupo1.entity.Lodging
//import unsam.phm.grupo1.entity.User
//import unsam.phm.grupo1.repository.BookingRepository
//import unsam.phm.grupo1.repository.UserRepository
//import unsam.phm.grupo1.service.LodgingService
//import java.time.LocalDate
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@DisplayName("Booking Controller Test")
//@Transactional
//class BookingControllerTest {
//
//    @Autowired
//    lateinit var mockMvc: MockMvc
//
//    @Autowired
//    lateinit var bookingRepository: BookingRepository
//
//    @Autowired
//    lateinit var userRepository: UserRepository
//
//    @Autowired
//    lateinit var lodgingService: LodgingService
//
//    lateinit var pedro: User
//    lateinit var juan: User
//
//    lateinit var bookingBrasil: Booking
//    lateinit var bookingArgentina: Booking
//    lateinit var bookingRusia: Booking
//
//    lateinit var lodgingBrasilEnt: Lodging
//    lateinit var lodgingArgentinaEnt: Lodging
//    lateinit var lodgingRusiaEnt: Lodging
//    @BeforeEach
//    fun setup(){
//        pedro = UserBuilder(User(), userRepository)
//            .withBalance(1000000f)
//            .withEmail("pedro51@gmail").save()
//
//        juan = UserBuilder(User(), userRepository)
//            .withBalance(2000f)
//            .withEmail("juan@gmail.com").save()
//
//        val lodgingBrasil: LodgingDTO = LodgingDTO(
//            id = null,
//            name= "Ejemplo de alojamiento",
//            averageScore = 2f,
//            image = "https://cdn.hobbyconsolas.com/sites/navi.axelspringer.es/public/styles/hc_1440x810/public/media/image/2022/10/pokemon-espanita-2854987.jpg?itok=jMDdc_xe",
//            country = "Brasil",
//            address = "Calle  312",
//            baseCost = 100.0f,
//            creatorId = pedro.id!!,
//            type = "Hut",
//            totalCost = 0f,
//            description = "Este es un ejemplo de alojamiento.",
//            aspects = "Esta locuraaaaaaaaaaaa",
//            capacity = 2,
//            numOfRooms = 2,
//            numOfBathrooms = 1,
//            detailLodging = "No la trates de entender",
//            houseKeeping = true,
//            fromDate = LocalDate.parse("2021-10-10"),
//            deleteDate = LocalDate.parse("2021-10-20"),
//        )
//
//        val lodgingArgentina: LodgingDTO = LodgingDTO(
////            id = 2,
//            id = null,
//            name= "Ejemplo de alojamiento",
//            averageScore = 2f,
//            image = "https://cdn.hobbyconsolas.com/sites/navi.axelspringer.es/public/styles/hc_1440x810/public/media/image/2022/10/pokemon-espanita-2854987.jpg?itok=jMDdc_xe",
//            country = "Argentina",
//            address = "Calle  312",
//            baseCost = 100.0f,
//            creatorId = juan.id!!,
//            type = "Hut",
//            totalCost = 0f,
//            description = "Este es un ejemplo de alojamiento.",
//            aspects = "Esta locuraaaaaaaaaaaa",
//            capacity = 2,
//            numOfRooms = 2,
//            numOfBathrooms = 1,
//            detailLodging = "No la trates de entender",
//            houseKeeping = true,
//            fromDate = LocalDate.parse("2021-10-10"),
//            deleteDate = LocalDate.parse("2021-10-20"),
//        )
//
//        val lodgingRusia: LodgingDTO = LodgingDTO(
////            id = 3,
//            id = null,
//            name= "Ejemplo de alojamiento",
//            averageScore = 2f,
//            image = "https://cdn.hobbyconsolas.com/sites/navi.axelspringer.es/public/styles/hc_1440x810/public/media/image/2022/10/pokemon-espanita-2854987.jpg?itok=jMDdc_xe",
//            country = "Rusia",
//            address = "Calle  312",
//            baseCost = 100.0f,
//            creatorId = pedro.id!!,
//            type = "Hut",
//            totalCost = 0f,
//            description = "Este es un ejemplo de alojamiento.",
//            aspects = "Esta locuraaaaaaaaaaaa",
//            capacity = 2,
//            numOfRooms = 2,
//            numOfBathrooms = 1,
//            detailLodging = "No la trates de entender",
//            houseKeeping = true,
//            fromDate = LocalDate.parse("2021-10-10"),
//            deleteDate = LocalDate.parse("2021-10-20"),
//        )
//
//        lodgingBrasilEnt = lodgingService.postLodging(lodgingBrasil)
//        lodgingArgentinaEnt = lodgingService.postLodging(lodgingArgentina)
//        lodgingRusiaEnt = lodgingService.postLodging(lodgingRusia)
//
//        bookingBrasil = BookingBuilder(Booking(), bookingRepository)
//            .withLodging(lodgingBrasilEnt.id)
//            .withUser(pedro)
//            .withFromDate(LocalDate.parse("2021-10-10"))
//            .withToDate(LocalDate.parse("2021-10-20"))
//            .withTotalCost(1000f)
//            .withNumberOfGuests(2)
//            .save()
//
//        bookingArgentina = BookingBuilder(Booking(), bookingRepository)
//            .withLodging(lodgingArgentinaEnt.id)
//            .withUser(juan)
//            .withFromDate(LocalDate.parse("2021-10-10"))
//            .withToDate(LocalDate.parse("2021-10-20"))
//            .withTotalCost(1000f)
//            .withNumberOfGuests(2)
//            .save()
//
//        bookingRusia = BookingBuilder(Booking(), bookingRepository)
//            .withLodging(lodgingRusiaEnt.id)
//            .withUser(pedro)
//            .withFromDate(LocalDate.parse("2021-10-10"))
//            .withToDate(LocalDate.parse("2021-10-20"))
//            .withTotalCost(1000f)
//            .withNumberOfGuests(2)
//            .save()
//
//    }
//
//    @AfterEach
//    fun deleteAll() {
//        lodgingService.deleteAll()
//    }
//
//    @Test
//    fun `Obtener una reserva` () {
//        println(pedro.id)
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/booking/${bookingBrasil.id}"))
//            .andExpect(MockMvcResultMatchers.status().isOk)
//            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookingBrasil.id))
//            .andExpect(MockMvcResultMatchers.jsonPath("userId").value(pedro.id))
//            .andExpect(MockMvcResultMatchers.jsonPath("lodgingId").value(bookingBrasil.lodgingId))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.fromDate").value(bookingBrasil.fromDate.toString()))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.toDate").value(bookingBrasil.toDate.toString()))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.totalCost").value(bookingBrasil.totalCost))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfGuests").value(bookingBrasil.numberOfGuests))
//    }
//
//    @Test
//    fun `Obtener todas las reservas de un usuario`(){
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/booking/user/${pedro.id}"))
//            .andExpect(MockMvcResultMatchers.status().isOk)
//            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(bookingBrasil.id))
//            .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").value(pedro.id))
//            .andExpect(MockMvcResultMatchers.jsonPath("$[0].lodgingId").value(bookingBrasil.lodgingId))
//            .andExpect(MockMvcResultMatchers.jsonPath("$[0].fromDate").value(bookingBrasil.fromDate.toString()))
//            .andExpect(MockMvcResultMatchers.jsonPath("$[0].toDate").value(bookingBrasil.toDate.toString()))
//            .andExpect(MockMvcResultMatchers.jsonPath("$[0].totalCost").value(bookingBrasil.totalCost))
//            .andExpect(MockMvcResultMatchers.jsonPath("$[0].numberOfGuests").value(bookingBrasil.numberOfGuests))
//            .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(bookingRusia.id))
//            .andExpect(MockMvcResultMatchers.jsonPath("$[1].userId").value(pedro.id))
//            .andExpect(MockMvcResultMatchers.jsonPath("$[1].lodgingId").value(bookingRusia.lodgingId))
//            .andExpect(MockMvcResultMatchers.jsonPath("$[1].fromDate").value(bookingRusia.fromDate.toString()))
//            .andExpect(MockMvcResultMatchers.jsonPath("$[1].toDate").value(bookingRusia.toDate.toString()))
//            .andExpect(MockMvcResultMatchers.jsonPath("$[1].totalCost").value(bookingRusia.totalCost))
//            .andExpect(MockMvcResultMatchers.jsonPath("$[1].numberOfGuests").value(bookingRusia.numberOfGuests))
//            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray)
//            .andExpect(MockMvcResultMatchers.jsonPath("$.length()")
//                .value(2))
//    }
//
//    @Test
//    fun `Crear una reserva`(){
//        val bookingDTO = BookingDTO(
//            id = null,
//            userId = pedro.id!!,
//            lodgingId = lodgingBrasilEnt.id!!,
//            fromDate = LocalDate.parse("2021-11-10"),
//            toDate = LocalDate.parse("2021-11-20"),
//            totalCost = 1000f,
//            numberOfGuests = 2
//        )
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/booking")
//            .contentType(MediaType.APPLICATION_JSON).content(bookingDTO.toJson()))
//            .andExpect(MockMvcResultMatchers.status().isOk)
//            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(Matchers.notNullValue()))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(bookingDTO.userId))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.lodgingId").value(bookingDTO.lodgingId))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.fromDate").value(bookingDTO.fromDate.toString()))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.toDate").value(bookingDTO.toDate.toString()))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.totalCost").value(bookingDTO.totalCost))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfGuests").value(bookingDTO.numberOfGuests))
//    }
//
//    @Test
//    fun `catch InvalidRangeOfDateException`(){
//        val bookingDTO = BookingDTO(
//            id = null,
//            userId = pedro.id!!,
//            lodgingId = lodgingBrasilEnt.id!!,
//            fromDate = LocalDate.parse("2021-10-12"),
//            toDate = LocalDate.parse("2021-10-25"),
//            totalCost = 1000f,
//            numberOfGuests = 2
//        )
//
//        // Creamos la primer reserva
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/booking")
//            .contentType(MediaType.APPLICATION_JSON).content(bookingDTO.toJson()))
//            .andExpect(MockMvcResultMatchers.status().isOk)
//
//        // Intenamos crear la misma reserva con las mismas fechas
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/booking")
//            .contentType(MediaType.APPLICATION_JSON).content(bookingDTO.toJson()))
//            .andExpect(MockMvcResultMatchers.status().isBadRequest)
//            .andExpect(MockMvcResultMatchers.status().reason(containsString("Date range already booked")))
//    }
//
//    @Test
//    fun `catch No enough amount exception`(){
//        val bookingDTO = BookingDTO(
//            id = null,
//            userId = juan.id!!,
//            lodgingId = lodgingBrasilEnt.id!!,
//            fromDate = LocalDate.parse("2021-10-10"),
//            toDate = LocalDate.parse("2021-10-20"),
//            totalCost = 5000f,
//            numberOfGuests = 2
//        )
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/booking")
//            .contentType(MediaType.APPLICATION_JSON).content(bookingDTO.toJson()))
//            .andExpect(MockMvcResultMatchers.status().isBadRequest)
//            .andExpect(MockMvcResultMatchers.status().reason(containsString("User does not have enough balance")))
//
//        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/api/user/${juan.id}"))
//            .andExpect(MockMvcResultMatchers.status().isOk)
//            .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(juan.balance))
//
//    }
//
//
//}