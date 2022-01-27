package com.hua.musicserver.bean

data class MusicBean(
    var id: Int,
    var name: String,
    var imgUrl: String,
    var musicUrl: String,
    var albumId: Int,
    var albumName: String,
    var artist: String
)