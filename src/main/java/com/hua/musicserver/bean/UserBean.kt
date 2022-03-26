package com.hua.musicserver.bean

import com.hua.musicserver.noarg.NoArg
import java.sql.Date

@NoArg
data class UserBean(
    var id :Int?,
    var name: String,
    var email:String,
    var passwd:String,
    var head:String?,
    var createTime: Date
)
