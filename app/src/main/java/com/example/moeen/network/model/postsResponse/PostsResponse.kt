package com.example.moeen.network.model.postsResponse

data class PostsResponse(
    val `data`: List<Data>,
    val links: Links,
    val massage: String,
    val meta: Meta,
    val status: Int
)