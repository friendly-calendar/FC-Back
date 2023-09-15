package com.friendly.calendar.service

import com.friendly.calendar.domain.model.baseEntity.DelFlag.*
import com.friendly.calendar.domain.model.enum.EventInvitationStatus
import com.friendly.calendar.network.EventCreateDto
import com.friendly.calendar.network.UserSignUpReq
import com.friendly.calendar.domain.persistence.EventRepository
import com.friendly.calendar.domain.persistence.UserRepository
import com.friendly.calendar.domain.service.EventService
import com.friendly.calendar.domain.service.UserService
import com.friendly.calendar.network.EventMemberDto
import com.friendly.calendar.network.EventUpdateDto
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@SpringBootTest
@Transactional
@ActiveProfiles("dev")
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

        val eventCreateDto = EventCreateDto(
            title = "titleTest",
            description = "descriptionTest",
            startDate = startDate,
            endDate = endDate,
            location = "locationTest",
            invitedMembersId = listOf(findUser1.username, findUser2.username)
        )
        val createEvent = eventService.createEvent(eventCreateDto)
        val findEvent = eventRepository.findEventWithDetails(createEvent.id)

        createEvent.title shouldBe findEvent.title
        createEvent.eventLocation?.location shouldBe findEvent.eventLocation?.location
        createEvent.members.size shouldBe findEvent.members.size
    }

    @Test
    fun deleteEventTest() {
        val findUser1 = userRepository.findById(userId1).get()
        val startDate: LocalDateTime = LocalDateTime.now().minusDays(1)
        val endDate: LocalDateTime = LocalDateTime.now()

        val eventCreateDto = EventCreateDto(
            title = "titleTest",
            description = "descriptionTest",
            startDate = startDate,
            endDate = endDate,
            location = "locationTest",
            invitedMembersId = listOf(findUser1.username)
        )
        val createEvent = eventService.createEvent(eventCreateDto)
        eventService.deleteEvent(createEvent.id)
        val findEvent = eventRepository.findById(createEvent.id).get()

        createEvent.delFlag shouldBe N
        findEvent.delFlag shouldBe Y
    }

    @Test
    fun updateEventTest() {
        val findUser1 = userRepository.findById(userId1).get()
        val findUser2 = userRepository.findById(userId2).get()
        val startDate: LocalDateTime = LocalDateTime.now().minusDays(1)
        val endDate: LocalDateTime = LocalDateTime.now()
        val changeStartDate: LocalDateTime = LocalDateTime.now().plusDays(3)
        val changeEndDate: LocalDateTime = LocalDateTime.now().plusDays(4)

        //create event
        val eventCreateDto = EventCreateDto(
            title = "title1",
            description = "description1",
            startDate = startDate,
            endDate = endDate,
            location = "location1",
            invitedMembersId = listOf(findUser1.username)
        )
        val createEvent = eventService.createEvent(eventCreateDto)
        val findEvent = eventRepository.findEventWithDetails(createEvent.id)

        //update event
        val eventMemberDto = EventMemberDto(
            invitedMembersId = findUser2.username,
            eventInvitationStatus = EventInvitationStatus.ACCEPTED
        )
        val eventUpdateDto = EventUpdateDto(
            eventKey = createEvent.id,
            title = "title2",
            description = "description2",
            startDate = changeStartDate,
            endDate = changeEndDate,
            location = "location2",
            eventMemberDto = listOf(eventMemberDto)
        )
        eventService.updateEvent(eventUpdateDto)
        val updateEvent = eventRepository.findEventWithDetails(createEvent.id)

        //check update event
        updateEvent.title shouldNotBe findEvent.title
        updateEvent.eventLocation?.location shouldNotBe findEvent.eventLocation?.location
        updateEvent.members[0]?.invitedUser?.username shouldNotBe findEvent.members[0]?.invitedUser?.username
    }
}
