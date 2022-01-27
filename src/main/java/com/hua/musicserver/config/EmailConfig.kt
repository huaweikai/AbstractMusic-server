package com.hua.musicserver.config

import org.apache.commons.mail.HtmlEmail
import org.springframework.stereotype.Component

object EmailConfig {
    private val htmlEmail=HtmlEmail()
        .apply {
            hostName = "smtp.qq.com"
            setFrom("1297720454@qq.com", "抽象音乐")
            setAuthentication("1297720454@qq.com", "scfixycqhmtfifid")
            subject = "注册验证码"
            send()
        }
    fun sendEmail(email:String,code:Int){
        htmlEmail.addTo(email)
        htmlEmail.setTextMsg("您的注册验证码为:$code")
    }
}