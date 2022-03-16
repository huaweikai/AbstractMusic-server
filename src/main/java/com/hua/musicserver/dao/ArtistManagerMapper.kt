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

    @Select("select albumList.*,artistList.`name` as 'artistName' from albumList,artistList where albumlist.artistId = artistList.id and artistlist.id = #{id} ORDER BY albumlist.time DESC")
    fun selectAlbumFromArtist(@Param("id")id :Int):List<AlbumBean>

    @Select("SELECT musiclist.id,musiclist.`name`,albumlist.imgUrl as 'imgUrl',musiclist.musicUrl,musiclist.albumId,albumlist.`name` as 'albumName',musiclist.artist " +
            "FROM musiclist,artistlist,musictoartist,albumlist WHERE albumlist.id = musiclist.albumId and musictoartist.artistId = artistlist.id and musictoartist.musicId = musiclist.id and artistlist.id = #{id}")
    fun selectMusicFromArtist(@Param("id")id: Int):List<MusicBean>

    @Select("SELECT * from artistList where `name` LIKE #{name}")
    fun selectArtist(@Param("name") name:String):List<ArtistBean>

    @Select("select artistlist.* from artistlist,musictoartist where artistlist.id = musictoartist.artistId and musictoartist.musicId = #{musicId} ")
    fun selectArtistIdByMusicId(musicId:String):List<ArtistBean>
}