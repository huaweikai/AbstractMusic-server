package com.hua.musicserver.other

import org.apache.commons.mail.HtmlEmail

fun String.toEmail(code:Int){
    HtmlEmail()
        .apply {
            hostName = "smtp.qq.com"
            setFrom(Key.EMAIL, "抽象音乐")
            setAuthentication(Key.EMAIL, Key.PASSWORD)
            subject = "注册验证码"
            addTo(this@toEmail)
            setTextMsg("您的注册验证码为:$code,请在五分钟内使用。\n五分钟之后验证码会失效。\n请不要将您的验证码告诉其他人。")
            send()
        }
}