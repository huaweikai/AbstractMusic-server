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
    private val emailCode = HashMap<String, Int>()
    private val codeTime = HashMap<String, Long>()

    //注册用户
    @RequestMapping("/register")
    fun registerUser(
        email: String,
        password: String,
        username: String,
        code: Int
    ): SaResult {
        if (emailCode[email] == null) {
            return SaResult.error("该邮箱未申请验证码")
        }
        if (System.currentTimeMillis() - codeTime[email]!! > 1000 * 60 * 5L) {
           return SaResult.error("验证码已失效")
        }
        if (userManagerMapper.selectNameCount(username) != 0) {
            return SaResult.error("用户名已存在")
        }
        return if (code == emailCode[email]) {
            val userBean = UserBean(null, username, email, password)
            if (userManagerMapper.insertUser(userBean) == 1) {
                emailCode.remove(email)
                codeTime.remove(email)
                SaResult.ok("用户注册成功")
            } else {
                SaResult.error("用户注册失败")
            }
        } else {
            SaResult.error("验证码错误")
        }
    }


    @RequestMapping("/emailCode")
    fun insertUser(
        email: String
    ): SaResult {
        return if (userManagerMapper.selectEmailCount(email) > 0) {
            SaResult.error("邮箱已存在")
        } else {
            val code = (100000..999999).random()
            emailCode[email] = code
            codeTime[email] = System.currentTimeMillis()
            email.toEmail(code)
            SaResult.ok("成功获取验证码")
        }
    }

    //用户名用户，返回token
    @RequestMapping("/name/login")
    fun loginWithNameServer(
        username: String,
        password: String
    ): SaResult {
        val userBean = userManagerMapper.selectUser(username)
        return loginResult(userBean, password)
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
    @RequestMapping("/get")
    fun getUser(
        token: String
    ): SaResult {
        val i = StpUtil.getLoginIdByToken(token) ?: return SaResult.error("无此token")
        val id = i.toString().toInt()
        val user = userManagerMapper.selectUserById(id)
        return SaResult.data(user)
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
    ):SaResult{
        val test = StpUtil.getLoginIdByToken(token)
        return if(test == null) SaResult.error() else SaResult.ok()
    }
}