package com.android.socialmedia.Model.home

class Home {

    data class Story(
        var id: String = "",
        var userId: String = "",
        var userName: String = "",
        var userAvatar: String = "",
        var videoLink: String = "",
        var userStories: Long = 0)

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