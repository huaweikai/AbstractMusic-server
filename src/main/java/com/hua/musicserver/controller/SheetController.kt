package com.hua.musicserver.controller

import cn.dev33.satoken.stp.StpUtil
import cn.dev33.satoken.util.SaResult
import com.hua.musicserver.bean.SheetBean
import com.hua.musicserver.dao.MusicManagerMapper
import com.hua.musicserver.dao.SheetManagerMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.random.Random

/*
 *等把所有功能写完之后，将所有错误作为状态值返回，让客户端可以根据错误值显示对应错误，而不是显示中文
 */

@RestController
@RequestMapping("/sheet")
class SheetController {

    private val bannerMap = HashMap<String, Int>()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    @Autowired
    lateinit var sheetManagerMapper: SheetManagerMapper

    @Autowired
    lateinit var musicManagerMapper: MusicManagerMapper

    @GetMapping("/recommend")
    fun recommendSheet(): SaResult {
        val count = sheetManagerMapper.selectSheetNum()
        val random = Random.nextInt(0,count-12)
        return SaResult.data(sheetManagerMapper.recommendList(random,12))
    }

    @GetMapping("/{id}/list")
    fun getRecommendList(
        @PathVariable("id") id: String
    ): SaResult {
        return SaResult.data(sheetManagerMapper.getMusicBySheetId(id))
    }

    @GetMapping("/banner")
    fun getBanner(): SaResult {
        val date = dateFormat.format(Date(System.currentTimeMillis()))
        val start = if (bannerMap.containsKey(date)) {
            bannerMap[date]!!
        } else {
            val count = sheetManagerMapper.selectAlbumCount()
            val random = Random.nextInt(0,count-3)
            bannerMap[date] = random
            random
        }
        return SaResult.data(sheetManagerMapper.selectBanner(start,3))
    }

    @GetMapping("/userSheet")
    fun getUserSheet(
        token: String
    ): SaResult {
        val id = StpUtil.getLoginIdByToken(token) ?: return SaResult.error("无此token")
        return SaResult.data(sheetManagerMapper.getUserSheet(id.toString()))
    }

    @GetMapping("/addSheet")
    fun insertUserSheet(
        sheetId: String,
        musicId: String,
        token: String
    ): SaResult {
        val id = StpUtil.getLoginIdByToken(token) ?: return SaResult.error()
        if (id != sheetManagerMapper.selectUserIdBySheetId(sheetId)) return SaResult.error("无权为这个歌单增加音乐")
        val musicLists = sheetManagerMapper.selectMusicIdBySheetId(sheetId)
        val music = musicManagerMapper.selectMusicById(musicId)
        val sheet = sheetManagerMapper.selectSheetBySheetId(sheetId)
        if (musicId in musicLists) return SaResult.error("${music?.name} 已存在歌单 ${sheet?.title} 中")
        return try {
            if (sheetManagerMapper.insertUserSheet(sheetId, musicId) == 1) {
                SaResult.ok("加入歌单成功")
            } else {
                SaResult.error("加入歌单失败，请再次尝试")
            }
        } catch (e: Exception) {
            SaResult.error("发生未知错误，请重试")
        }
    }

    @GetMapping("/createSheet")
    fun createSheet(
        token: String,
        title: String
    ): SaResult {
        val id = StpUtil.getLoginIdByToken(token) ?: return SaResult.error()
        val titles = sheetManagerMapper.selectTitleByUser(id.toString())
        if (title in titles) return SaResult.error("请勿添加重复的歌单名")
        return try {
            if (sheetManagerMapper.createNewSheet(title, id.toString()) == 1) {
                SaResult.ok("创建歌单成功")
            } else {
                SaResult.error()
            }
        } catch (e: Exception) {
            SaResult.error()
        }
    }

    @GetMapping("/delete/{sheetId}/{musicId}")
    fun deleteMusicFromSheet(
        token: String,
        @PathVariable sheetId: String,
        @PathVariable musicId: String
    ): SaResult {
        val id = StpUtil.getLoginIdByToken(token) ?: return SaResult.error()
        if (id.toString() != sheetManagerMapper.selectUserIdBySheetId(sheetId)) {
            return SaResult.error("无权限移除这首歌")
        }
        return try {
            sheetManagerMapper.deleteMusicFromSheet(sheetId, musicId)
            SaResult.ok()
        } catch (e: Exception) {
            SaResult.error()
        }
    }

    @GetMapping("delete/{sheetId}")
    fun deleteSheet(
        token: String,
        @PathVariable sheetId: String
    ): SaResult {
        val id = StpUtil.getLoginIdByToken(token) ?: return SaResult.error()
        if (id.toString() != sheetManagerMapper.selectUserIdBySheetId(sheetId)) {
            return SaResult.error("无权删除该歌单")
        }
        return try {
            sheetManagerMapper.deleteAllMusicFromSheet(sheetId)
            sheetManagerMapper.deleteSheet(sheetId)
            SaResult.ok()
        } catch (e: Exception) {
            SaResult.error()
        }
    }

    @PostMapping("/update")
    fun updateSheet(
        token: String,
        @RequestBody sheetBean: SheetBean
    ): SaResult {
        val id = StpUtil.getLoginIdByToken(token) ?: return SaResult.error()
        if (id.toString() != sheetManagerMapper.selectUserIdBySheetId(sheetBean.id.toString())) {
            return SaResult.error("无权修改该歌单")
        }
        return try {
            sheetManagerMapper.updateSheet(sheetBean)
            SaResult.ok()
        } catch (e: Exception) {
            SaResult.error()
        }
    }


    @GetMapping("/{id}")
    fun selectSheetBySheetId(
        @PathVariable("id") id: String
    ): SaResult {
        return try {
            val data = sheetManagerMapper.selectSheetBySheetId(id)
            if (data == null) {
                SaResult.error()
            } else {
                SaResult.data(data)
            }
        } catch (e: Exception) {
            SaResult.error()
        }
    }

    @GetMapping("/search/{name}")
    fun selectMusicByAlbumName(
        @PathVariable name: String
    ): SaResult {
        val data = sheetManagerMapper.selectSheetByName("%$name%")
        return SaResult.data(data)
    }
}