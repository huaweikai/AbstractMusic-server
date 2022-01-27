package com.hua.musicserver.dao

import com.hua.musicserver.bean.AlbumBean
import com.hua.musicserver.bean.MusicBean
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface AlbumManagerMapper {

    @Select("select albumlist.*,artistlist.`name` as 'artistName' from albumList,artistlist where albumlist.artistId = artistlist.id")
    fun showAlbum():List<AlbumBean>

    @Select("select * from musicList where albumId = #{id}")
    fun selectMusicFromAlbum(@Param("id")id :String):List<MusicBean>

    @Select("SELECT * from albumList where `name` LIKE #{name}")
    fun selectAlbum(@Param("name") name:String):List<AlbumBean>

}