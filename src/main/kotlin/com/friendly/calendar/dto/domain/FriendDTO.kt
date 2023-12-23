package com.friendly.calendar.dto.domain

import com.friendly.calendar.domain.model.FriendRelation
import com.friendly.calendar.dto.domain.FriendDTO.FriendReturnDTO

sealed class FriendDTO {
    data class FriendRequestDTO(
        val receiverId: Long,
    ) : FriendDTO()

    data class FriendPatchDTO(
        val senderId: Long,
    ) : FriendDTO()

    data class FriendRejectDTO(
        val senderId: Long,
        val isBlock: Boolean = false,
    ) : FriendDTO()

    data class FriendReturnDTO(
        val id: Long,
        val friendAlias: String,
        val introduce: String?,
        val path: String?,
        val email: String?
    ) : FriendDTO()
}

fun FriendRelation.toFriendDto(): FriendReturnDTO = FriendReturnDTO(
    id = friend.id,
    friendAlias = friendAlias ?: friend.username,
    introduce = friend.profile?.introduce,
    path = friend.profile?.path,
    email = friend.email,
)
