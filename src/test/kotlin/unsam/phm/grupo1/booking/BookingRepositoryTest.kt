//package unsam.phm.grupo1.booking
//
//import io.kotest.core.annotation.DisplayName
//import org.assertj.core.api.Assertions.assertThat
//import org.junit.jupiter.api.AfterEach
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.test.context.ActiveProfiles
//import unsam.phm.grupo1.builder.BookingBuilder
//import unsam.phm.grupo1.builder.LodgingBuilder
//import unsam.phm.grupo1.builder.UserBuilder
//import unsam.phm.grupo1.dto.BookingDTO
//import unsam.phm.grupo1.dto.LodgingDTO
//import unsam.phm.grupo1.dto.toEntity
//import unsam.phm.grupo1.entity.*
//import unsam.phm.grupo1.repository.BookingRepository
//import unsam.phm.grupo1.repository.LodgingRepository
//import unsam.phm.grupo1.repository.UserRepository
//import unsam.phm.grupo1.service.LodgingService
//import java.time.LocalDate
//
//@ActiveProfiles("test")
//@DisplayName("Booking Repository Test")
//@SpringBootTest
//class BookingRepositoryTest {
//    @Autowired
//    lateinit var bookingRepository: BookingRepository
//
//    @Autowired
//    lateinit var userRepository: UserRepository
//
//    @Autowired
//    lateinit var lodgingRepository: LodgingRepository
//
//    lateinit var booking_1: Booking
//    lateinit var booking_2: Booking
//    lateinit var booking_3: Booking
//    lateinit var booking_4: Booking
//
//    lateinit var user: User
//
//    val mapOfLodging: Map<String, Lodging> = mapOf(
//        "House" to House(),
//        "Building" to Building(),
//        "Hut" to Hut()
//    )
//
//    @BeforeEach
//    fun setup(){
//        user = UserBuilder(User(), userRepository)
//            .withEmail("pedro51@gmail").save()
//
//        booking_1 = BookingBuilder(Booking(), bookingRepository)
//            .withFromDate(LocalDate.of(2021, 1, 1))
//            .withToDate(LocalDate.of(2021, 1, 10))
//            .withTotalCost(1000f)
//            .withNumberOfGuests(2)
//            .withUser(user)
//            .save()
//        booking_2 = BookingBuilder(Booking(), bookingRepository)
//            .withFromDate(LocalDate.of(2021, 1, 1))
//            .withToDate(LocalDate.of(2021, 1, 10))
//            .withTotalCost(1000f)
//            .withNumberOfGuests(2)
//            .withUser(user)
//            .save()
//        booking_3 = BookingBuilder(Booking(), bookingRepository)
//            .withFromDate(LocalDate.of(2021, 1, 1))
//            .withToDate(LocalDate.of(2021, 1, 10))
//            .withTotalCost(1000f)
//            .withNumberOfGuests(2)
//            .save()
//        booking_4 = BookingBuilder(Booking(), bookingRepository)
//            .withFromDate(LocalDate.of(2021, 1, 1))
//            .withToDate(LocalDate.of(2021, 1, 10))
//            .withTotalCost(1000f)
//            .withNumberOfGuests(2)
//            .save()
//    }
//
//    @AfterEach
//    fun tearDown(){
//        bookingRepository.deleteAll()
//        userRepository.deleteAll()
//        lodgingRepository.deleteAll()
//    }
//
//    @Test
//    fun `get all bookings by user`(){
//        //act
//        val bookings = bookingRepository.findByUserId(user.id!!)
//
//        //assert
//        assertThat(bookings).hasSize(2)
//    }
//
//    @Test
//    fun `get booking by id`(){
//        //act
//        val booking = bookingRepository.findById(booking_1.id!!)
//
//        //assert
//        assertThat(booking).isNotNull
//        assertThat(booking.get().id).isEqualTo(booking_1.id)
//    }
//
//    @Test
//    fun `save new booking`(){
//        val lodging = LodgingBuilder(House(), lodgingRepository)
//            .withMandatoryData(
//                name = "Casa de campo",
//                description = "Casa de campo en el medio de la nada",
//                country = "Argentina",
//                address = "Tutan Kamon 123",
//                baseCost = 1000.toFloat(),
//                capacity = 4,
//                rooms = 2,
//                baths = 1,
//                detailLodging = "Casa de campo en el medio de la nada",
//                aspects = "Casa de campo en el medio de la nada",
//                owner = user
//            )
//            .withImage("https://i.ytimg.com/vi/B4xRxRAwbh0/hqdefault.jpg")
//            .withHouseKeeping(true)
//            .save()
//
//        val bookingDTO = BookingDTO(
//            id = null,
//            userId = user.id!!,
//            lodgingId = lodging.id!!,
//            fromDate = LocalDate.parse("2021-10-10"),
//            toDate = LocalDate.parse("2021-10-20"),
//            totalCost = 1000f,
//            numberOfGuests = 2
//        )
//
//        val bookingEnt = bookingDTO.toEntity(user, lodging)
//
//        //act
//        val booking = bookingRepository.save(bookingEnt)
//
//        //assert
//        assertThat(booking).isNotNull
//        assertThat(booking.id).isNotNull
//    }
//
//}