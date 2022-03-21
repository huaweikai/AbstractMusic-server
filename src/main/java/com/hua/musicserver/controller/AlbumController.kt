package com.hua.musicserver.controller

import cn.dev33.satoken.util.SaResult
import com.hua.musicserver.dao.AlbumManagerMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/album")
class AlbumController {

    @Autowired
    lateinit var albumManagerMapper: AlbumManagerMapper

    @GetMapping("/list")
    fun albumList(): SaResult {
        return SaResult.data(albumManagerMapper.showAlbum())
    }

    @GetMapping("/recommend/list")
    fun recommendAlbumList(): SaResult {
        return SaResult.data(albumManagerMapper.showRecommendAlbum())
    }

    @GetMapping("/{id}/music")
    fun selectMusicFromAlbum(
        @PathVariable id: String
    ): SaResult {
        return SaResult.data(albumManagerMapper.selectMusicFromAlbum(id))
    }

    @GetMapping("/search/{name}")
    fun selectAlbumByName(
        @PathVariable name: String
    ): SaResult {
        val data = albumManagerMapper.selectAlbum("%$name%")
        return if (data.isEmpty()) {
            SaResult.error()
        } else {
            SaResult.data(data)
        }
    }

    @GetMapping("/{id}")
    fun selectAlbumById(
        @PathVariable id: String
    ): SaResult {
        val data = albumManagerMapper.selectAlbumById(id)
        return if (data == null) {
            SaResult.error()
        } else {
            SaResult.data(data)
        }
    }
}