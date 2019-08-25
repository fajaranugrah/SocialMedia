package com.android.socialmedia.Model.profile

class Profile {

    data class MyPost(
        var id: String = "",
        var content_type: String = "",
        var media_filename: String = "",
        var created_time: String = "")

    data class AccountInfo(
        var id: String = "",
        var userId: String = "",
        var userName: String = "",
        var userAvatar: String = "",
        var messageProfile: String = "",
        var imageOrVideoPostUser: MutableList<MyPost> = mutableListOf(),
        var numberPost: Int = 0,
        var numberFollowers: Int = 0,
        var numberFollowing: Int = 0)

}