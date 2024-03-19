package de.lathspell.test.jpa.d1

import jakarta.persistence.*
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.jvm.Transient

@Entity
@Table(name = "d1_tasks")
class D1Task(

    @Id
    var uuid: UUID = UUID.randomUUID(),

    var name: String,

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "task", orphanRemoval = true, cascade = [CascadeType.ALL])
    var infos: MutableSet<D1Info> = mutableSetOf()

) {

    @Transient
    private val log = LoggerFactory.getLogger(D1Task::class.java)

    /** This method adds children and sets this object as their parent reference. */
    fun withInfo(d1Infos: List<D1Info>): D1Task {
        d1Infos.forEach { it.task = this }

        val (updated, removed) = infos.partition { it.info in d1Infos.map { dbAi -> dbAi.info } }
        val added = d1Infos.filter { it.info !in infos.map { ai -> ai.info } }
        log.debug("Found removed: {}; updated {}; added {} attachments", removed.map { it.info }, updated.map { it.info }, added.map { it.info })

        infos.removeAll(removed.toSet())
        updated.forEach { u ->
            val current = infos.first { it.info == u.info }
            // change additional fields
        }
        infos.addAll(added)

        return this
    }
}
