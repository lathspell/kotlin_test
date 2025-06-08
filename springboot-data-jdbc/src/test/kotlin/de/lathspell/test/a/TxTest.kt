package de.lathspell.test.a

import de.lathspell.test.a.db.GroupRepo
import de.lathspell.test.a.db.PersonRepo
import de.lathspell.test.a.model.Person
import de.lathspell.test.a.service.PersonService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class TxTest(
    @Autowired private val svc: PersonService,
    @Autowired private val personRepo: PersonRepo,
    @Autowired private val groupRepo: GroupRepo
) {

    @Test
    fun show() {
        svc.addPersonAndGroup(Person(name = "Tim", gid = 3))

        assertThat(personRepo.findAll().map { it.name }).containsExactly("Tim")
        assertThat(groupRepo.findAll().map { it.name }).containsExactly("G3")

        assertThat(personRepo.findOneByName("Tim")!!.name).isEqualTo("Tim")

        svc.addPersonAndGroup(Person(name = "Tom", gid = 4))
        assertThat(personRepo.findOneByName("Tom")).isNotNull
        assertThat(groupRepo.findAll().map { it.name }).containsExactly("G3", "G4")
    }
}
