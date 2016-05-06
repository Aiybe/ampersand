package im.abe.ampersand

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class AmpersandApplication {
}

fun main(args: Array<String>) {
    SpringApplication.run(AmpersandApplication::class.java, *args)
}
