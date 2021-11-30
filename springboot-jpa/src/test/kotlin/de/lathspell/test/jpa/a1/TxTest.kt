package de.lathspell.test.jpa.a1

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class TxTest(
    @Autowired private val svc: PersonService,
    @Autowired private val personRepo: PersonRepo,
    @Autowired private val groupRepo: GroupRepo
) {

    private val log = LoggerFactory.getLogger(TxTest::class.java)

    @Test
    fun show() {
        log.info("===== before =====")
        svc.addPersonAndGroup(Person(name = "Tim", gid = 3))
        log.info("===== after =====")

        assertThat(personRepo.findAll().map { it.name }).containsExactly("Tim")
        assertThat(groupRepo.findAll().map { it.name }).containsExactly("G3")
    }

}
/*
2021-02-09 23:58:53.352  INFO 15432 --- [    Test worker] de.lathspell.test.TxTest                 : ===== before =====
2021-02-09 23:58:53.352 DEBUG 15432 --- [    Test worker] o.s.orm.jpa.JpaTransactionManager        : Creating new transaction with name [de.lathspell.test.jpa.a1.PersonService.addPersonAndGroup]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
2021-02-09 23:58:53.352 DEBUG 15432 --- [    Test worker] o.s.orm.jpa.JpaTransactionManager        : Opened new EntityManager [SessionImpl(760474367<open>)] for JPA transaction
2021-02-09 23:58:53.352 DEBUG 15432 --- [    Test worker] o.s.orm.jpa.JpaTransactionManager        : Exposing JPA transaction as JDBC [org.springframework.orm.jpa.vendor.HibernateJpaDialect$HibernateConnectionHandle@3ef86707]

2021-02-09 23:58:53.367  INFO 15432 --- [    Test worker] de.lathspell.test.jpa.a1.PersonService  : # saving person Person(id=c0874068-1fe4-4fdd-95d3-bb5b232e034a, gid=3, name=Tim)
2021-02-09 23:58:53.383 DEBUG 15432 --- [    Test worker] o.s.orm.jpa.JpaTransactionManager        : Found thread-bound EntityManager [SessionImpl(760474367<open>)] for JPA transaction
2021-02-09 23:58:53.383 DEBUG 15432 --- [    Test worker] o.s.orm.jpa.JpaTransactionManager        : Participating in existing transaction
2021-02-09 23:58:53.414 DEBUG 15432 --- [    Test worker] org.hibernate.SQL                        : select person0_.id as id1_1_0_, person0_.gid as gid2_1_0_, person0_.name as name3_1_0_ from persons person0_ where person0_.id=?

2021-02-09 23:58:53.445  INFO 15432 --- [    Test worker] de.lathspell.test.jpa.a1.PersonService  : # reloading all persons
2021-02-09 23:58:53.445 DEBUG 15432 --- [    Test worker] o.s.orm.jpa.JpaTransactionManager        : Found thread-bound EntityManager [SessionImpl(760474367<open>)] for JPA transaction
2021-02-09 23:58:53.445 DEBUG 15432 --- [    Test worker] o.s.orm.jpa.JpaTransactionManager        : Participating in existing transaction
2021-02-09 23:58:53.552 DEBUG 15432 --- [    Test worker] org.hibernate.SQL                        : insert into persons (gid, name, id) values (?, ?, ?)
2021-02-09 23:58:53.568 DEBUG 15432 --- [    Test worker] org.hibernate.SQL                        : select person0_.id as id1_1_, person0_.gid as gid2_1_, person0_.name as name3_1_ from persons person0_

2021-02-09 23:58:53.568  INFO 15432 --- [    Test worker] de.lathspell.test.jpa.a1.PersonService  : # deleting all groups
2021-02-09 23:58:53.568 DEBUG 15432 --- [    Test worker] o.s.orm.jpa.JpaTransactionManager        : Found thread-bound EntityManager [SessionImpl(760474367<open>)] for JPA transaction
2021-02-09 23:58:53.568 DEBUG 15432 --- [    Test worker] o.s.orm.jpa.JpaTransactionManager        : Participating in existing transaction
2021-02-09 23:58:53.568 DEBUG 15432 --- [    Test worker] org.hibernate.SQL                        : select group0_.id as id1_0_, group0_.name as name2_0_ from groups group0_

2021-02-09 23:58:53.568  INFO 15432 --- [    Test worker] de.lathspell.test.jpa.a1.PersonService  : # rewriting all groups
2021-02-09 23:58:53.568 DEBUG 15432 --- [    Test worker] o.s.orm.jpa.JpaTransactionManager        : Found thread-bound EntityManager [SessionImpl(760474367<open>)] for JPA transaction
2021-02-09 23:58:53.568 DEBUG 15432 --- [    Test worker] o.s.orm.jpa.JpaTransactionManager        : Participating in existing transaction
2021-02-09 23:58:53.568 DEBUG 15432 --- [    Test worker] org.hibernate.SQL                        : select group0_.id as id1_0_0_, group0_.name as name2_0_0_ from groups group0_ where group0_.id=?

2021-02-09 23:58:53.583 DEBUG 15432 --- [    Test worker] o.s.orm.jpa.JpaTransactionManager        : Initiating transaction commit
2021-02-09 23:58:53.583 DEBUG 15432 --- [    Test worker] o.s.orm.jpa.JpaTransactionManager        : Committing JPA transaction on EntityManager [SessionImpl(760474367<open>)]
2021-02-09 23:58:53.583 DEBUG 15432 --- [    Test worker] org.hibernate.SQL                        : insert into groups (name, id) values (?, ?)
2021-02-09 23:58:53.583 DEBUG 15432 --- [    Test worker] o.s.orm.jpa.JpaTransactionManager        : Closing JPA EntityManager [SessionImpl(760474367<open>)] after transaction
2021-02-09 23:58:53.583  INFO 15432 --- [    Test worker] de.lathspell.test.TxTest                 : ===== after =====
*/
