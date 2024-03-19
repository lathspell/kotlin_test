package de.lathspell.test.jpa.d1

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

/**
 * How to correctly delete a child item.
 *
 * Quoting https://docs.jboss.org/hibernate/orm/6.4/introduction/html_single/Hibernate_Introduction.html#many-to-one
 *
 * > Changes made to the unowned side of an association are never synchronized to the database.
 * > If we desire to change an association in the database, we must change it from the owning side.
 *
 */
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
        taskRepo.flush()

        // verify save
        assertThat(task1.infos.map { it.info }).containsExactlyInAnyOrder("First info", "Second info")
        assertThat(infoRepo.findAll().map { it.info }).containsExactlyInAnyOrder("First info", "Second info")

        // trying to delete one child
        val info1 = task1.infos.first { it.info == "First info" }
        infoRepo.delete(info1) // does not actually delete anything
        infoRepo.deleteAllById(listOf(info1.uuid)) // neither does this
        infoRepo.flush() // does not help either, there is no actual DELETE statement sent to the database

        // verify deletion
        assertThat(task1.infos.map { it.info }).containsExactlyInAnyOrder("First info", "Second info") // nothing deleted!
        assertThat(infoRepo.findAll().map { it.info }).containsExactlyInAnyOrder("First info", "Second info") // nothing deleted!
        // If task.info is annotated with FetchType.LAZY, `infoRepo.findAll()` does not find "First info". `task.infos` still contains it though!
    }

    @Test
    fun `delete child objects - correctly`() {
        // create parent with children
        val task1 = D1Task(name = "First task").withInfo(
            listOf(
                D1Info(info = "First info"),
                D1Info(info = "Second info")
            )
        )
        taskRepo.save(task1)

        // delete one child
        val info1 = task1.infos.first { it.info == "First info" }
        task1.infos.remove(info1)
        taskRepo.save(task1)

        // verify
        assertThat(task1.infos.map { it.info }).containsExactlyInAnyOrder("Second info") // first element deleted!
        assertThat(infoRepo.findAll().map { it.info }).containsExactlyInAnyOrder("Second info") // first element deleted!
    }
}
