package com.hua.musicserver.bean

data class UserBean(
    var id :Int?,
    var name: String,
    var email:String,
    val passwd:String
){
    constructor():this(0,"","","")

    override fun toString(): String {
        return "UserBean(id=$id, username='$name', email='$email', password='$passwd')"
    }

}