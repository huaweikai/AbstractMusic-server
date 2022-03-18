package com.hua.musicserver.dao

import com.hua.musicserver.bean.MusicBean
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface MusicManagerMapper {

    @Select("SELECT " +
            "musiclist.id,musiclist.`name`,albumlist.imgUrl AS imgUrl,musiclist.musicUrl,musiclist.albumId,albumlist.`name` as albumName,artist " +
            "FROM musiclist,albumlist " +
            "where musiclist.albumId = albumlist.id order by musiclist.createTime desc")
    fun getMusicList():List<MusicBean>

    @Select("SELECT musiclist.id,musiclist.`name`,albumlist.imgUrl AS imgUrl,musiclist.musicUrl,musiclist.albumId,albumlist.`name` as albumName,artist " +
            "FROM musiclist,albumlist " +
            "where musiclist.albumId = albumlist.id and musiclist.`name` like #{name} order by musiclist.createTime desc")
    fun selectMusic(@Param("name") name:String):List<MusicBean>

    @Select("SELECT lyricslist.lyrics FROM `lyricslist` WHERE musicId = #{id}")
    fun selectLyrics(@Param("id")id :String):String?

    @Insert("insert into `abstractmusic`.`musictoartist` ( `musicId`, `artistId`) VALUES (#{musicId},#{artistId})")
    fun insertTest(musicId:String,artistId:String)

}