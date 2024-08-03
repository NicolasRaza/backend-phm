package unsam.phm.grupo1.repository

import org.springframework.data.repository.CrudRepository
import unsam.phm.grupo1.entity.Lodging
import unsam.phm.grupo1.entity.Search
import java.util.Optional

interface SearchRepository: CrudRepository<Search, String> {
}