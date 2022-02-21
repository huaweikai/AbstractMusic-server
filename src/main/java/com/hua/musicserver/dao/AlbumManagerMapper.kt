package com.hua.musicserver.dao

import com.hua.musicserver.bean.AlbumBean
import com.hua.musicserver.bean.MusicBean
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface AlbumManagerMapper {

    @Select("select albumList.*,artistList.`name` as 'artistName' from albumList,artistList where albumList.artistId = artistList.id ORDER BY albumlist.time DESC")
    fun showAlbum():List<AlbumBean>

    @Select(
        "SELECT musiclist.id,musiclist.`name`,albumlist.imgUrl AS imgUrl,musiclist.musicUrl,musiclist.albumId,albumlist.`name` as albumName,artist " +
                "                FROM musicList,albumlist where musiclist.albumId = albumlist.id and albumlist.id= #{id} order by musiclist.createTime desc"
    )
    fun selectMusicFromAlbum(@Param("id")id :String):List<MusicBean>

    @Select("SELECT * from albumList where `name` LIKE #{name}")
    fun selectAlbum(@Param("name") name:String):List<AlbumBean>

}