package com.hua.musicserver.dao

import com.hua.musicserver.bean.AlbumBean
import com.hua.musicserver.bean.ArtistBean
import com.hua.musicserver.bean.MusicBean
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

private const val artistBean = "artistlist.*,(select count(*) from musictoartist where artistId = artistlist.id) as 'num'"

@Mapper
interface ArtistManagerMapper {

    @Select("SELECT $artistBean FROM `artistlist`")
    fun getArtistList():List<ArtistBean>

    @Select("select $albumBean from albumList,artistList where albumlist.artistId = artistList.id and artistlist.id = #{id} ORDER BY albumlist.time DESC")
    fun selectAlbumFromArtist(@Param("id")id :Int):List<AlbumBean>

    @Select("SELECT $musicBean FROM musiclist,artistlist,musictoartist,albumlist WHERE albumlist.id = musiclist.albumId and musictoartist.artistId = artistlist.id and musictoartist.musicId = musiclist.id and artistlist.id = #{id}")
    fun selectMusicFromArtist(@Param("id")id: Int):List<MusicBean>

    @Select("SELECT $artistBean from artistList where `name` LIKE #{name}")
    fun selectArtist(@Param("name") name:String):List<ArtistBean>

    @Select("select $artistBean from artistlist,musictoartist where artistlist.id = musictoartist.artistId and musictoartist.musicId = #{musicId} ")
    fun selectArtistIdByMusicId(musicId:String):List<ArtistBean>

    @Select("select $artistBean from `artistlist` where `artistlist`.id = #{id}")
    fun selectArtistById(@Param("id") id:String):ArtistBean?
}