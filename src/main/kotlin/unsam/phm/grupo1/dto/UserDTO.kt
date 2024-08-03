package unsam.phm.grupo1.dto

//import unsam.phm.grupo1.domain.Lodging.Lodging
import unsam.phm.grupo1.entity.User
//import unsam.phm.grupo1.domain.User
import java.time.LocalDate

data class UserDTO(
    var id: Long = -1,
    var userName: String,
    val email: String,
    val nationality: String,
    val birthDate: LocalDate,
    val balance: Int,
    val friends: MutableSet<User> = mutableSetOf(),
    /*val purchasedLodgings:  MutableSet<Lodging> = mutableSetOf(),
    val myLodgings:  MutableSet<Lodging> = mutableSetOf(),*/
)


//fun User.toDTO()= UserDTO(
//    id = this.id,
//    userName = this.userName,
//    email = this.email,
//    nationality = this.nationality,
//    birthDate = this.birthDate,
//    balance = this.balance,
//    friends = this.friends,
//    purchasedLodgings = this.purchasedLodgings,
//    myLodgings = this.myLodgings,
//    profileImageURL = this.profileImageURL
//)

//fun UserDTO.toDomain() = User(
//    userName = this.userName,
//    email = this.email,
//    nationality = this.nationality,
//    birthDate = this.birthDate,
//    balance = this.balance
//    profileImageURL = this.profileImageURL
//).also {
//    it.id = this.id
//    it.friends = this.friends;
//    it.purchasedLodgings = this.purchasedLodgings;
//    it.myLodgings = this.myLodgings
//}


//data class UserRequestDTO(
//    var userName: String?,
//    var email: String?,
//    val nationality: String?,
//    val birthDate: LocalDate?,
//    val balance: Int?
//)


data class UserUpdateRequestDTO(
    var userName: String?,
    var profileImageURL: String?,
    var nationality: String?,
    var birthDay: LocalDate?,
    var balance: Int?
)