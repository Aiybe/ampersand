package im.abe.ampersand

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import java.time.Instant
import java.util.*
import javax.servlet.http.HttpSession

@Controller
open class WritingController @Autowired constructor(val documents: DocumentRepository,
                                                    val assignments: AssignmentRepository) {
    @RequestMapping("/a/new", method = arrayOf(RequestMethod.GET))
    fun newAssignment() = "newAssignment"

    @RequestMapping("/a/new", method = arrayOf(RequestMethod.POST))
    fun newAssignment(writing: NewWriting, session: HttpSession): String {
        val assignment = createAssignment(writing.name, writing.description, writing.id)
        session.setAttribute("canSeeResponses" + assignment.slug, true)
        return "redirect:/a/" + assignment.slug
    }

    @RequestMapping("/a/{slug}", method = arrayOf(RequestMethod.GET))
    fun assignment(@PathVariable slug: String,
                   session: HttpSession, model: Model): String {
        val assignment = assignments.findBySlug(slug)
        if (assignment != null) {
            model.addAttribute("assignment", assignment)
            model.addAttribute("canSeeResponses", session.getAttribute("canSeeResponses" + slug) as? Boolean ?: false)
            return "assignment"
        } else {
            return "redirect:/a/new"
        }
    }

    @RequestMapping("/a/{slug}", method = arrayOf(RequestMethod.POST))
    fun postAssignment(@PathVariable slug: String, response: NewResponse,
                       session: HttpSession): String {
        val assignment = assignments.findBySlug(slug)
        if (assignment != null) {
            documents.save(Document(response.name, response.url, response.id, Instant.now(), assignment))
            session.setAttribute("canSeeResponses" + slug, true)
            return "redirect:/a/" + slug
        } else {
            return "redirect:/a/new"
        }
    }

    @RequestMapping("/a/{slug}/verify", method = arrayOf(RequestMethod.POST))
    @ResponseBody
    fun verify(@PathVariable slug: String, verification: Verification,
               session: HttpSession): Boolean {
        val assignment = assignments.findBySlug(slug)
        if (assignment != null && (assignment.accountID == verification.id ||
                documents.findByAccountIDAndAssignment(verification.id, assignment) != null)) {
            session.setAttribute("canSeeResponses" + slug, true)
            return true
        } else {
            return false
        }
    }

    private tailrec fun createAssignment(name: String, description: String, accountID: String,
                                         length: Int = 1, random: Random = Random()): Assignment {
        val slugBuilder = StringBuilder()

        while (slugBuilder.length < length) {
            slugBuilder.append(possibleCharacters[random.nextInt(possibleCharacters.size)])
        }

        val slug = slugBuilder.toString()
        if (slug == "new" || assignments.findBySlug(slug) != null) {
            return createAssignment(name, description, accountID, length + 1)
        } else {
            return assignments.save(Assignment(name, description, slug, accountID))
        }
    }

    private val possibleCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()
}
