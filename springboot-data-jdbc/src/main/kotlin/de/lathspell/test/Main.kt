package de.lathspell.test

import de.lathspell.test.a.db.PersonRepo
import de.lathspell.test.b.db.AuthorsRepo
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories

@SpringBootApplication
@EnableJdbcRepositories(basePackageClasses = [PersonRepo::class, AuthorsRepo::class])
class Main
