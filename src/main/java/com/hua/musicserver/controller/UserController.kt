package com.hua.musicserver.controller

import cn.dev33.satoken.stp.StpUtil
import cn.dev33.satoken.util.SaResult
import com.hua.musicserver.bean.UserBean
import com.hua.musicserver.dao.UserManagerMapper
import com.hua.musicserver.other.toEmail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import kotlin.collections.HashMap

@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    lateinit var userManagerMapper: UserManagerMapper
    private val emailCodeRegister = HashMap<String, Int>()
    private val emailCodeRegisterTime = HashMap<String, Long>()
    private val emailCodeLogin = HashMap<String, Int>()
    private val emailCodeLoginTime = HashMap<String, Long>()

    //注册用户
    @RequestMapping("/register")
    fun registerUser(
        email: String,
        password: String,
        username: String,
        code: Int
    ): SaResult {
        if (emailCodeRegister[email] == null) {
            return SaResult.error("该邮箱未申请验证码")
        }
        if (System.currentTimeMillis() - emailCodeRegisterTime[email]!! > 1000 * 60 * 5L) {
            return SaResult.error("验证码已失效")
        }
        if (userManagerMapper.selectNameCount(username) != 0) {
            return SaResult.error("用户名已存在")
        }
        return if (code == emailCodeRegister[email]) {
            val userBean = UserBean(null, username, email, password, null)
            if (userManagerMapper.insertUser(userBean) == 1) {
                emailCodeRegister.remove(email)
                emailCodeRegisterTime.remove(email)
                SaResult.ok("用户注册成功")
            } else {
                SaResult.error("用户注册失败")
            }
        } else {
            SaResult.error("验证码不匹配")
        }
    }

    @RequestMapping("/emailCode/register")
    fun insertUser(
        email: String
    ): SaResult {
        return if (userManagerMapper.selectEmailCount(email) > 0) {
            SaResult.error("邮箱已存在")
        } else {
            sendEmail(emailCodeRegister, emailCodeRegisterTime, email)
        }
    }

//    //用户名用户，返回token
//    @RequestMapping("/name/login")
//    fun loginWithNameServer(
//        username: String,
//        password: String
//    ): SaResult {
//        val userBean = userManagerMapper.selectUser(username)
//        return loginResult(userBean, password)
//    }

    //验证码登录
    @RequestMapping("/code/login")
    fun loginWithNameServer(
        email: String,
        code: Int
    ): SaResult {
        if (emailCodeLogin[email] == null) {
            return SaResult.error("该邮箱未申请验证码")
        }
        if (System.currentTimeMillis() - emailCodeLoginTime[email]!! > 1000 * 60 * 5L) {
            return SaResult.error("验证码已失效")
        }
        return if (emailCodeLogin[email] == code) {
            val id = userManagerMapper.selectIdByEmail(email)
            StpUtil.login(id)
            SaResult.data(StpUtil.getTokenInfo().tokenValue)
        } else {
            SaResult.error("验证码不匹配")
        }
    }

    //邮箱登录，返回token
    @RequestMapping("/email/login")
    fun loginWithEmailServer(
        email: String,
        password: String
    ): SaResult {
        val userBean = userManagerMapper.selectUserByEmail(email)
        return loginResult(userBean, password)
    }

    //获取用户信息
    @RequestMapping("/getInfo")
    fun getUser(
        token: String
    ): SaResult {
        val i = StpUtil.getLoginIdByToken(token) ?: return SaResult.error("无此token")
        val id = i.toString().toInt()
        val user = userManagerMapper.selectUserById(id)
        return SaResult.data(user)
    }

    //设置用户信息
    @RequestMapping("/setInfo")
    fun setUser(
        token: String,
        @RequestBody userBean: UserBean
    ): SaResult {
        val i = StpUtil.getLoginIdByToken(token) ?: return SaResult.error("无此token")
        val id = i.toString().toInt()
        return if (userManagerMapper.updateUser(
                userBean.copy(id = id)
            ) == 1
        ) {
            SaResult.ok()
        } else {
            SaResult.error()
        }
    }

    //退出信息
    @RequestMapping("/logout")
    fun logoutUser(
        token: String
    ): SaResult {
        StpUtil.getLoginIdByToken(token)?.let {
            StpUtil.logout(it.toString().toInt())
        }
        return SaResult.ok("退出成功")
    }

    //注销用户
    @RequestMapping("/delete")
    fun deleteUser(
        token: String
    ): SaResult {
        StpUtil.getLoginIdByToken(token)?.let {
            StpUtil.logout(it.toString().toInt())
            userManagerMapper.deleteUser(it.toString().toInt())
        }
        return SaResult.ok("删除用户数据成功,感谢您的使用")
    }

    private fun loginResult(userBean: UserBean?, password: String): SaResult {
        return if (userBean == null) {
            SaResult.error("用户不存在，请先注册")
        } else {
            if (password == userBean.passwd) {
                StpUtil.login(userBean.id)
                SaResult.data(StpUtil.getTokenInfo().tokenValue)
            } else {
                SaResult.error("登录失败,请重试")
            }
        }
    }

    @RequestMapping("/testToken")
    fun testToken(
        token: String
    ): SaResult {
        val test = StpUtil.getLoginIdByToken(token)
        return if (test == null) SaResult.error("登录失效，重新登录") else SaResult.ok()
    }

    @RequestMapping("/emailCode/login")
    fun getEmailCodeWithLogin(
        email: String
    ): SaResult {
        val result = userManagerMapper.selectEmailCount(email)
        return if (result == 0) {
            SaResult.error("该邮箱未注册")
        } else {
            sendEmail(emailCodeLogin, emailCodeLoginTime, email, true)
        }
    }

    private fun sendEmail(
        emailMap: HashMap<String, Int>,
        emailTime: HashMap<String, Long>,
        email: String,
        login: Boolean = false
    ): SaResult {
        val code = (100000..999999).random()
        emailMap[email] = code
        emailTime[email] = System.currentTimeMillis()
        return try {
            email.toEmail(code, login)
            SaResult.ok("成功获取验证码")
        } catch (e: Throwable) {
            emailMap.remove(email)
            emailTime.remove(email)
            SaResult.error("发送失败，请检验邮箱")
        }
    }
}