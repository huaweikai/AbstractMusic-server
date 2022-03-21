package com.hua.musicserver.controller

import cn.dev33.satoken.util.SaResult
import com.hua.musicserver.dao.MusicManagerMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
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

    @RequestMapping("/search/{name}")
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

    @RequestMapping("/{id}/lyrics")
    fun selectLyrics(
        @PathVariable id:String
    ):SaResult{
        val lyrics = managerMapper.selectLyrics(id)
        return if(lyrics != null){
            SaResult.data(lyrics)
        }else{
            SaResult.error()
        }
    }

    @GetMapping("/{id}")
    fun selectMusicById(
        @PathVariable id: String
    ):SaResult{
        return try {
            val result = managerMapper.selectMusicById(id)
            if(result == null){
                SaResult.error()
            }else{
                SaResult.data(result)
            }
        }catch (e:Exception){
            SaResult.error()
        }
    }


    @RequestMapping("/test1")
    fun test(
        start:Int,
        end:Int,
        artistId:String
    ){
        for(i in start..end){
            managerMapper.insertTest(i.toString(),artistId)
        }
    }
}