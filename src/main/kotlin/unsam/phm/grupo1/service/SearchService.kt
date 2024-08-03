package unsam.phm.grupo1.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import unsam.phm.grupo1.dto.FilterRequestLodingDTO
import unsam.phm.grupo1.dto.LodgingPageRequestDTO
import unsam.phm.grupo1.entity.Lodging
import unsam.phm.grupo1.entity.Search
import unsam.phm.grupo1.repository.SearchRepository
import java.time.LocalDate
import java.util.Optional

@Service
class SearchService {
    @Autowired
    lateinit var searchRepository: SearchRepository

    fun getSearchByFilter(data: LodgingPageRequestDTO): Optional<Search> {
        val id = convertFilterDataToString(data)

        val search = searchRepository.findById(id)

        return search
    }

    fun createSearch(data: LodgingPageRequestDTO, result: Search) {
        val id = convertFilterDataToString(data)

        result.search = id
        if(result.result != null && result.result!!.isNotEmpty())
            searchRepository.save(result)
        else searchRepository.save(Search(
            result = null
        ).apply {
            this.search = id
        })
    }

    private fun convertFilterDataToString(data: LodgingPageRequestDTO): String {
        return (data.pageNumber ?: 0).toString()  + "," + (data.pageSize ?: 12).toString() + "," + (data.minScore ?: 0).toString() + "," +
                data.filters.destination + "," + data.filters.todate + "," + data.filters.fromdate  +
                "," + data.filters.capacity + "," + data.filters.userId
    }
}