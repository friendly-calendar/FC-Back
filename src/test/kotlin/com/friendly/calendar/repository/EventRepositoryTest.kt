package com.friendly.calendar.repository

import com.friendly.calendar.entity.event.Event
import io.kotest.core.spec.style.AnnotationSpec
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import javax.lang.model.element.Name
import javax.transaction.Transactional

@SpringBootTest
@Transactional
class EventRepositoryTest (
    @Autowired
    val eventRepository: EventRepository,
) : AnnotationSpec() {

    @Test
    fun Test() {
        val context = SecurityContextHolder.createEmptyContext()
        val auth = object : Authentication {
            override fun getName(): String {
                return ""
            }

            override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
                return mutableListOf()
            }

            override fun getCredentials(): Any {
                return ""
            }

            override fun getDetails(): Any {
                return ""
            }

            override fun getPrincipal(): Any {
                return "tttttttttttest"
            }

            override fun isAuthenticated(): Boolean {
                return true
            }

            override fun setAuthenticated(isAuthenticated: Boolean) {
            }
        }
        context.authentication = auth
        SecurityContextHolder.setContext(context)

        val event = Event(
            title = "test",
            description = "test",
            eventDate = null,
            eventLocation = null,
            members = emptyList()
            )
        val saveEvent: Event = eventRepository.save(event)

        assertNotNull(saveEvent.createdDate)
        assertEquals(saveEvent.createdBy, "tttttttttttest")
    }

}
