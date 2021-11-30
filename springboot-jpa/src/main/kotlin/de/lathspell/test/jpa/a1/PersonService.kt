package de.lathspell.test.jpa.a1

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation.SERIALIZABLE
import org.springframework.transaction.annotation.Propagation.REQUIRED
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(propagation = REQUIRED, isolation = SERIALIZABLE)
class PersonService(
    private val personRepo: PersonRepo,
    private val groupRepo: GroupRepo
) {

    private val log = LoggerFactory.getLogger(PersonService::class.java)

    fun addPersonAndGroup(p: Person) {
        log.info("# saving person $p")
        personRepo.save(p)

        val groups = calcNeededGroups()
        rewriteGroups(groups)

        log.info("# counting")
        log.info("Found groups: " + groupRepo.count())
    }

    fun calcNeededGroups(): Set<Group> {
        log.info("# reloading all persons")
        return personRepo.findAll().map { Group(name = "G" + it.gid) }.toSet()
    }

    fun rewriteGroups(groups: Set<Group>) {
        log.info("# deleting all groups")
        groupRepo.deleteAll()

        log.info("# rewriting all groups")
        groups.forEach { groupRepo.save(it) }
    }
}
