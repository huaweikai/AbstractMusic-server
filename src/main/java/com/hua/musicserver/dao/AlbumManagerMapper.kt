package com.hua.musicserver.dao

import com.hua.musicserver.bean.AlbumBean
import com.hua.musicserver.bean.MusicBean
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.springframework.stereotype.Repository

const val albumBean = "albumList.*,artistList.`name` as 'artistName',(select count(*) from musiclist where albumlist.id = musiclist.albumId) as 'num'"

@Mapper
@Repository
interface AlbumManagerMapper {

    @Select("select $albumBean" +
            "from albumList,artistList where albumlist.artistId = artistList.id ORDER BY albumlist.time DESC")
    fun showAlbum():List<AlbumBean>

    @Select("select $albumBean" +
            "from albumList,artistList where albumlist.artistId = artistList.id ORDER BY albumlist.time DESC limit 6")
    fun showRecommendAlbum():List<AlbumBean>

    @Select(
        "SELECT $musicBean FROM musicList,albumlist where musiclist.albumId = albumlist.id and albumlist.id= #{id} order by musiclist.createTime desc"
    )
    fun selectMusicFromAlbum(@Param("id")id :String):List<MusicBean>

    @Select("select $albumBean from albumList,artistList where albumlist.artistId = artistList.id and albumlist.`name` LIKE #{name} ORDER BY albumlist.time DESC")
    fun selectAlbum(@Param("name") name:String):List<AlbumBean>

    @Select("select $albumBean from albumList,artistList where albumlist.artistId = artistList.id and albumlist.id = #{id}")
    fun selectAlbumById(@Param("id")id:String):AlbumBean?

    @Select("select $albumBean from albumList,artistList,musiclist where albumlist.artistId = artistList.id and albumlist.id = musiclist.albumId and musiclist.id = #{id}")
    fun selectAlbumByMusicId(@Param("id")id:String):AlbumBean?

}