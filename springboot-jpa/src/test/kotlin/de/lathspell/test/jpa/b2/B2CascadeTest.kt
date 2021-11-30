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
    @Autowired private val infoRequestRepo: B2InfoRequestRepo,
    @Autowired private val b2Service: B2Service
) {
    private val log = LoggerFactory.getLogger(B2CascadeTest::class.java)

    @Test
    fun `strange behaviour`() {

        b2Service.prepare()

        b2Service.delete()

        // child should be gone none - but isn't due to the JPA cache which does not know about the SQL "on cascade"
        assertThat(infoCaseRepo.findAll()).isEmpty()
        assertThat(infoRequestRepo.findAll()).isEmpty()
    }
}
