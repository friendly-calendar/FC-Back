package com.friendly.calendar.service

import com.friendly.calendar.entity.enum.Status.*
import com.friendly.calendar.network.event.EventDto
import com.friendly.calendar.network.user.UserSignUpReq
import com.friendly.calendar.repository.EventRepository
import com.friendly.calendar.repository.UserRepository
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import org.hibernate.Hibernate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Propagation.*
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@SpringBootTest
@Transactional
class EventServiceTest (
    @Autowired
    val eventService: EventService,

    @Autowired
    val eventRepository: EventRepository,

    @Autowired
    val userService: UserService,

    @Autowired
    val userRepository: UserRepository
) : AnnotationSpec() {

    private val user1Email1 : String = "c65901262@gamil.com"
    private val user1Email2 : String = "c65901263@gamil.com"

    @BeforeEach
    fun setUp() {
        val user1 = UserSignUpReq(
            nickName = "qw0916e11",
            email = user1Email1,
            id = "sada771380",
            password = "cs3061435669sd",
            phoneNumber = "010-1615-5544"
        )
        val user2 = UserSignUpReq(
            nickName = "qw0916e12",
            email = user1Email2,
            id = "sada771360",
            password = "cs611435669sd",
            phoneNumber = "010-1515-5544"
        );

        userService.signUp(user1)
        userService.signUp(user2)
    }

    @Test
    fun createEventTest() {
        val findUser1 = userRepository.findByEmail(user1Email1).get()
        val findUser2 = userRepository.findByEmail(user1Email2).get()
        val startDate: LocalDateTime = LocalDateTime.now().minusDays(1)
        val endDate: LocalDateTime = LocalDateTime.now()

        val eventDto = EventDto(
            title = "titleTest",
            description = "descriptionTest",
            startDate = startDate,
            endDate = endDate,
            location = "locationTest",
            status = ACCEPTED,
            invitedMembers = listOf(findUser1, findUser2)
        )
        val createEvent = eventService.createEvent(eventDto)
        val findEvent = eventRepository.findEventWithDetails(createEvent.id)

        createEvent.title shouldBe findEvent?.title
        createEvent.eventDate?.startDate shouldBe findEvent?.eventDate?.startDate
        createEvent.eventLocation?.location shouldBe findEvent?.eventLocation?.location
        createEvent.members.size shouldBe findEvent?.members?.size
    }
}
