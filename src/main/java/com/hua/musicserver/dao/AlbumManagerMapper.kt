package com.hua.musicserver.dao

import com.hua.musicserver.bean.AlbumBean
import com.hua.musicserver.bean.MusicBean
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface AlbumManagerMapper {

    @Select("select albumList.*,artistList.`name` as 'artistName',(select count(*) from musiclist where albumlist.id = musiclist.albumId) as 'num'" +
            "from albumList,artistList where albumlist.artistId = artistList.id ORDER BY albumlist.time DESC")
    fun showAlbum():List<AlbumBean>

    @Select(
        "SELECT musiclist.id,musiclist.`name`,albumlist.imgUrl AS imgUrl,musiclist.musicUrl,musiclist.albumId,albumlist.`name` as albumName,artist " +
                "                FROM musicList,albumlist where musiclist.albumId = albumlist.id and albumlist.id= #{id} order by musiclist.createTime desc"
    )
    fun selectMusicFromAlbum(@Param("id")id :String):List<MusicBean>

    @Select("select albumList.*,artistList.`name` as 'artistName',(select count(*) from musiclist where albumlist.id = musiclist.albumId) as 'num'" +
            "from albumList,artistList where albumlist.artistId = artistList.id and albumlist.`name` LIKE #{name} ORDER BY albumlist.time DESC")
    fun selectAlbum(@Param("name") name:String):List<AlbumBean>

}