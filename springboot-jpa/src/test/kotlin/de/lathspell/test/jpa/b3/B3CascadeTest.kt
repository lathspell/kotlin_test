package de.lathspell.test.jpa.b3

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class B3CascadeTest(
    @Autowired private val infoCaseRepo: B3InfoCaseRepo,
    @Autowired private val infoRequestRepo: B3InfoRequestRepo,
) {
    private val log = LoggerFactory.getLogger(B3CascadeTest::class.java)

    @BeforeEach
    fun beforeEach() {
        infoCaseRepo.deleteAll()
        infoRequestRepo.deleteAll()
    }

    @Test
    fun `works now`() {
        // create objects
        val case1 = B3InfoCase(customerId = "c123")
        infoCaseRepo.save(case1)

        val req1 = B3InfoRequest(case = case1, comment = "foo")
        infoRequestRepo.save(req1)
        val req2 = B3InfoRequest(case = case1, comment = "bar")
        infoRequestRepo.save(req2)

        // delete parent
        infoCaseRepo.delete(case1)

        // child should be gone none - but isn't due to the JPA cache which does not know about the SQL "on cascade"
        assertThat(infoCaseRepo.findAll()).isEmpty()
        assertThat(infoRequestRepo.findAll()).isEmpty()
    }
}
