package com.friendly.calendar.domain.service.impl

import com.friendly.calendar.domain.model.FriendRelation
import com.friendly.calendar.domain.model.FriendStatus
import com.friendly.calendar.domain.model.base.DelFlag
import com.friendly.calendar.domain.persistence.CalendarUserRepository
import com.friendly.calendar.domain.persistence.FriendRelationRepository
import com.friendly.calendar.domain.persistence.util.findByIdOrThrow
import com.friendly.calendar.domain.service.FriendService
import com.friendly.calendar.dto.domain.FriendDTO.FriendReturnDTO
import com.friendly.calendar.dto.domain.toFriendDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FriendServiceImpl(
    private val calendarUserRepository: CalendarUserRepository,
    private val friendRelationRepository: FriendRelationRepository
) : FriendService {

    override fun requestFriend(senderId: Long, receiverId: Long) {
        val sender = calendarUserRepository.findByIdOrThrow(senderId, "User not found")
        val receiver = calendarUserRepository.findByIdOrThrow(receiverId, "User not found")

        require(canRequestFriend(senderId, receiverId)) {
            "Cannot send friend request to blocked user"
        }

        val friendRelation = friendRelationRepository.findFriendRelationByUserIdAndFriendIdWithDeleted(senderId, receiverId)
            ?: FriendRelation(sender, receiver)
        friendRelation.request()

        friendRelationRepository.save(friendRelation)
    }

    @Transactional
    override fun acceptFriend(senderId: Long, receiverId: Long) {
        val pendingFriendRelation: FriendRelation? = friendRelationRepository.findPendingRelationByUserIdAndFriendId(senderId, receiverId)

        require(pendingFriendRelation != null) {
            "Friend request not found"
        }

        val mutualFriendRelations = mutualFriendPair(senderId, receiverId).toList()
        friendRelationRepository.saveAll(mutualFriendRelations.onEach { it.accept() })
    }

    @Transactional
    override fun rejectFriend(senderId: Long, receiverId: Long, isBlock: Boolean) {
        val pendingFriendRelation: FriendRelation? = friendRelationRepository.findPendingRelationByUserIdAndFriendId(senderId, receiverId)

        require(pendingFriendRelation != null) {
            "Friend request not found"
        }

        isBlock.takeIf { it }?.let {
            pendingFriendRelation.block(receiverId)

            val friendRelation = friendRelationRepository.findFriendRelationByUserIdAndFriendId(receiverId, senderId)
                ?: FriendRelation(pendingFriendRelation.friend, pendingFriendRelation.user)
            friendRelation.block(receiverId)
            friendRelationRepository.save(friendRelation)
        }
            ?: pendingFriendRelation.reject()
    }

    override fun blockFriend(blockById: Long, blockToId: Long) {
        require(blockById != blockToId) {
            "Cannot block friend to yourself"
        }

        val (firstUserFriendRelation, secondUserFriendRelation) = mutualFriendPair(blockById, blockToId)

        val mutualFriendRelations = listOf(firstUserFriendRelation, secondUserFriendRelation).onEach { it.block(blockById) }
        friendRelationRepository.saveAll(mutualFriendRelations)
    }

    @Transactional
    override fun unblockFriend(unblockById: Long, unblockToId: Long) {
        val friendPair = mutualFriendPair(unblockById, unblockToId)

        require(friendPair.statusIs(FriendStatus.BLOCKED)) {
            "Not exists block relation"
        }

        friendPair.toList().forEach {
            it.unBlock(unblockById)
        }
    }

    override fun getFriendList(userId: Long): List<FriendReturnDTO> {
        val friendRelationList = friendRelationRepository.findAllByUserId(userId)

        return friendRelationList.filter {
            it.delFlag == DelFlag.N && it.status == FriendStatus.ACCEPTED
        }.map(FriendRelation::toFriendDto)
    }

    override fun getBlockedList(userId: Long): List<FriendReturnDTO> {
        val friendRelationList = friendRelationRepository.findAllByUserId(userId)

        return friendRelationList.filter {
            it.delFlag == DelFlag.N && it.status == FriendStatus.BLOCKED
        }.map(FriendRelation::toFriendDto)
    }

    private fun mutualFriendPair(
        firstUserId: Long,
        secondUserId: Long,
    ): Pair<FriendRelation, FriendRelation> {
        val firstUser = calendarUserRepository.findByIdOrThrow(firstUserId, "User not found")
        val secondUser = calendarUserRepository.findByIdOrThrow(secondUserId, "User not found")

        val firstUserFriendRelation =
            friendRelationRepository.findFriendRelationByUserIdAndFriendId(firstUserId, secondUserId)
                ?: FriendRelation(firstUser, secondUser)
        val secondUserFriendRelation =
            friendRelationRepository.findFriendRelationByUserIdAndFriendId(secondUserId, firstUserId)
                ?: FriendRelation(secondUser, firstUser)

        return Pair(firstUserFriendRelation, secondUserFriendRelation)
    }

    private fun canRequestFriend(senderId: Long, receiverId: Long): Boolean {
        require(senderId != receiverId) {
            "Cannot send friend request to yourself"
        }

        val friendRelation =
            friendRelationRepository.findFriendRelationByUserIdAndFriendId(senderId, receiverId) ?: return true

        return friendRelation.status == FriendStatus.REJECTED
    }

    private fun Pair<FriendRelation, FriendRelation>.statusIs(friendStatus: FriendStatus): Boolean =
        this.first.status == friendStatus && this.second.status == friendStatus
}
