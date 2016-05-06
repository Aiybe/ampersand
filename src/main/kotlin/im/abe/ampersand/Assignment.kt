package im.abe.ampersand

import org.springframework.data.repository.CrudRepository
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
data class Assignment(var name: String = "",
                      var description: String = "",
                      var slug: String = "",
                      var accountID: String = "",
                      @Id @GeneratedValue
                      var id: Long = 0) {

    @OneToMany(mappedBy = "assignment")
    var documents: MutableList<Document> = arrayListOf()

}

interface AssignmentRepository : CrudRepository<Assignment, Long> {
    fun findBySlug(slug: String): Assignment?
}
