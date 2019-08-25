package com.android.socialmedia.Model.like

class Like {

    data class Follow(
        var id: String = "",
        var userId: String = "",
        var userName: String = "",
        var userAvatar: String = "",
        var message: String = "",
        var mentionUser: String = "",
        var imageLike: String = "")

    data class You(
        var id: String = "",
        var userId: String = "",
        var userName: String = "",
        var userAvatar: String = "",
        var message: String = "",
        var mentionUser: String = "",
        var imageLike: String = "")
}