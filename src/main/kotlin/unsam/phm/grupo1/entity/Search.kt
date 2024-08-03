package unsam.phm.grupo1.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash


@RedisHash("search", timeToLive = 60)
class Search(
    var result: List<Lodging?>?
) {
    @Id
    lateinit var search: String
}