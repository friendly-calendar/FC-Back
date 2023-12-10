package com.friendly.calendar.network.domain

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
}
