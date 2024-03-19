package de.lathspell.test.jpa.b2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class B2CascadeTest(
    @Autowired private val infoCaseRepo: B2InfoCaseRepo,
    @Autowired private val infoRequestRepo: B2InfoRequestRepo
) {
    private val log = LoggerFactory.getLogger(B2CascadeTest::class.java)

    @Test
    fun `strange behaviour`() {
        // prepare - create objects
        val case1 = B2InfoCase(customerId = "c123")
        infoCaseRepo.save(case1)
        val req1 = B2InfoRequest(case = case1, comment = "foo")
        infoRequestRepo.save(req1) // FIXME: don't do this, attach req1 to case1 and then save case1!
        val req2 = B2InfoRequest(case = case1, comment = "bar")
        infoRequestRepo.save(req2)

        // children are only available after refreshing
        assertThat(case1.requests.size).isEqualTo(0)
    }
}
