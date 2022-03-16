package com.hua.musicserver.controller

import cn.dev33.satoken.util.SaResult
import com.hua.musicserver.dao.ArtistManagerMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/artist")
class ArtistController {

    @Autowired
    lateinit var artistManagerMapper: ArtistManagerMapper

    @GetMapping("/list")
    fun getArtistList(): SaResult {
        artistManagerMapper.getArtistList().also {
            return SaResult.data(it)
//            return SaResult.get(200, "获取成功", it)
        }
    }

    @GetMapping("/{id}/album")
    fun selectArtistAlbum(
        @PathVariable("id") id: Int
    ): SaResult {
        val list = artistManagerMapper.selectAlbumFromArtist(id)
        return if (list.isEmpty()) {
            SaResult.error("该歌手没有专辑")
        } else {
            SaResult.data(list)
        }
    }

    @GetMapping("/{id}/music")
    fun selectArtistMusic(
        @PathVariable("id") id: Int
    ): SaResult {
        val list = artistManagerMapper.selectMusicFromArtist(id)
        return if (list.isEmpty()) {
            SaResult.error("该歌手没有歌曲")
        } else {
//            SaResult.get(200, "获取到${list.size}个歌曲", list)
            SaResult.data(list)
        }
    }

    @GetMapping("/{name}")
    fun selectArtist(
        @PathVariable name: String
    ): SaResult {
        val data = artistManagerMapper.selectArtist(name)
        return if (data.isEmpty()) {
            SaResult.error()
        } else {
            SaResult.data(data)
        }
    }

    @GetMapping("/{musicId}/list")
    fun selectArtistByMusicId(
        @PathVariable musicId: String
    ): SaResult {
        return SaResult.data(artistManagerMapper.selectArtistIdByMusicId(musicId))
    }
}