package unsam.phm.grupo1

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories

@EnableJpaRepositories(basePackages = ["unsam.phm.grupo1.repository"])
@EnableNeo4jRepositories(basePackages = ["unsam.phm.grupo1.neo4j"])
@EnableMongoRepositories(basePackages = ["unsam.phm.grupo1.mongo"])
@SpringBootApplication(
    exclude = [SecurityAutoConfiguration::class]
)
class airphmApplication


fun main(args: Array<String>) {
    runApplication<airphmApplication>(*args)
}