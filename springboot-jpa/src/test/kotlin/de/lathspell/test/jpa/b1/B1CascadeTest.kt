package de.lathspell.test.jpa.b1

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE
import org.springframework.test.context.transaction.TestTransaction
import org.springframework.transaction.annotation.Transactional
import java.util.*

@SpringBootTest(webEnvironment = NONE)
@Transactional
class B1CascadeTest(
    @Autowired private val infoCaseRepo: B1InfoCaseRepo,
    @Autowired private val infoRequestRepo: B1InfoRequestRepo
) {

    @Test
    fun `strange behaviour`() {
        TestTransaction.flagForCommit() // else there is no SQL "delete" in the logs

        // create objects
        val case1 = B1InfoCase(id = UUID.randomUUID(), customerId = "c123")
        infoCaseRepo.save(case1)

        val req1 = B1InfoRequest(id = UUID.randomUUID(), caseId = case1.id, comment = "foo")
        infoRequestRepo.save(req1)

        assertThat(infoRequestRepo.findAll()).isNotEmpty

        // delete parent
        infoCaseRepo.delete(case1)

        // child should be gone none - but isn't due to the JPA cache which does not know about the SQL "on cascade"
        assertThat(infoRequestRepo.findAll()).containsExactly(req1)
    }
}
