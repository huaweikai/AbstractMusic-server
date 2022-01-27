package com.hua.musicserver.dao

import com.hua.musicserver.bean.ArtistBean
import com.hua.musicserver.bean.MusicBean
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface MusicManagerMapper {

    @Select("select * from musicList")
    fun getMusicList():List<MusicBean>

    @Select("SELECT * from musicList where `name` LIKE #{name}")
    fun selectMusic(@Param("name") name:String):List<MusicBean>

}