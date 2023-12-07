package com.friendly.calendar

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class CalendarApplication

fun main(args: Array<String>) {
    runApplication<CalendarApplication>(*args)
}
