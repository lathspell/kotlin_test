package de.lathspell.test.jpa.c1

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class C1CascadeTest(
    @Autowired private val infoCaseRepo: C1InfoCaseRepo,
) {
    private val log = LoggerFactory.getLogger(C1CascadeTest::class.java)

    @BeforeEach
    fun beforeEach() {
        infoCaseRepo.deleteAll()
    }

    @Test
    fun `do collection entries automatically appear in the parent or is reload necessary`() {
        // create parent
        val case1 = C1InfoCase(customerId = "c123", request = C1InfoRequest(comment = "foo"))
        infoCaseRepo.save(case1)

        // refresh
        val case1b = infoCaseRepo.findById(case1.id).get()
        assertThat(case1b.request?.comment).isEqualTo("foo")
    }

    @Test
    fun `deleting parent cascades to children`() {
        // create objects
        val case1 = C1InfoCase(customerId = "c123", request = C1InfoRequest(comment = "foo"))
        infoCaseRepo.save(case1)

        // delete parent
        infoCaseRepo.delete(case1)

        // child should be gone none
        assertThat(infoCaseRepo.findAll()).isEmpty()
    }

}
