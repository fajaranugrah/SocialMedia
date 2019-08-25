package com.android.socialmedia.Model.search

class Search {

    data class PostUser(
        var id: String = "",
        var content_type: String = "",
        var media_filename: String = "",
        var created_time: String = "")

    data class Post(
        var id: String = "",
        var userId: String = "",
        var userName: String = "",
        var userAvatar: String = "",
        var captionPostUser: String = "",
        var imageOrVideoPostUser: MutableList<PostUser> = mutableListOf(),
        var likesPost: String = "",
        var commentPost: Int = 0)
}