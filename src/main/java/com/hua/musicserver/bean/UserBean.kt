package com.hua.musicserver.bean

import com.hua.musicserver.noarg.NoArg

@NoArg
data class UserBean(
    var id :Int?,
    var name: String,
    var email:String,
    var passwd:String,
    var head:String?
)