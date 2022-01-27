package com.hua.musicserver.controller

import cn.dev33.satoken.util.SaResult
import com.hua.musicserver.bean.MusicBean
import com.hua.musicserver.dao.MusicManagerMapper
import org.apache.ibatis.annotations.Param
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/music")
class MusicController {

    @Autowired
    lateinit var managerMapper: MusicManagerMapper

    @RequestMapping("/list")
    fun showMusicList(): SaResult {
        return SaResult.data(managerMapper.getMusicList())
    }

    @RequestMapping("/{name}")
    fun selectMusic(
        @PathVariable name: String
    ): SaResult {
        val list = managerMapper.selectMusic("%$name%")
        return if (list.isEmpty()) {
            SaResult.error()
        } else {
            SaResult.data(list)
        }
    }

}