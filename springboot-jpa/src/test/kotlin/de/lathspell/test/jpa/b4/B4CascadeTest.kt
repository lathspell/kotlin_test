package de.lathspell.test.jpa.b4

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class B4CascadeTest(
    @Autowired private val infoCaseRepo: B4InfoCaseRepo,
    @Autowired private val infoRequestRepo: B4InfoRequestRepo,
) {
    private val log = LoggerFactory.getLogger(B4CascadeTest::class.java)

    @BeforeEach
    fun beforeEach() {
        infoCaseRepo.deleteAll()
        infoRequestRepo.deleteAll()
    }

    @Test
    fun `do collection entries automatically appear in the parent or is reload necessary`() {
        // create parent
        val case1 = B4InfoCase(customerId = "c123")
        infoCaseRepo.save(case1)

        // create child
        val req1 = B4InfoRequest(case = case1, comment = "foo")
        infoRequestRepo.save(req1)

        // check if child appears automatically
        assertThat(case1.requests).hasSize(0) // no

        // refresh
        val case1b = infoCaseRepo.findById(case1.id).get()
        assertThat(case1b.requests).hasSize(1) // now it's there
    }

    @Test
    fun `deleting parent cascades to children`() {
        // create objects
        val case1 = B4InfoCase(customerId = "c123")
        infoCaseRepo.save(case1)

        val req1 = B4InfoRequest(case = case1, comment = "foo")
        infoRequestRepo.save(req1)
        val req2 = B4InfoRequest(case = case1, comment = "bar")
        infoRequestRepo.save(req2)

        // refresh
        val case1b = infoCaseRepo.findById(case1.id).get()
        assertThat(case1b.requests).hasSize(2)

        // delete parent
        infoCaseRepo.delete(case1)

        // child should be gone none
        assertThat(infoCaseRepo.findAll()).isEmpty()
        assertThat(infoRequestRepo.findAll()).isEmpty()
    }

    @Test
    fun `deleting children does not cascades to parent`() {
        // create objects
        val case1 = B4InfoCase(customerId = "c123")
        infoCaseRepo.save(case1)

        val req1 = B4InfoRequest(case = case1, comment = "foo")
        infoRequestRepo.save(req1)
        val req2 = B4InfoRequest(case = case1, comment = "bar")
        infoRequestRepo.save(req2)

        // refresh
        val case1b = infoCaseRepo.findById(case1.id).get()
        assertThat(case1b.requests).hasSize(2)

        // delete children
        infoRequestRepo.delete(req1)

        // does parent know about the now missing child?
        assertThat(case1b.requests.size).isEqualTo(2) // No! CAVEAT: Attempting to access them gives StackOverflow!

        // child should be gone none
        assertThat(infoCaseRepo.findAll()).hasSize(1)
        assertThat(infoRequestRepo.findAll()).hasSize(1)
    }

}
