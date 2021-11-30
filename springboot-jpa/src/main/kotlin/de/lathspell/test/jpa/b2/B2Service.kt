package de.lathspell.test.jpa.b2

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class B2Service(
    private val infoCaseRepo: B2InfoCaseRepo,
    private val infoRequestRepo: B2InfoRequestRepo
) {
    private val log = LoggerFactory.getLogger(B2Service::class.java)

    fun prepare() {
        // create objects
        val case1 = B2InfoCase(customerId = "c123")
        infoCaseRepo.save(case1)

        val req1 = B2InfoRequest(case = case1, comment = "foo")
        infoRequestRepo.save(req1)
        val req2 = B2InfoRequest(case = case1, comment = "bar")
        infoRequestRepo.save(req2)

        // children are only available after refreshing
        log.info("case1: $case1")

        val case1Reloaded = infoCaseRepo.findAll().first()
        log.info("case1: $case1Reloaded")

    }

    fun delete() {
        // delete parent
        val case1 = infoCaseRepo.findAll().first()
        infoCaseRepo.delete(case1)
    }
}
