package de.lathspell.test.jpa.d1

import com.querydsl.jpa.impl.JPAQuery
import jakarta.persistence.EntityManager
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
    @Autowired private val infoRepo: D1InfoRepo,
    @Autowired private val em: EntityManager
) {
    private val log = LoggerFactory.getLogger(D1CascadeTest::class.java)

    @BeforeEach
    fun beforeEach() {
        infoRepo.deleteAll()
        taskRepo.deleteAll()
    }

    @Test
    fun `flyway 123`() {
        // create parent with children
        val task1 = D1Task(name = "First task").withInfo(
            listOf(
                D1Info(info = "First info"),
                D1Info(info = "Second info")
            )
        )
        taskRepo.save(task1)
        taskRepo.flush()

        // Query using QueryDSL
        val d1task = QD1Task.d1Task
        val query = JPAQuery<D1Task>(em)

        val resuult =
            query
                .select(d1task)
                .from(d1task)
                .where(
                    d1task.name.eq("First task")
                )
                .fetchFirst()
        assertThat(resuult?.uuid).isEqualTo(task1.uuid)
    }

}
