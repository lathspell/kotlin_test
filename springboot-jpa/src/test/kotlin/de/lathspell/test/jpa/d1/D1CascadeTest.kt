package de.lathspell.test.jpa.d1

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class D1CascadeTest(
    @Autowired private val taskRepo: D1TaskRepo,
    @Autowired private val infoRepo: D1InfoRepo
) {
    private val log = LoggerFactory.getLogger(D1CascadeTest::class.java)

    @BeforeEach
    fun beforeEach() {
        infoRepo.deleteAll()
        taskRepo.deleteAll()
    }

    @Test
    fun `delete child objects - wrong`() {
        // create parent with children
        val task1 = D1Task(name = "First task").withInfo(
            listOf(
                D1Info(info = "First info"),
                D1Info(info = "Second info")
            )
        )
        taskRepo.save(task1)

        // trying to delete one child
        val info1 = task1.infos.first { it.info == "First info" }
        infoRepo.delete(info1)

        // verify
        assertThat(task1.infos.map { it.info }).containsExactlyInAnyOrder("First info", "Second info") // nothing deleted!
        assertThat(infoRepo.findAll().map { it.info }).containsExactlyInAnyOrder("First info", "Second info") // nothing deleted!
    }

    @Test
    fun `delete child objects`() {
        // create parent with children
        val task1 = D1Task(name = "First task").withInfo(
            listOf(
                D1Info(info = "First info"),
                D1Info(info = "Second info")
            )
        )
        taskRepo.save(task1)

        // trying to delete one child
        val info1 = task1.infos.first { it.info == "First info" }
        task1.infos.remove(info1)
        taskRepo.save(task1)

        // verify
        assertThat(task1.infos.map { it.info }).containsExactlyInAnyOrder("Second info") // first element deleted!
        assertThat(infoRepo.findAll().map { it.info }).containsExactlyInAnyOrder("Second info") // first element deleted!
    }
}
