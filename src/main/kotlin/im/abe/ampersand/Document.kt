package im.abe.ampersand

import org.ocpsoft.prettytime.PrettyTime
import org.springframework.data.repository.CrudRepository
import java.time.Instant
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class Document(var name: String = "",
                    var url: String = "",
                    var accountID: String = "",
                    var time: Instant = Instant.MIN,
                    @ManyToOne
                    var assignment: Assignment? = null,
                    @Id @GeneratedValue
                    var id: Long = 0) {

    fun humanTime() = PrettyTime().format(Date.from(time))

}

interface DocumentRepository : CrudRepository<Document, Long> {
    fun findByAccountIDAndAssignment(accountID: String, assignment: Assignment): Document?
}
