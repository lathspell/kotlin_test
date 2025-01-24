package de.lathspell.test.jpa.tx

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation.NESTED
import org.springframework.transaction.annotation.Propagation.REQUIRES_NEW
import org.springframework.transaction.annotation.Transactional

@Component
class TransactionTestComponent {

    private val log = LoggerFactory.getLogger(TransactionTestComponent::class.java)

    @Transactional
    fun doStuff() {
        log.info("Entering doStuff")
        error("kaboom")
    }

    @Transactional(propagation = REQUIRES_NEW)
    fun doStuffInANewTx() {
        log.info("Entering doStuffCorrectly")
        error("kaboom")
    }

    @Transactional(propagation = NESTED)
    fun doStuffInANestedTx() {
        log.info("Entering doStuffNested")
        error("kaboom")
    }
}
