package dev.verite.myposts

import retrofit2.http.Body

data class  Post(
    var userId: Int,
    var id: Int,
    var title: String,
    var body: String,
)
data class Comment(
    var postId: Int,
    var id: String,
    var name: String,
    var email: String,
    var body: String,
)