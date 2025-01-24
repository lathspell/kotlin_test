package de.lathspell.test.jpa.tx

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW
import org.springframework.transaction.UnexpectedRollbackException
import org.springframework.transaction.support.DefaultTransactionDefinition
import org.springframework.transaction.support.TransactionTemplate

/**
 * Nested transactions in Spring.
 *
 * If an Exception occurs in a transaction, that transaction is marked as "burned" and will get an internal status flag that
 * only permits a Rollback. If the default behaviour of commit is triggered, i.e. by just ending the transaction, the following
 * exception gets thrown: "Transaction rolled back because it has been marked as rollback-only"
 *
 * When a method is annotated with `@Transactional` it gets the default of `propagation = PROPAGATION_REQUIRED`.
 * If it calls another @Transactional annotated method, that joins the original transaction. Even if we explicitly
 * create a new TransactionTemplate object, it uses the same transaction as a TransactionTemplate is just a "wrapper"
 * object, like a JdbcTemplate is just a wrapper around an SQL connection and not an SQL connection of its own.
 * So using "TransactionTemplate" behaves exactly the same as @Transactional.
 *
 * To continue with an outer transaction, no matter what happens to the inner transaction, we need to explicitly specify
 * `propagation = PROPAGATION_REQUIRES_NEW` to the inner transaction.
 *
 * Beware that there is a difference between using a new and using a nested transaction:
 * When using a new one, the old one gets "suspended" i.e. both are complete independent. It can happen that the inner one
 * succeeds and gets persisted and the outer will be rolled back as an error occurs after the inner has finished.
 * When using a nested transaction it might even succeed and call commit - if  an errors occurs afterward in the outer
 * transaction, that transaction and all its inner ones get rolled back as a whole.
 *
 * Beware also that opening new transactions carelessly may quickly lead to deadlocks:
 * When reading a list of database entries in the outer method and then passing them to an inner method for update, that inner
 * method cannot be in a different transaction as it the outer still locks the row of the entry it has passed to the inner method.
 */
@SpringBootTest(webEnvironment = NONE)
@Suppress("LoggingSimilarMessage")
class TransactionTest(@Autowired private val ptxm: PlatformTransactionManager) {

    private val log = LoggerFactory.getLogger(TransactionTest::class.java)

    /**
     * This test uses only a single transaction.
     * It will fail if @Transactional is used on the test class/method level as we then would have an outer transaction
     * (or rather the same, if both will be joined), leading to the error seen in the next test.
     */
    @Test
    fun `just one tx is no problem`() {
        log.info("Starting test")

        // Outer transaction, like for example the @Scheduled method
        val tt = TransactionTemplate(ptxm)
        tt.execute {
            log.info("Entered tx")

            // We tried to catch an Exception
            try {
                error("kaboom")
            } catch (e: Exception) {
                log.info("Caught: ${e.message}")
            }

            log.info("Leaving tx")
        }
        log.info("Leaving test")
    }

    @Test
    fun `show that the initial fix did not work`() {
        assertThrows<UnexpectedRollbackException>("Transaction rolled back because it has been marked as rollback-only") {
            log.info("Starting test")

            // Outer transaction, like for example the @Scheduled method
            val outerTx = TransactionTemplate(ptxm)
            outerTx.execute {
                log.info("Entered outer tx")

                // Manually created inner exception sadly does not prevent the outer one from getting the rollback flag as well
                try {
                    val innerTx = TransactionTemplate(ptxm)
                    innerTx.execute<Any> {
                        log.info("Entered inner tx")
                        error("kaboom")
                    }
                } catch (e: Exception) {
                    log.info("Caught exception: ${e.message}")
                }

                log.info("Leaving outer tx")
            }
            log.info("Leaving test")
        }
    }


    @Test
    fun `possible fix`() {
        log.info("Starting test")

        // Outer transaction, like for example the @Scheduled method
        val outerTx = TransactionTemplate(ptxm)
        outerTx.execute {
            log.info("Entered outer tx")

            // Manually created inner transaction that is separate from the outer one
            try {
                val innerTx = TransactionTemplate(ptxm, DefaultTransactionDefinition(PROPAGATION_REQUIRES_NEW))
                innerTx.execute<Any> {
                    log.info("Entered inner tx")
                    error("kaboom")
                }
            } catch (e: Exception) {
                log.info("Caught exception: ${e.message}")
            }

            log.info("Leaving outer tx")
        }
        log.info("Leaving test")
    }


    @Test
    fun `show problem with Spring annotations`(@Autowired component: TransactionTestComponent) {
        assertThrows<UnexpectedRollbackException>("Transaction rolled back because it has been marked as rollback-only") {
            log.info("Entered test")
            TransactionTemplate(ptxm).execute {
                log.info("Entered outer tx")
                try {
                    component.doStuff()
                } catch (e: Exception) {
                    log.info("Caught exception: ${e.message}")
                }
                log.info("Leaving outer tx")
            }
            log.info("Leaving test")
        }
    }

    @Test
    fun `possible fix with Spring annotations`(@Autowired component: TransactionTestComponent) {
        log.info("Entered test")
        TransactionTemplate(ptxm).execute {
            log.info("Entered outer tx")
            try {
                component.doStuffInANewTx()
            } catch (e: Exception) {
                log.info("Caught exception: ${e.message}")
            }
            log.info("Leaving outer tx")
        }
        log.info("Leaving test")
    }

        @Test
    fun `another possible fix with Spring annotations`(@Autowired component: TransactionTestComponent) {
        log.info("Entered test")
        TransactionTemplate(ptxm).execute {
            log.info("Entered outer tx")
            try {
                component.doStuffInANestedTx()
            } catch (e: Exception) {
                log.info("Caught exception: ${e.message}")
            }
            log.info("Leaving outer tx")
        }
        log.info("Leaving test")
    }
}


