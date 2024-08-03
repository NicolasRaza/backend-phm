
package unsam.phm.grupo1.mongo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import unsam.phm.grupo1.domain.Lodging.Location
import unsam.phm.grupo1.entity.Lodging
import java.time.LocalDate
import java.util.*

@Repository
interface LodgingRepository :  PagingAndSortingRepository<Lodging, String>, MongoRepository<Lodging, String> {
    @Query("{ 'deleteDate': { \$exists: false }, 'userId': ?0 }")
    fun findByUserId(userId: Long): Iterable<Lodging>


    override fun findById(lodgingId: String): Optional<Lodging>

    @Query("{\n" +
            "    \$and: [\n" +
            "        { 'country': { \$regex: ?0, \$options: 'i' } },\n" +
            "        { 'capacity': { \$gte: ?3 } },\n" +
            "        { 'averageScore': { \$gte: ?4 } },\n" +
            "        { 'deleteDate': { \$exists: false } },\n" +
            "        { 'bookings': { \$not: { \$elemMatch: { 'fromDate': { \$lt: ?2 }, 'toDate': { \$gt: ?1 } } } } }\n" +
            "    ]\n" +
            "}")
    fun findByFilters(
        destination: String,
        fromdate: LocalDate,
        todate: LocalDate,
        capacity: Int,
        score: Int,
        pageable: Pageable
    ): Page<Lodging>


    @Query("{ '_id': ?0, 'bookings': { \$not: { \$elemMatch: { 'fromDate': { \$lt: ?2 }, 'toDate': { \$gt: ?1 } } } } }")
    fun findByBookings(lodgingid: String, fromDate: LocalDate, toDate: LocalDate): Iterable<Lodging>

    @Query(value = "{'country': {\$regex: ?0, \$options: 'i'}}", fields = "{'country': 1, '_id': 0}")
    fun findDistinctCountries(country : String): List<Location>
    @Query(value = "{}", delete = true)
    override fun deleteAll()

}
/*

@Query("{ \$and: [ { 'country': { \$regex: ?0, \$options: 'i' } }, { 'capacity': { \$gte: ?3 } } ] }")
    fun findByFilters(
        destination: String?,
        fromdate: String?,
        todate: String?,
        capacity: Int?,
        pageable: Pageable
    ): Page<Lodging>
    @Query("{\n" +
            "    \$or: [\n" +
            "        { 'country': { \$regex: ?0, \$options: 'i' } },\n" +
            "        { 'fromdate': ?1 },\n" +
            "        { 'todate': ?2 },\n" +
            "        { 'capacity': ?3 }\n" +
            "    ]\n" +
            "}")
 */

//@Repository
//interface LodgingRepository :  PagingAndSortingRepository<Lodging, Long>, CrudRepository<Lodging, Long> {
//
//
//    @Query("SELECT  l FROM lodging l WHERE (l.user = :user) and (l.deleteDate > NOW() or l.deleteDate is null)")
//    fun findByUserId(user: Optional<User>): Iterable<Lodging>
//
//
//    @Modifying
//    @Query("UPDATE lodging SET delete_date = NOW() WHERE id = :id", nativeQuery = true)
//    fun deactivateLodging(@Param("id") id: Long): Int
//
//   @Query(
//       """
//       SELECT DISTINCT country FROM lodging
//   """
//   )
//   fun searchAllCountry(): Iterable<String>
//
//   @Query(
//       """
//       SELECT DISTINCT country FROM lodging WHERE country ILIKE CONCAT(:country, '%')
//
//   """
//   )
//    fun searchCountry(@Param("country") country:String) : Iterable<String>
//
//
//    @Query("SELECT l FROM lodging l WHERE " +
//            "(:destination is null or l.country = :destination) and " +
//            "(:capacity is null or l.capacity >= :capacity) and " +
//            "(:minScore is null or l.averageScore >= :minScore)" +
//            "and (l.deleteDate > NOW() or l.deleteDate is null)" )
//    fun findLodgingsByFiltersPage(
//        @Param("destination") destination: String?,
//        @Param("capacity") capacity: Int?,
//        @Param("minScore") minScore: Float?,
//        pageable: Pageable
//    ): Page<Lodging>
//}
