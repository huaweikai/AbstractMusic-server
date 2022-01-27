package com.hua.musicserver.dao

import com.hua.musicserver.bean.AlbumBean
import com.hua.musicserver.bean.ArtistBean
import com.hua.musicserver.bean.MusicBean
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface ArtistManagerMapper {

    @Select("select * from artistList")
    fun getArtistList():List<ArtistBean>

    @Select("select * from albumList where artistId = #{id}")
    fun selectAlbumFromArtist(@Param("id")id :Int):List<AlbumBean>

    @Select("SELECT musicList.* from musicList,artistList,musicToArtist WHERE musicId=musicList.id AND artistId=artistList.id AND artistId = #{id}")
    fun selectMusicFromArtist(@Param("id")id: Int):List<MusicBean>

    @Select("SELECT * from artistList where `name` LIKE #{name}")
    fun selectArtist(@Param("name") name:String):List<ArtistBean>
}