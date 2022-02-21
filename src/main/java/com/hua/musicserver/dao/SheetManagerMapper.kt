package com.hua.musicserver.dao

import com.hua.musicserver.bean.AlbumBean
import com.hua.musicserver.bean.MusicBean
import com.hua.musicserver.bean.SheetBean
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import java.net.URLDecoder
import java.util.*

@Mapper
interface SheetManagerMapper {

    @Select(
        "select albumlist.*,artistlist.`name` as artistName FROM albumlist,artistlist " +
                "WHERE (albumlist.id = #{id1} or albumlist.id =#{id2} or albumlist.id = #{id3}) " +
                "and albumlist.artistId = artistlist.id"
    )
    fun selectBanner(id1: Int, id2: Int, id3: Int): List<AlbumBean>

    @Select(
        "select * from serversheet"
    )
    fun recommendList():List<SheetBean>

    @Select(
        "SELECT musiclist.id,musiclist.`name`,albumlist.imgUrl AS imgUrl,musiclist.musicUrl,musiclist.albumId,albumlist.`name` as albumName,artist " +
                "FROM musicList,albumlist,sheettomusic where musiclist.albumId = albumlist.id " +
                "and sheettomusic.sheetId = #{id} and sheettomusic.musicId = musiclist.id order by musiclist.createTime desc"
    )
    fun getRecommendList(id:String):List<MusicBean>

}