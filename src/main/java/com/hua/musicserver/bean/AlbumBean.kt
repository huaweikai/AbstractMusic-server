package com.hua.musicserver.bean

import java.sql.Date

data class AlbumBean(
    var id: Int,
    var artistId: Int,
    var name: String,
    var time: Date,
    var albumDesc: String,
    var num:Int,
    var imgUrl: String,
    var artistName: String
)