package com.friendly.calendar.service

import com.friendly.calendar.domain.model.enum.EventInvitationStatus.*
import com.friendly.calendar.network.EventDto
import com.friendly.calendar.network.UserSignUpReq
import com.friendly.calendar.domain.persistence.EventRepository
import com.friendly.calendar.domain.persistence.UserRepository
import com.friendly.calendar.domain.service.EventService
import com.friendly.calendar.domain.service.UserService
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@SpringBootTest
@Transactional
class EventServiceTest(
    val eventService: EventService,
    val eventRepository: EventRepository,
    val userService: UserService,
    val userRepository: UserRepository
) : AnnotationSpec() {

    var userId1: Long = 0
    var userId2: Long = 0

    @BeforeEach
    fun setUp() {
        val user1 = UserSignUpReq(
            nickName = "qw0916e11",
            email = "c65901262@gamil.com",
            username = "sada771380",
            password = "cs3061435669sd",
            phoneNumber = "010-1615-5544"
        )
        val user2 = UserSignUpReq(
            nickName = "qw0916e12",
            email = "c65901263@gamil.com",
            username = "sada771360",
            password = "cs611435669sd",
            phoneNumber = "010-1515-5544"
        )

        userId1 = userService.signUp(user1)
        userId2 = userService.signUp(user2)
    }

    @Test
    fun createEventTest() {
        val findUser1 = userRepository.findById(userId1).get()
        val findUser2 = userRepository.findById(userId2).get()
        val startDate: LocalDateTime = LocalDateTime.now().minusDays(1)
        val endDate: LocalDateTime = LocalDateTime.now()

        val eventDto = EventDto(
            title = "titleTest",
            description = "descriptionTest",
            startDate = startDate,
            endDate = endDate,
            location = "locationTest",
            eventInvitationStatus = ACCEPTED,
            invitedMembersId = listOf(findUser1.username, findUser2.username)
        )
        val createEvent = eventService.createEvent(eventDto)
        val findEvent = eventRepository.findEventWithDetails(createEvent.id)

        createEvent.title shouldBe findEvent?.title
        createEvent.eventDate?.startDate shouldBe findEvent?.eventDate?.startDate
        createEvent.eventLocation?.location shouldBe findEvent?.eventLocation?.location
        createEvent.members.size shouldBe findEvent?.members?.size
    }
}
