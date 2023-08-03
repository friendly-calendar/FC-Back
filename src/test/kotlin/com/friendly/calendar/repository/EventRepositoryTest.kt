package com.friendly.calendar.repository

import com.friendly.calendar.entity.event.Event
import io.kotest.core.spec.style.AnnotationSpec
import org.junit.jupiter.api.Assertions.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import javax.transaction.Transactional

@SpringBootTest
@Transactional
class EventRepositoryTest (
    @Autowired
    val eventRepository: EventRepository,
) : AnnotationSpec() {

    @Test
    fun Test() {
        val event = Event(title = "test")
        val saveEvent: Event = eventRepository.save(event)

        println("saveEvent = $saveEvent")
        assertNotNull(saveEvent.createdDate)
        assertNotNull(saveEvent.createdBy)
    }

}
