package com.hua.musicserver.bean

data class UserBean(
    var id :Int?,
    var name: String,
    var email:String,
    var passwd:String,
    var head:String?
){
    constructor():this(0,"","","",null)

    override fun toString(): String {
        return "UserBean(id=$id, username='$name', email='$email', password='$passwd,head='$head')"
    }

}