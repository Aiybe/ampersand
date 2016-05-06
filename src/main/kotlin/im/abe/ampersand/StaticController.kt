package im.abe.ampersand

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
open class StaticController {
    @RequestMapping("/")
    fun index(): String {
        return "index"
    }
}
